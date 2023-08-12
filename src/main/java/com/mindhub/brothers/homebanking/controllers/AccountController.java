package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
@RestController
public class AccountController {
    @Autowired
    private AccountsRepository accountsRepository;
    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountsRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountsRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
}
