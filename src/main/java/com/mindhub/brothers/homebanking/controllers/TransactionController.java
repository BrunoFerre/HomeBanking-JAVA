package com.mindhub.brothers.homebanking.controllers;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.Transaction;
import com.mindhub.brothers.homebanking.models.enums.TransactionType;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.repositories.TransactionRepository;
import com.mindhub.brothers.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> newTransaction(Authentication authentication,
              @RequestParam Double amount,@RequestParam String description,@RequestParam String accountOrigin,
              @RequestParam String accountDestination) {
        if (amount.isNaN() || amount < 0) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()) {
            return new ResponseEntity<>("Invalid description", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.isBlank()) {
            return new ResponseEntity<>("Invalid account origin", HttpStatus.FORBIDDEN);
        }
        if (accountDestination.isBlank()) {
            return new ResponseEntity<>("Invalid account destination", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.equals(accountDestination)) {
            return new ResponseEntity<>("Cannot send transactions to the same account", HttpStatus.FORBIDDEN);
        }
        if (accountsRepository.findByNumber(accountOrigin) == null) {
            return new ResponseEntity<>("Invalid account origin", HttpStatus.FORBIDDEN);
        }
        Client clientAuth = clientRepository.findByEmail(authentication.getName());
        Account originalAccount = accountsRepository.findByNumber(accountOrigin);
        Account destAccount = accountsRepository.findByNumber(accountDestination);

        if (originalAccount.getOwner().getId() != clientAuth.getId()) {
            return new ResponseEntity<>("Invalid account origin", HttpStatus.FORBIDDEN);
        }
        if (originalAccount.getBalance() >= amount){
            originalAccount.setBalance(originalAccount.getBalance()-amount);
            destAccount.setBalance(destAccount.getBalance()+amount);
            Transaction debit= new Transaction(TransactionType.DEBIT,amount*-1,description,LocalDateTime.now(),originalAccount.getBalance());
            originalAccount.addTransaction(debit);
            transactionRepository.save(debit);
            Transaction credit= new Transaction(TransactionType.CREDIT,amount,description,LocalDateTime.now(), destAccount.getBalance());
            destAccount.addTransaction(credit);
            accountsRepository.save(originalAccount);
            accountsRepository.save(destAccount);
            transactionRepository.save(credit);
        }else{
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Transactions Realized",HttpStatus.CREATED);
    }

    @GetMapping("/transactions/findDate")
    public ResponseEntity<Object> getTransactionsbyDateTime(@RequestParam String dateInit,@RequestParam String dateEnd,@RequestParam String numberAcc,
                                                            Authentication authentication) throws DocumentException, IOException {
        Client current = clientService.findByEmail(authentication.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//@param nada mas
        if (!accountsRepository.existsByNumber(numberAcc)){
            return new ResponseEntity<>("this account dont exist", HttpStatus.BAD_REQUEST);
        }
        if (current == null){
            return new ResponseEntity<>("you are not allowed to see this", HttpStatus.FORBIDDEN);
        }
        if (dateInit == null){
            return new ResponseEntity<>("Please, fill the date requeriment", HttpStatus.BAD_REQUEST);
        }else if (dateEnd == null){
            new ResponseEntity<>("Please, fill the date end requeriment", HttpStatus.BAD_REQUEST);
        }
        if (dateInit.equals(dateEnd)){
            return new ResponseEntity<>("you cannot do this", HttpStatus.BAD_REQUEST);
        }
        LocalDateTime localDateTime = LocalDateTime.parse(dateInit, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateEnd, formatter);
        List<Transaction> transf = transactionRepository.findByDateBetweenAndAccountNumber(localDateTime, localDateTime2, numberAcc);

        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);
        doc.open();
        Image logo = Image.getInstance("/home/bruno/IdeaProjects/HomeBanking-JAVA/src/main/resources/static/web/bank.png");
        logo.scaleToFit(100, 100);
        logo.setAlignment(Image.ALIGN_CENTER | Image.TEXTWRAP);
        PdfPTable table = new PdfPTable(4);
        table.addCell("Type");
        table.addCell("Description");
        table.addCell("Amount");
        table.addCell("Date");
        doc.add(logo);
        for (Transaction transaction : transf) {
            table.addCell(transaction.getType().toString());
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDate().format(formatter));
        }
        doc.add(table);
        doc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=transactions.pdf");
        byte[] pdf = out.toByteArray();
        return new ResponseEntity<>(pdf,headers, HttpStatus.OK);
    }
}