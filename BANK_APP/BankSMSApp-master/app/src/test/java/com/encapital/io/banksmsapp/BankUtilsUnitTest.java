package com.encapital.io.banksmsapp;

import com.encapital.io.banksmsapp.utils.BankUtils;

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
        assertTrue(BankUtils.isSendFromBank("Sacombank", "Sacombank"));
        assertTrue(BankUtils.isSendFromBank("+84913227799", "Sacombank"));
        assertTrue(BankUtils.isSendFromBank("+84913227799", "12345"));
        assertTrue(BankUtils.isSendFromBank("Sacombank", "+84396111026"));
        assertFalse(BankUtils.isSendFromBank("+84396111026", "Sacombank"));
        assertFalse(BankUtils.isSendFromBank("+84396111026", "84396111026"));
        assertFalse(BankUtils.isSendFromBank("", ""));
    }
}