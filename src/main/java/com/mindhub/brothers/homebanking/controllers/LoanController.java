package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.brothers.homebanking.dtos.LoanDTO;
import com.mindhub.brothers.homebanking.models.*;
import com.mindhub.brothers.homebanking.models.enums.TransactionType;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;
    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanAplicationDTO loanAplicationDTO) {

        if (loanAplicationDTO.getAmount()<1){
            return new ResponseEntity<>("Amount must be greater than 0", HttpStatus.FORBIDDEN);
        }

        if (loanAplicationDTO.getPayments()<=0){
            return new ResponseEntity<>("Payments must be greater than 0", HttpStatus.FORBIDDEN);
        }

        if(loanAplicationDTO.getAccountDestiny().isBlank()){
            return new ResponseEntity<>("Account destination not found", HttpStatus.FORBIDDEN);
        }

        Client authClient = clientService.findByEmail(authentication.getName());

        Account destinationAccount = accountService.findByNumber(loanAplicationDTO.getAccountDestiny());

        Loan loan = loanService.findById(loanAplicationDTO.getId());

        if (destinationAccount.getOwner().getId() != authClient.getId()){
            return new ResponseEntity<>("Account destiny error", HttpStatus.UNAUTHORIZED);
        }

        if(clientLoanService.existByClientAndLoan(authClient,loan)){
            return new ResponseEntity<>("Loan already exist",HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanAplicationDTO.getPayments())){
            return new ResponseEntity<>("Payment not found", HttpStatus.FORBIDDEN);
        }

        if (!authClient.getAccounts().contains(destinationAccount)){
            return new ResponseEntity<>("This account does not belong to an authenticated client", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = new ClientLoan((int) (loanAplicationDTO.getAmount()+(loanAplicationDTO.getAmount()*0.2)),
                loanAplicationDTO.getAmount());
        clientLoanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanAplicationDTO.getAmount(),
                loan.getName()+"Loan aproved", LocalDateTime.now());
        transactionService.saveTransaction(transaction);

        destinationAccount.setBalance(destinationAccount.getBalance()+loanAplicationDTO.getAmount());
        destinationAccount.addTransaction(transaction);

        loan.addClientLoan(clientLoan);
        authClient.addClientLoan(clientLoan);
        accountService.save(destinationAccount);
        clientService.saveClient(authClient);

        return new ResponseEntity<>("Loan Aproved",HttpStatus.CREATED);
    }
}
