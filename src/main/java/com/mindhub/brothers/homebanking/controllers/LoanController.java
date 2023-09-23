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
import com.mindhub.brothers.homebanking.utils.InterestRate;
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

import static com.mindhub.brothers.homebanking.utils.InterestRate.calculateInterest;
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
        if (loanAplicationDTO.getAmount()<1000){
            return new ResponseEntity<>("Amount must be greater than 1000", HttpStatus.FORBIDDEN);
        }

        if (loanAplicationDTO.getPayments()<=0){
            return new ResponseEntity<>("Payments must be greater than 0", HttpStatus.FORBIDDEN);
        }

        if(loanAplicationDTO.getAccountDestiny().isBlank()){
            return new ResponseEntity<>("Account destination not found", HttpStatus.FORBIDDEN);
        }
        if(loanAplicationDTO.getId() == 0) {
            return new ResponseEntity<>("Loan id not found", HttpStatus.FORBIDDEN);
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
        if (!authClient.getAccounts().contains(destinationAccount)){
            return new ResponseEntity<>("This account does not belong to an authenticated client", HttpStatus.FORBIDDEN);
        }
        double totalAmount = calculateInterest(loanAplicationDTO,loan);
        destinationAccount.setBalance(destinationAccount.getBalance()+loanAplicationDTO.getAmount());

        ClientLoan clientLoan = new ClientLoan(loanAplicationDTO.getPayments(),totalAmount);
        clientLoan.setClient(authClient);
        clientLoan.setLoan(loan);
        clientLoanService.saveClientLoan(clientLoan);
        Transaction transaction = new Transaction(TransactionType.CREDIT,loanAplicationDTO.getAmount(),loan.getName(),LocalDateTime.now(),destinationAccount.getBalance());
        destinationAccount.addTransaction(transaction);
        transactionService.saveTransaction(transaction);
        accountService.save(destinationAccount);
        clientService.saveClient(authClient);
        return new ResponseEntity<>("Loan Aproved",HttpStatus.CREATED);

    }
    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan (Authentication authentication, @RequestBody LoanDTO loanDTO) {
        Client authClient = clientService.findByEmail(authentication.getName());
        if (loanDTO.getName().isBlank()){
            return new ResponseEntity<>("Loan name not found", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount()<1000){
            return new ResponseEntity<>("Max amount must be greater than 1000", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayments().size() ==0 || loanDTO.getPayments().isEmpty()){
            return new ResponseEntity<>("Payments must be greater than 0", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getInterest()<0){
            return new ResponseEntity<>("Interest must be greater than 0", HttpStatus.FORBIDDEN);
        }
        if (!authClient.getEmail().contains("admin.com")){
        return new ResponseEntity<>("You are not an admin", HttpStatus.FORBIDDEN);
        }
        boolean exist = loanRepository.existsByName(loanDTO.getName());
        if (exist){
        return new ResponseEntity<>("Loan already exist",HttpStatus.FORBIDDEN);
        }
        Loan loan = new Loan(loanDTO.getName(),loanDTO.getMaxAmount(),loanDTO.getPayments(),loanDTO.getInterest());
        loanRepository.save(loan);
        return new ResponseEntity<>("Loan created successfu",HttpStatus.OK);
    }

//     @Transactional
//    @PostMapping("/loans/pay")
//    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam String numberAccount,
//                                          @RequestParam Double amount) {
//     Client authClient = clientService.findByEmail(authentication.getName());
//     if (numberAccount.isBlank()){
//         return new ResponseEntity<>("Account number not found", HttpStatus.FORBIDDEN);
//     }
//     if (amount.isNaN() || amount < 0){
//         return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
//     }
//     if (clientLoan == null){
//         return new ResponseEntity<>("Loan not exists", HttpStatus.FORBIDDEN);
//     }
//    if (clientLoan.getClient().getId() != authClient.getId()){
//        return new ResponseEntity<>("This account does not belong to an authenticated client", HttpStatus.FORBIDDEN);
//    }
//    Account account = accountService.findByNumber(numberAccount);
//    if (account == null){
//        return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
//    }
//    if (account.getBalance() < amount){
//        return new ResponseEntity<>("This account does not have enough balance", HttpStatus.FORBIDDEN);
//    }
//    account.setBalance(account.getBalance()-amount);
//    accountService.save(account);
//    clientLoan.setPayments(clientLoan.getPayments()-1);
//    clientLoan.setAmount(clientLoan.getAmount()-amount);
//    clientLoanRepository.save(clientLoan);
//     return new ResponseEntity<>(HttpStatus.OK);
//    }
}
