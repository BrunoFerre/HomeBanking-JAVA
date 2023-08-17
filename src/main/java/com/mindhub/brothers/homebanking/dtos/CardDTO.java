package com.mindhub.brothers.homebanking.dtos;

import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.models.enums.CardColor;
import com.mindhub.brothers.homebanking.models.enums.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    public CardDTO() {
    }
    public  CardDTO(Card card){
        this.cardHolder = card.getClient().getFirstName() + " " + card.getClient().getLastName();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
    }


    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }
}
