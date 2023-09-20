package com.mindhub.brothers.homebanking.dtos;

public class CardPaymentDTO {
    private String cardNumber;
    private Double amount;
    private int cvv;
    private String description;

    public CardPaymentDTO() {
    }

    public CardPaymentDTO(String cardNumber, Double amount, int cvv, String description) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.cvv = cvv;
        this.description = description;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public int getCvv() {
        return cvv;
    }

    public String getDescription() {
        return description;
    }
}
