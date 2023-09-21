package com.mindhub.brothers.homebanking.dtos;

public class LoanAplicationDTO {

    private long id;
    private Double amount;
    private int payments;
    private String accountDestiny;
    public LoanAplicationDTO() {
    }
    public LoanAplicationDTO(long id, Double amount, int payments, String accountDestiny) {
        this.amount = amount;
        this.payments = payments;
        this.accountDestiny = accountDestiny;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }

}

