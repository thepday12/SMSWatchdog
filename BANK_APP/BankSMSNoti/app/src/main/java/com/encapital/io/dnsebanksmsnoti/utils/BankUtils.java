package com.encapital.io.dnsebanksmsnoti.utils;

public class BankUtils {
    final static String[] PHONE_NUMBER_BANKS = {
            "BIDV",
            "Vietcombank",
            "Public Bank",
            "VIB",
            "MSB",
            "VietinBank",
            "TPBank",
            "VietABank",
            "MBBANK",
            "Sacombank",
            "VPBank",
    };

    public static boolean isSendFromBank(String phoneNumber) {
        for (String bankPhoneNumber : PHONE_NUMBER_BANKS
        ) {
            if (phoneNumber.equals(bankPhoneNumber)) {
                return true;
            }
        }
        return false;
    }


}
