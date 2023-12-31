package com.mindhub.brothers.homebanking.dtos;

import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Transaction;
import com.mindhub.brothers.homebanking.models.enums.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private AccountType type;
    private Boolean status;
    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO() {
    }

    public AccountDTO(Account acounnt) {
        this.id= acounnt.getId();
        this.number=acounnt.getNumber();
        this.creationDate=acounnt.getCreationDate();
        this.balance=acounnt.getBalance();
        this.transactions = acounnt.getTransactions()
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toSet());
        this.type = acounnt.getType();
        this.status = acounnt.getStatus();
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
    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountType getType() {
        return type;
    }

    public Boolean getStatus() {
        return status;
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
