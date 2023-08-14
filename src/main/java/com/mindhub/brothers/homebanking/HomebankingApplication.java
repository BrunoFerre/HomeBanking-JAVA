package com.mindhub.brothers.homebanking;

import com.mindhub.brothers.homebanking.models.*;
import com.mindhub.brothers.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootApplication
public class HomebankingApplication {
 private LocalDate date = LocalDate.now();
 private LocalDateTime date1 = LocalDateTime.now().withNano(0);
 private LocalDateTime date2 = LocalDateTime.now().withNano(0).plusDays(1);
 private final List<Integer> hipotecario= List.of(12,24,36,48,60);
 private final List<Integer> personal= List.of(6,12,24);
 private final List<Integer> automotriz= List.of(6,12,24,36);
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository  repositoryClient,
									  AccountsRepository accountsRepository,
									  RepositoryTransaction repositoryTransaction,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository) {
		return (args) -> {
		/*	Account account = new Account("VIN001",this.date,5000);
			Account account1 = new Account("VIN002",this.date,7500);
			Transaction transaction = new Transaction(TransactionType.CREDIT,1700,"DESCRIPTION ",this.date1);
			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -1500, "DESCRIPTION2", this.date2);
			accountsRepository.save(account);
			accountsRepository.save(account1);
			account1.addTransaction(transaction);
			account1.addTransaction(transaction1);
			repositoryTransaction.save(transaction);
			repositoryTransaction.save(transaction1);*/

			Client client = new Client("Melba", "Morel","melba@melba");
			Loan loan1 = new Loan("Hipotecario", 400.000,this.hipotecario);
			Loan loan2 = new Loan("Personal", 100.000,this.personal);
			Loan loan3 = new Loan("Automotriz", 300.000,this.automotriz);
			ClientLoan clientLoan = new ClientLoan(60,400.000);
			repositoryClient.save(client);
			loanRepository.save(loan2);
			loanRepository.save(loan1);
			loanRepository.save(loan3);
			client.addClientLoan(clientLoan);
			loan1.addClientLoan(clientLoan);
			clientLoanRepository.save(clientLoan);

		};
	}
}

