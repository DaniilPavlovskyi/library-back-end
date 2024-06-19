package com.daniil.library.service.loan;

import com.daniil.library.dao.LoanDAO;
import com.daniil.library.entity.Loan;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanDAO loanDAO;

    public LoanServiceImpl(LoanDAO loanDAO) {
        this.loanDAO = loanDAO;
    }

    @Override
    public void save(Loan loan) {
        loanDAO.save(loan);
    }
}
