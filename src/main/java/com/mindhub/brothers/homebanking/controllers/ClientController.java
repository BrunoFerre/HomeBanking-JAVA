package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountsRepository accountsRepository;
    private String randomNumber(){
        String random;
        do {
            int number= (int)(Math.random()*100+999);
            random= "VIN-"+number;
        }while (accountsRepository.findByNumber(random)!=null);
        return  random;
    }
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
      return  clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping("/clients/{current}")
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(newClient);
        String accountNumber = randomNumber();
        Account newAccount = new Account(accountNumber, LocalDate.now(),0.0);
        newClient.addAccount(newAccount);
        accountsRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
