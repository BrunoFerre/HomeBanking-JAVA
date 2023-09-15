package com.mindhub.brothers.homebanking.service.implement;

import com.mindhub.brothers.homebanking.dtos.AccountDTO;
import com.mindhub.brothers.homebanking.dtos.ClientDTO;
import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountsRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @Override
    public List<AccountDTO> getAcccount(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getAccounts().stream().collect(toList());
    }

    @Override
    public Account accountId(long id) {
        return accountRepository.findById(id);
    }

    @Override
    public AccountDTO getAccountId(long id) {
        return new AccountDTO(accountRepository.findById(id));
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

  /*  @Override
    public boolean existsByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }*/
}
