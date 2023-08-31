package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.Transaction;
import com.mindhub.brothers.homebanking.models.enums.TransactionType;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> newTransaction(Authentication authentication,
              @RequestParam Double amount,@RequestParam String description,@RequestParam String accountOrigin,
              @RequestParam String accountDestination) {

        if (amount.isNaN() || amount < 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()) {
            return new ResponseEntity<>("Invalid description", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.isBlank()) {
            return new ResponseEntity<>("Invalid account origin", HttpStatus.FORBIDDEN);
        }
        if (accountDestination.isBlank()) {
            return new ResponseEntity<>("Invalid account destination", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.equals(accountDestination)) {
            return new ResponseEntity<>("Cannot send transactions to the same account", HttpStatus.FORBIDDEN);
        }
        if (accountsRepository.findByNumber(accountOrigin) == null) {
            return new ResponseEntity<>("Invalid account origin", HttpStatus.FORBIDDEN);
        }
        Client clientAuth = clientRepository.findByEmail(authentication.getName());
        Account originalAccount = accountsRepository.findByNumber(accountOrigin);
        Account destAccount = accountsRepository.findByNumber(accountDestination);

        if (originalAccount.getOwner().getId() != clientAuth.getId()) {
            return new ResponseEntity<>("Invalid account origin", HttpStatus.FORBIDDEN);
        }
        if (originalAccount.getBalance() >= amount){
            originalAccount.setBalance(originalAccount.getBalance()-amount);
            destAccount.setBalance(destAccount.getBalance()+amount);
            Transaction debit= new Transaction(TransactionType.DEBIT,amount*-1,description,LocalDateTime.now());
            Transaction credit= new Transaction(TransactionType.CREDIT,amount,description,LocalDateTime.now());
            originalAccount.addTransaction(debit);
            destAccount.addTransaction(credit);
            accountsRepository.save(originalAccount);
            accountsRepository.save(destAccount);
            transactionRepository.save(debit);
            transactionRepository.save(credit);
        }else{
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Transactions Realized",HttpStatus.CREATED);
    }
}
