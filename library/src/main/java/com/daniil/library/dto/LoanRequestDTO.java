package com.daniil.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class LoanRequestDTO {
    private int bookId;
    private LocalDate date;
}
