package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.dtos.LoanDTO;
import com.mindhub.brothers.homebanking.models.Loan;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> getLoans();
    Loan findById(long id);
}
