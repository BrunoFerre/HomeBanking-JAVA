package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AccountsRepository extends JpaRepository<Account,Long> {
    Account findByNumber(String number);
    Account findById(long id);
    boolean existsByNumber(String number);
    List<Account> findByClientAndStatusIsTrue(Client client);

    List<Account> findAllByClientAndStatusTrue(Client client);
   /* @Query("SELECT c FROM account c WHERE c.client_id = :clientId AND c.balance = (SELECT MAX(c2.balance) FROM account c2 WHERE c2.client_id = :clientId)")
    Account findAccountWithMaxBalanceAndClient(long clientId);*/
}
