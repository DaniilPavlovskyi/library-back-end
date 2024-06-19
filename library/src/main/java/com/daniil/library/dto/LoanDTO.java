package com.daniil.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LoanDTO {
    private String title;
    private LocalDate start;
    private LocalDate end;
    private String status;
}
