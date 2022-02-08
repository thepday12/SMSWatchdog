package com.encapital.io.dnsebanksmsnoti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;


public class BankNotification {

    final static Map<String, String> CHANNEL_MAP = new HashMap<String, String>() {{
        put("BIDV", "accounting-bidv-sms");
        put("Vietcombank", "accounting-vcb-sms");
        put("Public Bank", "accounting-pb-sms");
        put("VIB", "accounting-vib-sms");
        put("MSB", "accounting-msb-sms");
        put("VietinBank", "accounting-vietinbank-sms");
        put("TPBank", "accounting-tpbank-sms");
        put("VietABank", "accounting-vietabank-sms");
        put("MBBANK", "accounting-mb-sms");
        put("Sacombank", "accounting-sacombank-sms");
        put("VPBank", "accounting-vpbank-sms");
    }};

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("channel")
    @Expose
    private String channel;


    public BankNotification() {
    }

    public BankNotification(BankMessage bankMessage) {
        text = "Thời gian nhận tin nhắn: " + bankMessage.getReceivedTime() + "\nNội dung tin nhắn: " + bankMessage.getContent();
        channel = CHANNEL_MAP.get(bankMessage.getName());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
