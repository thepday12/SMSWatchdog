package com.encapital.io.banksmsapp.utils;

public class BankUtils {
    final static String[] PHONE_NUMBER_BANKS = {
            "+84913227799", //tester
            "Sacombank"
    };

    public static boolean isSendFromBank(String phoneNumber) {
        for (String bankPhoneNumber : PHONE_NUMBER_BANKS
        ) {
            if(phoneNumber.equals(bankPhoneNumber)){
                return true;
            }
        }
        return false;
    }


}
