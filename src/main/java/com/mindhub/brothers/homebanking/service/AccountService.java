package com.mindhub.brothers.homebanking.service;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();
    List<AccountDTO> getAcccount(Authentication authentication);
    Account accountId(long id);
    AccountDTO getAccountId(long id);
    Account findByNumber(String number);
    void save(Account account);
    void deleteAccount(Account account);
    boolean existsByNumber(String number);
    List<Account> findByClientAndStatusIsTrue(Client client);

}
