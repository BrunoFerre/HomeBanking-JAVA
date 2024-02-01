package com.mindhub.brothers.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double maxAmount;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> payments;
    private Double interest;
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoan = new HashSet<>();
    public Loan() {
    }
    public Loan(String name, double maxAmount, List<Integer> payments, Double interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interest = interest;
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        this.clientLoan.add(clientLoan);
    }
    public List<String> getClients(){
        return clientLoan.stream().map(clientLoan->clientLoan.getClient().getFirstName()+" "+ clientLoan.getClient().getLastName()).collect(Collectors.toList());
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
    public Set<ClientLoan> getClientLoan() {
        return clientLoan;
    }
    public void setClientLoan(Set<ClientLoan> clientLoan) {
        this.clientLoan = clientLoan;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }
}