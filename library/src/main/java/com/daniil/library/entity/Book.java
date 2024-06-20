package com.daniil.library.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "category")
    private String category;

    @Column(name = "is_present")
    private boolean isPresent;

    @OneToMany(mappedBy = "book")
    @EqualsAndHashCode.Exclude
    private List<Loan> loans;

    public void addLoan(Loan loan){
        if (this.loans == null){
            this.loans = new ArrayList<>();
        }
        this.loans.add(loan);
    }
}
