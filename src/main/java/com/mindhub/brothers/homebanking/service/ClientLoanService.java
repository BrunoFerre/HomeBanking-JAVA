package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.ClientLoan;
import com.mindhub.brothers.homebanking.models.Loan;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);
    boolean existByClientAndLoan(Client client, Loan loan);
}
