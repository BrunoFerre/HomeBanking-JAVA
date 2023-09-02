package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.brothers.homebanking.dtos.LoanDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountsRepository accountRepository;
    @Transactional
    @PostMapping("/loan")
    public ResponseEntity<String> newLoan(Authentication authentication, @RequestBody LoanAplicationDTO loanAplicationDTO) {
        Client authClient = clientRepository.findByEmail(authentication.getName());
        Account destinationAccount = accountRepository.findByNumber(loanAplicationDTO.getAccountDestiny());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
