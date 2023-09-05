package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.CardDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.enums.CardColor;
import com.mindhub.brothers.homebanking.models.enums.CardType;
import com.mindhub.brothers.homebanking.repositories.CardRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.service.CardService;
import com.mindhub.brothers.homebanking.service.ClientService;
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
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/cards")
    public List<CardDTO>getCards(){
        return cardService.getCards();
    }
    @RequestMapping("/clients/current/cards")
    public List<CardDTO>getCards(Authentication authentication){
        return cardService.cardsAuthentication(authentication);
    }
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> addCard(Authentication authentication, @RequestParam String type,
            @RequestParam String color) {
        Client client = clientService.findByEmail(authentication.getName());
        if (type.isBlank()) {
            return new ResponseEntity<>("Missing type ", HttpStatus.FORBIDDEN);
        }
        if(color.isBlank()){
           return new ResponseEntity<>("Missing color", HttpStatus.FORBIDDEN);
        }
        String numberCard;
        do {
            numberCard = RandomNumberGenerate.cardNumber();

        }while(cardService.findByNumber(numberCard) != null);


        int cardCvv = RandomNumberGenerate.getCardCVV();
        do {cardCvv = RandomNumberGenerate.getCardCVV();} while (cardService.findByCvv(cardCvv) != null);

        for (Card card : client.getCards()) {
            if (card.getType().equals(CardType.valueOf((type))) && card.getColor().equals(CardColor.valueOf(color))) {
                return new ResponseEntity<>("Card already exists", HttpStatus.FORBIDDEN);
            }
        }

        Card newCard = new Card(client.getFirstName() + " " + client.getLastName(), CardType.valueOf(type), CardColor.valueOf(color),
                numberCard, cardCvv, LocalDate.now(), LocalDate.now().plusYears(5));
        client.addCard(newCard);
        cardService.addCard(newCard);
        return new ResponseEntity<>("Card Created", HttpStatus.CREATED);
    }
}
