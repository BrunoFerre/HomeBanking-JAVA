package com.mindhub.brothers.homebanking.dtos;

import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.ClientLoan;
import com.mindhub.brothers.homebanking.models.Loan;

import java.util.List;
import java.util.stream.Collectors;


public class ClientLoanDTO {
    private long id;
    private long id_loan;

    private String loan;

    private double amount;
    private int payments;
    private Double totalAmount;
    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.id_loan = clientLoan.getLoan().getId();
        this.loan= clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    public long getId() {
        return id;
    }

    public long getId_loan() {
        return id_loan;
    }

    public String getLoan() {
        return loan;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}
