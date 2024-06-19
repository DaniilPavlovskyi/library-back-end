package com.daniil.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "authority")
@Getter @Setter
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "authority")
    private String authority;

    @ManyToMany(mappedBy = "authorities")
    private List<Client> clients;
}
