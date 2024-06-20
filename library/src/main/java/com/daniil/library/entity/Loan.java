package com.daniil.library.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "loan")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @EqualsAndHashCode.Exclude
    private Book book;

    @ManyToOne
    @JoinColumn(name = "username")
    @EqualsAndHashCode.Exclude
    private Client client;

    @Column(name = "start")
    private LocalDate start;

    @Column(name = "end")
    private LocalDate end;

    @Column(name = "status")
    private String status;
}
