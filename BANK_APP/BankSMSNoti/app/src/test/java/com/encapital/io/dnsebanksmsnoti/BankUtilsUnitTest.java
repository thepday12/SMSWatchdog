package com.encapital.io.dnsebanksmsnoti;

import com.encapital.io.dnsebanksmsnoti.utils.BankUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BankUtilsUnitTest {
    @Test
    public void validateBank() {
        assertTrue(BankUtils.isSendFromBank("BIDV"));
        assertTrue(BankUtils.isSendFromBank("Vietcombank"));
        assertTrue(BankUtils.isSendFromBank("MSB"));
        assertTrue(BankUtils.isSendFromBank("TPBank"));
        assertTrue(BankUtils.isSendFromBank("VietinBank"));
        assertTrue(BankUtils.isSendFromBank("VietABank"));
        assertTrue(BankUtils.isSendFromBank("MBBANK"));
        assertTrue(BankUtils.isSendFromBank("Sacombank"));
        assertTrue(BankUtils.isSendFromBank("VPBank"));

        assertFalse(BankUtils.isSendFromBank("FailBank"));
        assertFalse(BankUtils.isSendFromBank("+84913227799"));
        assertFalse(BankUtils.isSendFromBank(""));
    }
}