package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.service.AccountService;
import com.mindhub.brothers.homebanking.service.ClientService;
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
public class    AccountController {

    @Autowired
    private AccountService accountService;
//    @Autowired
//    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts();
    }

    @RequestMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAcccount(Authentication authentication){
        return accountService.getAcccount(authentication);
    }

    @RequestMapping(path = "/api/clients/current/accounts",method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication){
        if (clientService.findByEmail(authentication.getName()).getAccounts().size() <=2){
            String accountNumber = RandomNumberGenerate.accountNumber();
            Account newAccount = new Account("VIN-"+accountNumber, LocalDate.now(),0.0);
            clientService.findByEmail(authentication.getName()).addAccount(newAccount);
            accountService.save(newAccount);
        }else{
            return new ResponseEntity<>("Maximum of accounts made", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/api/clients/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        Account acc = accountService.accountId(id);
        if (client.getId() == acc.getOwner().getId()){
            return new ResponseEntity<>(new AccountDTO(acc),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
    }
    }
}

