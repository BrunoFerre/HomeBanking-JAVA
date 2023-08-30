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
        Client clientAuth = clientRepository.findByEmail(authentication.getName());
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
        Account account= clientAuth.getAccounts().stream().filter(accountC ->
                accountC.getNumber().equals(accountOrigin)).collect(Collectors.toList()).get(0);
        Account accountDest = accountsRepository.findByNumber(accountDestination);
        if (account.getBalance() >= amount){
            account.setBalance(account.getBalance()-amount);
            accountDest.setBalance(accountDest.getBalance()+amount);
            Transaction debit= new Transaction(TransactionType.DEBIT,amount,description,LocalDateTime.now());
            Transaction credit= new Transaction(TransactionType.CREDIT,amount,description,LocalDateTime.now());
            account.addTransaction(debit);
            accountDest.addTransaction(credit);
            accountsRepository.save(account);
            accountsRepository.save(accountDest);
            transactionRepository.save(debit);
            transactionRepository.save(credit);
        }else{
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Transactions Realized",HttpStatus.CREATED);
    }
}
