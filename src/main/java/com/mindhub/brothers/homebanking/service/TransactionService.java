package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
