package com.daniil.library.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name="registration_date")
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "client")
    @EqualsAndHashCode.Exclude
    private List<Loan> loans;

    @ManyToMany
    @JoinTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "authority")
    )
    private List<Authority> authorities;

    public void addLoan(Loan loan){
        if (this.loans == null){
            this.loans = new ArrayList<>();
        }
        this.loans.add(loan);
    }
}
