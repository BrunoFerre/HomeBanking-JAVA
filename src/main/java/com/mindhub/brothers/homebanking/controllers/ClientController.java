package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.enums.AccountType;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.service.ClientService;
import com.mindhub.brothers.homebanking.utils.RandomNumberGenerate;
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
    @Autowired
    private ClientService clientService;
    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
      return  clientService.getClients();
    }
    @GetMapping("/clients/{current}")
    public ClientDTO getClient(Authentication authentication ) {
        return clientService.getClientAuthentication(authentication);
    }
    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank()) {
            return new ResponseEntity<>("First name cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()) {
            return new ResponseEntity<>("Last name cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()) {
            return new ResponseEntity<>("Email cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()) {
            return new ResponseEntity<>("Password cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);
        String accountNumber = RandomNumberGenerate.accountNumber();
        Account newAccount = new Account("VIN-"+accountNumber, LocalDate.now(),0.0, AccountType.CURRENT,true);
        newClient.addAccount(newAccount);
        accountsRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
