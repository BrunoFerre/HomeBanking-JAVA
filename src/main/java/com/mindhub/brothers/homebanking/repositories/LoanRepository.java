package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {
//Loan findById(long id);


}
