package com.mindhub.brothers.homebanking.service.implement;

import com.mindhub.brothers.homebanking.dtos.LoanDTO;
import com.mindhub.brothers.homebanking.models.Loan;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.repositories.LoanRepository;
import com.mindhub.brothers.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService  {
    @Autowired
    private LoanRepository loanRepository;
   @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    @Override
    public Loan findById(long id) {
       return loanRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByName(String name) {
        return loanRepository.existsByName(name);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }

}
