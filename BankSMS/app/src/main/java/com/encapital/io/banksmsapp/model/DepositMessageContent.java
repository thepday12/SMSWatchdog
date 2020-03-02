package com.encapital.io.banksmsapp.model;

import android.util.Log;

public class DepositMessageContent {
    private String bankReceivedTime;
    private String incurred;
    private String messageSyntax;
    private boolean isDeposit;

    public DepositMessageContent(String smsContent) {
        String[] splitSmsContent = smsContent.split("\n");
        for (String smsLine : splitSmsContent)
            if (smsLine.matches("^Sacombank\\s(\\d{1,2}\\/\\d{1,2}\\/\\d{4})\\s([0-1][0-9]|[2][0-3]):([0-5][0-9])")) {
                this.bankReceivedTime = smsLine.replace("Sacombank", "");
            } else if (smsLine.matches("PS:\\s(\\+|\\-)([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+)\\sVND")) {
                this.incurred = smsLine.replace("PS:", "").replace("VND", "");
            } else if (smsLine.matches(".*(?:[Ee][Nn][Tt][Rr][Aa][Dd][Ee])\\s?(\\d+).*")) {
                this.messageSyntax = smsLine;
            }
        if (this.incurred != null) {
            this.isDeposit = this.incurred.trim().startsWith("+");
        }

    }

    public String getBankReceivedTime() {
        return bankReceivedTime;
    }

    public void setBankReceivedTime(String bankReceivedTime) {
        this.bankReceivedTime = bankReceivedTime;
    }

    public String getIncurred() {
        return incurred;
    }

    public void setIncurred(String incurred) {
        this.incurred = incurred;
    }

    public String getMessageSyntax() {
        return messageSyntax;
    }

    public void setMessageSyntax(String messageSyntax) {
        this.messageSyntax = messageSyntax;
    }

    public boolean isDeposit() {
        return isDeposit;
    }

    public void setDeposit(boolean deposit) {
        isDeposit = deposit;
    }
}
