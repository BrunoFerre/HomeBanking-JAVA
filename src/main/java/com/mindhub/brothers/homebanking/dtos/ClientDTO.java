package com.mindhub.brothers.homebanking.dtos;


import com.mindhub.brothers.homebanking.models.Client;
import java.util.Set;
import java.util.stream.Collectors;


public class ClientDTO {
    private  long id ;
    private String firstName;
    private String lastName;
    private String email;
    private Set<ClientLoanDTO> loans;

    private Set<AccountDTO> accounts;
    private Set<CardDTO> cards;

    public ClientDTO(){}
    public ClientDTO(Client client){
     this.id=client.getId();
     this.firstName= client.getFirstName();
     this.lastName=client.getLastName();
     this.email=client.getEmail();
     this.loans = client.getClientLoans()
             .stream()
             .map(ClientLoanDTO::new)
             .collect(Collectors.toSet());
     this.accounts= client.getAccounts()
             .stream()
             .map(AccountDTO::new)
             .collect(Collectors.toSet());

        this.cards= client.getCards()
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
