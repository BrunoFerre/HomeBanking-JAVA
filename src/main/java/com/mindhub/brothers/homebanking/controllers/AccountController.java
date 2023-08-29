package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.utils.RandomNumberGenerate;
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

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountsRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAcccount(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()))
                .getAccounts().stream().collect(toList());
    }

    @RequestMapping(path = "/api/clients/current/accounts",method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication){
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() <=2){
            int accountNumber = RandomNumberGenerate.getRandomNumber(1000, 9999);
            Account newAccount = new Account("VIN-"+accountNumber, LocalDate.now(),0.0);
            clientRepository.findByEmail(authentication.getName()).addAccount(newAccount);
            accountsRepository.save(newAccount);
        }else{
            return new ResponseEntity<>("Maximum of accounts made", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/api/clients/accounts/{id}")
    public ResponseEntity<Object>  getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account acc = accountsRepository.findById(id).orElse(null);
        if (client.getId() == acc.getOwner().getId()){
            return new ResponseEntity<>(new AccountDTO(acc),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
    }
    }
}

