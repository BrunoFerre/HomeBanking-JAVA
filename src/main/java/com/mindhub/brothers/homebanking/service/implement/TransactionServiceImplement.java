package com.mindhub.brothers.homebanking.service.implement;

import com.mindhub.brothers.homebanking.models.Transaction;
import com.mindhub.brothers.homebanking.repositories.TransactionRepository;
import com.mindhub.brothers.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
