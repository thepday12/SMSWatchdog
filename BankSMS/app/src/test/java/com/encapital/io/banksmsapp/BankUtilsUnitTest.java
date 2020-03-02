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
        assertTrue(BankUtils.isSacombank("Sacombank", "Sacombank"));
        assertFalse(BankUtils.isSacombank("Sacombank", "+84396111026"));
        assertFalse(BankUtils.isSacombank("+84396111026", "Sacombank"));
        assertFalse(BankUtils.isSacombank("+84396111026", "84396111026"));
        assertFalse(BankUtils.isSacombank("", ""));
    }
}