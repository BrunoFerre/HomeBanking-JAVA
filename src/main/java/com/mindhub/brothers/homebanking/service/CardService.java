package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.dtos.CardDTO;
import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface CardService {
    List<CardDTO> getCards();
    List<CardDTO> cardsAuthentication(Authentication authentication);
    void addCard(Card card);
    Card findByNumber(String number);
    Card findByCvv(int cvv);
    void delete(Card card);
    Card findById(long id);
    List<Card> findAllByClientAndStatusTrue(Client client);
}
