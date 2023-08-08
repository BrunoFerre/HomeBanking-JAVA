package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RepositoryTransaction extends JpaRepository<Transaction,Long> {
}
