package com.mindhub.brothers.homebanking;

import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.models.Transaction;
import com.mindhub.brothers.homebanking.models.TransactionType;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import com.mindhub.brothers.homebanking.repositories.RepositoryTransaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootApplication
public class HomebankingApplication {
 LocalDate date = LocalDate.now();
 private LocalDateTime date1 = LocalDateTime.now().withNano(0);
 private LocalDateTime date2 = LocalDateTime.now().withNano(0).plusDays(1);
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository  repositoryClient, AccountsRepository accountsRepository, RepositoryTransaction repositoryTransaction) {
		return (args) -> {
			Client client = new Client("Melba", "Morel","melba@melba");
			Account account = new Account("VIN001",this.date,5000);
			Account account1 = new Account("VIN002",this.date,7500);
			Transaction transaction = new Transaction(TransactionType.CREDIT,1700,"DESCRIPTION ",this.date1);
			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -1500, "DESCRIPTION2", this.date2);
			client.addAccount(account);
			client.addAccount(account1);
			repositoryClient.save(client);
			accountsRepository.save(account);
			accountsRepository.save(account1);
			account1.addTransaction(transaction);
			account1.addTransaction(transaction1);
			repositoryTransaction.save(transaction);
			repositoryTransaction.save(transaction1);
		};
	}
}

