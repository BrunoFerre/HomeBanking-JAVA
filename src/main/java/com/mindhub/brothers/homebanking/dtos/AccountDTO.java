package com.mindhub.brothers.homebanking.dtos;

import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Transaction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    double balance;
    private Set<TransactionDTO> transactionDTOSet = new HashSet<>();
    public AccountDTO() {
    }

    public AccountDTO(Account acounnt) {
        this.id= acounnt.getId();
        this.number=acounnt.getNumber();
        this.creationDate=acounnt.getCreationDate();
        this.balance=acounnt.getBalance();
        this.transactionDTOSet = new HashSet<>();
        for (Transaction transaction: acounnt.getTransactions()) {
            this.transactionDTOSet.add(new TransactionDTO(transaction));
        }
    }
    public long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactionDTOSet() {
        return transactionDTOSet;
    }


    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                '}';
    }
}
