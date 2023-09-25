package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.ClientLoan;
import com.mindhub.brothers.homebanking.models.Loan;

import java.util.List;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);
    boolean existByClientAndLoan(Client client, Loan loan);
    List<ClientLoan> findAllByClient(Client client);
}
