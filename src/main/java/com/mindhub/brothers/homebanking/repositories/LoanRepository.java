package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
//Loan findById(long id);
    boolean existsByName(String name);
}
