package com.mindhub.brothers.homebanking.dtos;

import com.mindhub.brothers.homebanking.models.ClientLoan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanDTO {
    private long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments;
    private Set<ClientLoan> clientLoan = new HashSet<>();

    public LoanDTO() {
    }

    public LoanDTO(String name, double maxAmount, List<Integer> payments, Set<ClientLoan> clientLoan) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.clientLoan = clientLoan;
    }
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public Set<ClientLoan> getClientLoan() {
        return clientLoan;
    }
}
