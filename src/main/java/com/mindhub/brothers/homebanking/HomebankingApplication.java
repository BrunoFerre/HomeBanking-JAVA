package com.mindhub.brothers.homebanking;

import com.mindhub.brothers.homebanking.models.Account;
import com.mindhub.brothers.homebanking.models.Client;
import com.mindhub.brothers.homebanking.repositories.AccountsRepository;
import com.mindhub.brothers.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {
 LocalDate date = LocalDate.now();
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository  repositoryClient, AccountsRepository accountsRepository) {
		return (args) -> {
			Account account = new Account("VIN001",this.date,5000);
			Account account1 = new Account("VIN002",this.date,7500);
			Client client = new Client("Melba", "Morel","melba@melba");
			client.addAccount(account);
			client.addAccount(account1);
			repositoryClient.save(client);
			accountsRepository.save(account);
			accountsRepository.save(account1);
		};
	}
}

