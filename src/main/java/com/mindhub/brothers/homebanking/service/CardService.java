package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.dtos.CardDTO;
import com.mindhub.brothers.homebanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    List<CardDTO> getCards();
    List<CardDTO> cardsAuthentication(Authentication authentication);
    void addCard(Card card);
    Card findByNumber(String number);
    Card findByCvv(int cvv);
}
