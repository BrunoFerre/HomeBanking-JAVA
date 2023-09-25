package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    Client findById(Long id);
    List<ClientDTO> getClients();

    ClientDTO getClientAuthentication(Authentication authentication);

    Client findByEmail(String email);
    void saveClient(Client client);
}
