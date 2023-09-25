package com.mindhub.brothers.homebanking;

import com.mindhub.brothers.homebanking.models.*;
import com.mindhub.brothers.homebanking.models.enums.AccountType;
import com.mindhub.brothers.homebanking.models.enums.CardColor;
import com.mindhub.brothers.homebanking.models.enums.CardType;
import com.mindhub.brothers.homebanking.models.enums.TransactionType;
import com.mindhub.brothers.homebanking.repositories.*;
import com.mindhub.brothers.homebanking.utils.RandomNumberGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {
    private LocalDate date = LocalDate.now();
    private LocalDate goldTime = LocalDate.now();
    private LocalDate titaniumTime = LocalDate.now();
    private LocalDateTime date1 = LocalDateTime.now().withNano(0);
    private LocalDateTime date2 = LocalDateTime.now().withNano(0).plusDays(1);
    private final List<Integer> hipotecario = List.of(12, 24, 36, 48, 60);
    private final List<Integer> personal = List.of(6, 12, 24);
    private final List<Integer> automotriz = List.of(6, 12, 24, 36);
 @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

 @Bean
    public CommandLineRunner initData(ClientRepository repositoryClient,
                                      AccountsRepository accountsRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository) {
     return (args) -> {

         Account account = new Account("VIN-001", this.date, 5000, AccountType.CURRENT, true);
         Account account1 = new Account("VIN-002", this.date, 7500, AccountType.SAVINGS, true);
         Transaction transaction = new Transaction(TransactionType.CREDIT,
                 1700, "DESCRIPTION ", this.date1, account.getBalance());
         Transaction transaction1 = new Transaction(TransactionType.DEBIT,
                 -1500, "DESCRIPTION2", this.date2, account1.getBalance());
         Client client = new Client("Melba",
                 "Morel",
                 "melba@melba.com", passwordEncoder.encode("pass123"));
         Loan loan1 = new Loan("Mortgage", 400.000, this.hipotecario, .5);
         Loan loan2 = new Loan("Personal", 100.000, this.personal, .5);
         Loan loan3 = new Loan("Automotive", 300.000, this.automotriz, .5);

         ClientLoan clientLoan = new ClientLoan(60, 400.000);
         ClientLoan clientLoan1 = new ClientLoan(12, 50.00);

         Card cardDebit = new Card(client.getLastName() + client.getFirstName(),
                 CardType.DEBIT, CardColor.GOLD, "1234-1234-1234-1234",
                 234, this.goldTime.plusYears(5), this.goldTime, true);

         Card cardCredit = new Card(client.getLastName() + " " + client.getFirstName(),
                 CardType.CREDIT, CardColor.TITANIUM,
                 "5656-5656-5656-5656",
                 567, this.titaniumTime.plusYears(5), this.titaniumTime.plusYears(1), true);

         client.addCard(cardDebit);
         client.addCard(cardCredit);
         client.addAccount(account);
         client.addAccount(account1);
         account1.addTransaction(transaction);
         account1.addTransaction(transaction1);
         client.addClientLoan(clientLoan);
         client.addClientLoan(clientLoan1);
         loan1.addClientLoan(clientLoan);
         loan2.addClientLoan(clientLoan1);


         repositoryClient.save(client);
         accountsRepository.save(account);
         accountsRepository.save(account1);
         transactionRepository.save(transaction);
         transactionRepository.save(transaction1);
         loanRepository.save(loan2);
         loanRepository.save(loan1);
         loanRepository.save(loan3);
         clientLoanRepository.save(clientLoan);
         clientLoanRepository.save(clientLoan1);
         cardRepository.save(cardCredit);
         cardRepository.save(cardDebit);

         Client bruno = new Client("Bruno Marcos",
                 "Ferreira",
                 "fbrunomarcos@gmail.com",
                 passwordEncoder.encode("pass123"));
         Account accountBruno1 = new Account("VIN-" + RandomNumberGenerate.accountNumber(),
                 this.date.plusDays(5), 15000, AccountType.CURRENT, true);
         Account accountBruno2 = new Account("VIN-" + RandomNumberGenerate.accountNumber(),
                 this.date.plusDays(6), 15000, AccountType.SAVINGS, true);
         Transaction transactionBruno = new Transaction(TransactionType.CREDIT, 2500,
                 "nose que poner je",
                 this.date1.plusDays(1), accountBruno1.getBalance());
         ClientLoan loanBruno = new ClientLoan(12, 50.000);

         Card cardBruno = new Card(bruno.getLastName() + bruno.getFirstName(),
                 CardType.CREDIT, CardColor.TITANIUM,
                 "9898-9898-9898-9898",
                 346,
                 this.date.plusMonths(1),
                 this.date.plusYears(5), true);
         Client admin = new Client("GG", "RR", "admin@admin.com", passwordEncoder.encode("1235"));
         repositoryClient.save(admin);
         bruno.addAccount(accountBruno1);
         bruno.addAccount(accountBruno2);
         bruno.addCard(cardBruno);
         accountBruno1.addTransaction(transactionBruno);
         bruno.addClientLoan(loanBruno);
         loan1.addClientLoan(loanBruno);
         repositoryClient.save(bruno);
         accountsRepository.save(accountBruno1);
         accountsRepository.save(accountBruno2);
         transactionRepository.save(transactionBruno);
         clientLoanRepository.save(loanBruno);
         cardRepository.save(cardBruno);
     };
 }
}

