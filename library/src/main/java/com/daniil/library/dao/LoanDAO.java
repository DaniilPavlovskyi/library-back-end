package com.daniil.library.dao;

import com.daniil.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanDAO extends JpaRepository<Loan, Integer> {
}
