package com.daniil.library.controller;

import com.daniil.library.dto.AuthResponseDTO;
import com.daniil.library.dto.ClientDTO;
import com.daniil.library.dto.LoanDTO;
import com.daniil.library.entity.Authority;
import com.daniil.library.entity.Client;
import com.daniil.library.security.ClientDetailsService;
import com.daniil.library.security.JwtUtil;
import com.daniil.library.service.authority.AuthorityService;
import com.daniil.library.service.client.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${link}")
public class ClientRESTController {
    private final ClientService clientService;
    private final AuthorityService authorityService;
    private final ClientDetailsService clientDetailsService;
    private final JwtUtil jwtUtil;

    public ClientRESTController(ClientService clientService, AuthorityService authorityService, ClientDetailsService clientDetailsService, JwtUtil jwtUtil) {
        this.clientService = clientService;
        this.authorityService = authorityService;
        this.clientDetailsService = clientDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("api/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        UserDetails userDetails = clientDetailsService.loadUserByUsername(username);

        if (new BCryptPasswordEncoder().matches(password, userDetails.getPassword().substring("{bcrypt}".length()))) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            List<String> roles = userDetails
                    .getAuthorities()
                    .stream()
                    .map(e -> e.getAuthority().substring("ROLE_".length()))
                    .collect(Collectors.toList());

            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(new AuthResponseDTO(token, roles));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("api/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        username = username.trim();
        password = password.trim();

        if (username.length() < 3 || username.length() > 15 || password.length() < 6 || password.length() > 32) {
            return ResponseEntity.badRequest().body("Bad username/password.");
        }

        if (clientService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Such user exists.");
        }


        Client client = new Client();

        Authority authority = authorityService.findByName("ROLE_USER");
        client.setAuthorities(List.of(authority));
        client.setUsername(username);
        client.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(password));
        client.setEnabled(true);
        client.setRegistrationDate(LocalDate.now());

        clientService.save(client);
        authorityService.save(authority);

        return ResponseEntity.ok("Success");
    }

    @GetMapping("api/account")
    public ResponseEntity<ClientDTO> account(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        Client client = clientService.findByUsername(jwtUtil.extractUsername(bearerToken.substring(7)));

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setUsername(client.getUsername());
        clientDTO.setFirstName(client.getFirstName());
        clientDTO.setLastName(client.getLastName());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setRegistrationDate(client.getRegistrationDate());

        List<LoanDTO> loanDTOS = new ArrayList<>();

        for(var loan : client.getLoans()){
            loanDTOS.add(new LoanDTO(loan.getBook().getTitle(), loan.getStart(), loan.getEnd(), loan.getStatus()));
        }

        clientDTO.setLoans(loanDTOS);

        return ResponseEntity.ok(clientDTO);
    }

    @PutMapping("api/update-account")
    public ResponseEntity<String> update(@RequestBody Map<String, String> clientInfo, HttpServletRequest request){
        String firstName = clientInfo.get("firstName");
        String lastName = clientInfo.get("lastName");
        String email = clientInfo.get("email");

        String bearerToken = request.getHeader("Authorization");
        Client client = clientService.findByUsername(jwtUtil.extractUsername(bearerToken.substring(7)));

        if (StringUtils.hasText(firstName) && isValidName(firstName)) {
            client.setFirstName(firstName);
        }

        if (StringUtils.hasText(lastName) && isValidName(lastName)) {
            client.setLastName(lastName);
        }

        if (StringUtils.hasText(email) && isValidEmail(email)) {
            client.setEmail(email);
        }

        clientService.save(client);

        return ResponseEntity.ok("Success");
    }

    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z]+$");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
}