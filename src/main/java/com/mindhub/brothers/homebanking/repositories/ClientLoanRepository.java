package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.ClientLoan;
import com.mindhub.brothers.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan,Long> {
    boolean existsByClientAndLoan(Client client, Loan loan);
    List<ClientLoan> findAllByClient(Client client);

}
