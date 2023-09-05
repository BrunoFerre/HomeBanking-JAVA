package com.mindhub.brothers.homebanking.service.implement;

import com.mindhub.brothers.homebanking.dtos.CardDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.repositories.CardRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }

    @Override
    public List<CardDTO> cardsAuthentication(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getCards().stream().collect(toList());
    }

    @Override
    public void addCard(Card card){
        cardRepository.save(card);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card findByCvv(int cvv) {
        return cardRepository.findByCvv(cvv);
    }
}
