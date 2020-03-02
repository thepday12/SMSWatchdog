package com.encapital.io.banksmsapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

import static com.encapital.io.banksmsapp.utils.CommonUtils.getCurrentDate;

@Entity(tableName = "deposit-message")
public class DepositEntity {
    public static final String PENDING_STATUS = "PENDING";
    public static final String FAIL_STATUS = "FAIL";
    public static final String SUCCESS_STATUS = "SUCCESS";

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "from")
    private String from;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "date")
    private String date;


    public DepositEntity() {
    }

    public DepositEntity(DepositMessage depositMessage) {
        this.from = depositMessage.getFrom();
        this.name = depositMessage.getName();
        this.content = depositMessage.getContent();
        this.status = PENDING_STATUS;
        this.date = getCurrentDate("yyyy-mm-dd hh:mm:ss");
    }

    @Ignore
    public DepositEntity(String from, String name, String content) {
        this.from = from;
        this.name = name;
        this.content = content;
        this.status = PENDING_STATUS;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DepositMessage getDepositMessage() {
        return new DepositMessage(this.from,
                this.name,
                this.content);
    }
}
