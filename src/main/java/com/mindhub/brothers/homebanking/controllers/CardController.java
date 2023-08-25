package com.mindhub.brothers.homebanking.controllers;

import com.mindhub.brothers.homebanking.dtos.CardDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.enums.CardColor;
import com.mindhub.brothers.homebanking.models.enums.CardType;
import com.mindhub.brothers.homebanking.repositories.CardRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
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
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    private String randomNumberCard(){
        String randomCards="";
        for (int i = 0; i < 4; i++) {
            int min = 1000;
            int max = 8999;
            randomCards+= (int)(Math.random()*(max-min+1)+min)+"-";
        }
        return randomCards;
    }
    private int randomCvv(){
        int cvv= (int)(Math.random()*899+100);
        return cvv;
    }
    @RequestMapping("/api/cards")
    public List<CardDTO>getCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }
    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO>getCards(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getCards()
                .stream().collect(toList());
    }
    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> addCard(
            Authentication authentication,@RequestParam CardType type, @RequestParam CardColor color){
        if (type==null|| color==null){
            return new ResponseEntity<>("Missing type", HttpStatus.FORBIDDEN);
        }
    String numberCard;
        do {numberCard=randomNumberCard();}while (cardRepository.findByNumber(numberCard)!=null);

        int cardCvv=randomCvv();
        do {
            cardCvv=randomCvv();
        }while (cardRepository.findByCvv(cardCvv)!=null);
        Client client=clientRepository.findByEmail(authentication.getName());
        for (Card card:client.getCards()){
            if (card.getType().equals(type)&&card.getColor().equals(color)){
                return new ResponseEntity<>("Card already exists",HttpStatus.FORBIDDEN);
            }
        }
        Card newCard=new Card(client.getFirstName()+" "+client.getLastName(),type,color,numberCard,cardCvv,LocalDate.now(),LocalDate.now().plusYears(5));
        client.addCard(newCard);
        clientRepository.save(client);
        cardRepository.save(newCard);
        return new ResponseEntity<>(newCard,HttpStatus.CREATED);
        }
}
