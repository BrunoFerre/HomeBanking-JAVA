package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.CardDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.enums.CardColor;
import com.mindhub.brothers.homebanking.models.enums.CardType;
import com.mindhub.brothers.homebanking.repositories.CardRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.utils.RandomNumberGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards")
    public List<CardDTO>getCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }
    @RequestMapping("/clients/current/cards")
    public List<CardDTO>getCards(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getCards()
                .stream().collect(toList());
    }
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication,@RequestParam CardType type,
            @RequestParam CardColor color) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (type == null||color == null) {
            return new ResponseEntity<>("Missing type or empty type", HttpStatus.FORBIDDEN);
        }
        String numberCard;
        do {numberCard = RandomNumberGenerate.CardNumber();} while (cardRepository.findByNumber(numberCard) != null);
        int cardCvv = RandomNumberGenerate.getCardCVV();
        do {cardCvv = RandomNumberGenerate.getCardCVV();} while (cardRepository.findByCvv(cardCvv) != null);
        for (Card card : client.getCards()) {
            if (card.getType().equals(type) && card.getColor().equals(color)) {
                return new ResponseEntity<>("Card already exists", HttpStatus.FORBIDDEN);
            }
        }
        System.out.println(type);
        System.out.println(color);
        Card newCard = new Card(client.getFirstName() + " " + client.getLastName(), type, color, numberCard, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
        client.addCard(newCard);
        cardRepository.save(newCard);
        return new ResponseEntity<>("Card Created", HttpStatus.CREATED);
    }
}
