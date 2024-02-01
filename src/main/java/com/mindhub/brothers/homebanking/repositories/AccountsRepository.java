package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account,Long> {
    Account findByNumber(String number);
    Account findById(long id);
    boolean existsByNumber(String number);
    List<Account> findByClientAndStatusIsTrue(Client client);

    List<Account> findAllByClientAndStatusTrue(Client client);

}
