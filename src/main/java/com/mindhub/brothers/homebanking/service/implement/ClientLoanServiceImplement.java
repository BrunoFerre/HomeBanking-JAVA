package com.mindhub.brothers.homebanking.service.implement;

import com.mindhub.brothers.homebanking.models.ClientLoan;
import com.mindhub.brothers.homebanking.repositories.ClientLoanRepository;
import com.mindhub.brothers.homebanking.service.ClientLoanService;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {
@Autowired
    private ClientLoanRepository clientLoanRepository;
    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
