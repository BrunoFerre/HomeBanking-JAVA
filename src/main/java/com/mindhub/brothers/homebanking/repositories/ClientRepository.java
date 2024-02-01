package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository <Client,Long> {
    Client findByEmail(String email);
}
