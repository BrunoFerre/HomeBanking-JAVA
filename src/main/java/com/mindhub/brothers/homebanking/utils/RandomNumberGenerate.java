package com.mindhub.brothers.homebanking.utils;

public class RandomNumberGenerate {

    public static String cardNumber() {
        return getRandomNumber(1000, 10000) + "-"
                + getRandomNumber(1000, 10000) + "-" +
                getRandomNumber(1000, 10000) + "-" +
                getRandomNumber(1000, 10000);
    }
    public static String accountNumber() {
        return getRandomNumber(1000, 10000) + "-"+
        getRandomNumber(1000, 10000);
    }

    public static int getCardCVV() {
        int cvv = getRandomNumber(100, 1000);
        return cvv;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
