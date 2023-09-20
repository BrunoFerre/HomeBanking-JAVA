package com.mindhub.brothers.homebanking.utils;

import com.mindhub.brothers.homebanking.dtos.LoanAplicationDTO;

public class InterestRate {
    public static double totalAmount(LoanAplicationDTO loanApp){
        int[] interest = {10,20,45,65,70};
        int interestIndex = 0;
        int payments = loanApp.getPayments();
        double amount = loanApp.getAmount();

        if (payments == 6) {
            interestIndex = 0;
        } else if (payments == 12) {
            interestIndex = 1;
        } else if (payments== 24) {
            interestIndex = 2;
        } else if (payments == 36) {
            interestIndex = 3;
        } else if (payments == 48) {
            interestIndex = 4;
        } else {
            throw new IllegalArgumentException("This payments is not available.");
        }
        double interestRate = (double) interest[interestIndex] / 100;

        double totalAmountWithInterest = amount * (1 + interestRate);

        return totalAmountWithInterest;
    }
}
