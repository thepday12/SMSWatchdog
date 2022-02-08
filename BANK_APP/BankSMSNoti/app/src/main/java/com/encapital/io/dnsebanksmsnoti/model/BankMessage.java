package com.encapital.io.dnsebanksmsnoti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.formatDateTime;
import static com.encapital.io.dnsebanksmsnoti.utils.CommonUtils.getCurrentTimeMilliseconds;


public class BankMessage {

    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("content")
    @Expose
    private String content;

    private String receivedTime;

    public BankMessage() {
    }

    public BankMessage(String from, String name, String content,long receivedTime) {
        this.from = from;
        this.name = name;
        this.content = content;
        this.receivedTime = formatDateTime(receivedTime,"yyyy-MM-dd hh:mm:ss");
    }

    public BankMessage(String from, String name, String content) {
        this.from = from;
        this.name = name;
        this.content = content;
        this.receivedTime = formatDateTime(getCurrentTimeMilliseconds(),"yyyy-MM-dd hh:mm:ss");
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }


}