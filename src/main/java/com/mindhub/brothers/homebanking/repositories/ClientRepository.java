package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository <Client, Long> {
    List<Client> findByLastName(String lastName);
}
