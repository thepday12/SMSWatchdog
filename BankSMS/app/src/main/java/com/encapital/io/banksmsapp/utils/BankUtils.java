package com.encapital.io.banksmsapp.utils;

public class BankUtils {
    public static boolean isSacombank(String phoneNumber, String branchName) {
        return branchName.equals("Sacombank") && phoneNumber.equals(branchName);
    }

    public static boolean isTester(String phoneNumber) {
        return phoneNumber.equals("+84913227799");
    }
}
