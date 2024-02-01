package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Card;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);
    Card findByCvv(int cvv);
    Card findByType(CardType type);

    List<Card> findAllByClientAndStatusTrue(Client client);
    boolean existsByNumber(String number);
}