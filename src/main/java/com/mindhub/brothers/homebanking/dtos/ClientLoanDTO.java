package com.mindhub.brothers.homebanking.dtos;

import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.ClientLoan;
import com.mindhub.brothers.homebanking.models.Loan;


public class ClientLoanDTO {
    private long id;
    private Integer payments;
    private double amount;
    private Client client;
    private Loan clientsLoan;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.payments= clientLoan.getPayments();
        this.amount = clientLoan.getAmount();
        this.client = clientLoan.getClient();
    }

    public long getId() {
        return id;
    }

    public Integer getPayments() {
        return payments;
    }

    public double getAmount() {
        return amount;
    }

    public Client getClient() {
        return client;
    }

    public Loan getClientsLoan() {
        return clientsLoan;
    }
}
