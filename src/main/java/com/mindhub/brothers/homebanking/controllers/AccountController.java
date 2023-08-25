package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
@RestController
public class AccountController {
    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private ClientRepository clientRepository;
    private String randomNumber(){
        String random;
        do {
            int number= (int)(Math.random()* 100 + 999 );
            random= "VIN-"+number;
        }while (accountsRepository.findByNumber(random)!=null);
        return  random;
    }
    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountsRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAcccount(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getAccounts().stream().collect(toList());
    }

    @RequestMapping("/api/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountsRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }



    @RequestMapping(path = "/api/clients/current/accounts",method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication){
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() <=2){
            String accountNumber = randomNumber();
            Account newAccount = new Account(accountNumber, LocalDate.now(),0.0);
            clientRepository.findByEmail(authentication.getName()).addAccount(newAccount);
            accountsRepository.save(newAccount);
        }else{
            return new ResponseEntity<>("Maximo de cuentas alcanzado", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
   /* @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountsRepository.findById(id).map(AccountDTO::new).orElse(null);
    }*/
}
