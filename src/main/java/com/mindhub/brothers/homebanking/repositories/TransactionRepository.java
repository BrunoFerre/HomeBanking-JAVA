package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByDateBetweenAndAccountNumber(LocalDateTime localDateTime, LocalDateTime localDateTime2, String numberAcc);
}
