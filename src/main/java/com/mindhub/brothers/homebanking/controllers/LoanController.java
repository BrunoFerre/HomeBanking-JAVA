package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.brothers.homebanking.dtos.LoanDTO;
import com.mindhub.brothers.homebanking.models.*;
import com.mindhub.brothers.homebanking.models.enums.TransactionType;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientLoanRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.repositories.LoanRepository;
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
    private LoanRepository loanRepository;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
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

        ClientLoan clientLoan = new ClientLoan(loanAplicationDTO.getAmount()*1.2,loanAplicationDTO.getPayments(),loanAplicationDTO.getAmount()*0.2);
        clientLoan.setClient(authClient);
        clientLoanService.saveClientLoan(clientLoan);

        destinationAccount.setBalance(destinationAccount.getBalance()+loanAplicationDTO.getAmount()*0.2);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanAplicationDTO.getAmount(),
                loan.getName()+"Loan aproved", LocalDateTime.now(), loanAplicationDTO.getAmount()*0.2);
        transactionService.saveTransaction(transaction);
        destinationAccount.addTransaction(transaction);
        loan.addClientLoan(clientLoan);
        authClient.addClientLoan(clientLoan);
        accountService.save(destinationAccount);
        clientService.saveClient(authClient);

        return new ResponseEntity<>("Loan Aproved",HttpStatus.CREATED);
    }


    @Transactional
    @PostMapping("/loans/pay")
    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam String numberAccount,
                                          @RequestParam Double amount) {
     Client authClient = clientService.findByEmail(authentication.getName());
     ClientLoan clientLoan = clientLoanRepository.getClientLoan(authClient.getId());
     if (numberAccount.isBlank()){
         return new ResponseEntity<>("Account number not found", HttpStatus.FORBIDDEN);
     }
     if (amount.isNaN() || amount < 0){
         return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
     }
     if (clientLoan == null){
         return new ResponseEntity<>("Loan not exists", HttpStatus.FORBIDDEN);
     }
    if (clientLoan.getClient().getId() != authClient.getId()){
        return new ResponseEntity<>("This account does not belong to an authenticated client", HttpStatus.FORBIDDEN);
    }
    Account account = accountService.findByNumber(numberAccount);
    if (account == null){
        return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
    }
    if (account.getBalance() < amount){
        return new ResponseEntity<>("This account does not have enough balance", HttpStatus.FORBIDDEN);
    }
    account.setBalance(account.getBalance()-amount);
    accountService.save(account);
    clientLoan.setPayments(clientLoan.getPayments()-1);
    clientLoan.setTotalAmount(clientLoan.getTotalAmount()-amount);
    clientLoanRepository.save(clientLoan);
     return new ResponseEntity<>(HttpStatus.OK);
    }

}
