package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.enums.AccountType;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.service.AccountService;
import com.mindhub.brothers.homebanking.service.ClientService;
import com.mindhub.brothers.homebanking.utils.RandomNumberGenerate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class    AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private ClientService clientService;
    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }
    @GetMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAcccount(Authentication authentication){
        return accountService.getAcccount(authentication);
    }
    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam AccountType type){
        if (type.toString().isBlank()){return new ResponseEntity<>("Missing type", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        List <Account> acounts = accountsRepository.findByClientAndStatusIsTrue(client);
        System.out.println(acounts);
        if (acounts.size()<=2){
            String accountNumber = RandomNumberGenerate.accountNumber();
            Account newAccount = new Account("VIN-"+accountNumber, LocalDate.now(),
                    0.0, type,true);
            clientService.findByEmail(authentication.getName()).addAccount(newAccount);
            accountService.save(newAccount);
        }else{
            return new ResponseEntity<>("Maximum of accounts made", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/api/clients/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        Account acc = accountService.accountId(id);
        if (client.getId() == acc.getOwner().getId()){
            return new ResponseEntity<>(new AccountDTO(acc),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
    }
    }
    @PutMapping("/api/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount(Authentication authentication,@PathVariable Long id){
        if (id == null){
            return new ResponseEntity<>("Missing id", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Account acc = accountService.accountId(id);
        List <Account> acounts = accountsRepository.findByClientAndStatusIsTrue(client);
        if (acounts.size()==1){
            return new ResponseEntity<>("You cannot delete your only account", HttpStatus.NOT_ACCEPTABLE);
        }
        if (client.getId() == acc.getOwner().getId()){
            acc.setStatus(false);
            accountService.save(acc);
            return new ResponseEntity<>("Account deleted",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }
    }
}
