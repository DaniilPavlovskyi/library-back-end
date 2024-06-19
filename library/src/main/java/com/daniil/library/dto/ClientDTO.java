package com.daniil.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ClientDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate registrationDate;
    private List<LoanDTO> loans;

}
