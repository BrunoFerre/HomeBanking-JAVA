package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.CardPaymentDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.Transaction;
import com.mindhub.brothers.homebanking.models.enums.TransactionType;
import com.mindhub.brothers.homebanking.service.AccountService;
import com.mindhub.brothers.homebanking.service.CardService;
import com.mindhub.brothers.homebanking.service.ClientService;
import com.mindhub.brothers.homebanking.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;
@Autowired
private TransactionService transactionService;
    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<Object> newPayment(@RequestBody CardPaymentDTO cardPaymentDTO){
        String cardNumber = cardPaymentDTO.getCardNumber();
        if (cardNumber.isBlank()){
            return new ResponseEntity<>("Card number cannot be empty", HttpStatus.FORBIDDEN);
        }
        Card exist = cardService.findByNumber(cardNumber);
        if (exist.getStatus() == false || exist == null){
            return new ResponseEntity<>("Card not exist", HttpStatus.NOT_FOUND);
        }
        if (exist.getCvv() != cardPaymentDTO.getCvv()){
            return new ResponseEntity<>("Error CVV", HttpStatus.BAD_GATEWAY);
        }

        long idClient = exist.getClient().getId();
        Client client = clientService.findById(idClient);
        Set<Account> accountList = client.getAccounts();
        List<Account> maxBalanceAccountList = new ArrayList<>();
        for (Account account : accountList) {
            if (account.getStatus()==true){
                maxBalanceAccountList.add(account);
                System.out.println(maxBalanceAccountList);
            }
        }
        Account maxBalanceAccount = maxBalanceAccountList.stream().reduce((ac2, ac3) -> ac2.getBalance() > ac3.getBalance() ? ac2 : ac3).orElse(null);
        if (maxBalanceAccount.getBalance() < cardPaymentDTO.getAmount()){
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_GATEWAY);
        }else{
            maxBalanceAccount.setBalance(maxBalanceAccount.getBalance() - cardPaymentDTO.getAmount());
            Transaction transaction = new Transaction(TransactionType.DEBIT, cardPaymentDTO.getAmount(), cardPaymentDTO.getDescription(), LocalDateTime.now(), maxBalanceAccount.getBalance());
            maxBalanceAccount.addTransaction(transaction);
            transactionService.saveTransaction(transaction);
            accountService.save(maxBalanceAccount);
        }
        return new ResponseEntity<>("Payment successful", HttpStatus.OK);
    }
}
