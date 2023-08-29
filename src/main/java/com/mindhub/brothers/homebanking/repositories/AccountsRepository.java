package com.mindhub.brothers.homebanking.repositories;

import com.mindhub.brothers.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountsRepository extends JpaRepository<Account,Long> {
    Account findByNumber(String number);
    Account findById(long id);
}
