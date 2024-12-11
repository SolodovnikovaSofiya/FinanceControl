package com.example.financecontrol;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private String id;
    private String amount;
    private String description;
    private String category;
    private String type;
    private String walletId;
    private String piggyBankId;
    private String date;

    // Private constructor
    private Transaction(Builder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.description = builder.description;
        this.category = builder.category;
        this.type = builder.type;
        this.walletId = builder.walletId;
        this.piggyBankId = builder.piggyBankId;
        this.date = builder.date;
    }

    // Builder class
    public static class Builder {
        private String id;
        private String amount;
        private String description;
        private String category;
        private String type;
        private String walletId;
        private String piggyBankId;
        private String date;

        public Builder(String id, String amount, String description, String category, String type) {
            this.id = id;
            this.amount = amount;
            this.description = description;
            this.category = category;
            this.type = type;
            this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // Устанавливаем текущую дату
        }

        public Builder walletId(String walletId) {
            this.walletId = walletId;
            return this;
        }

        public Builder piggyBankId(String piggyBankId) {
            this.piggyBankId = piggyBankId;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

    // Конструктор с четырьмя аргументами
    public Transaction(String amount, String description, String category, String type) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.type = type;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); // Устанавливаем текущую дату
        this.walletId = "default_wallet_id"; // Устанавливаем дефолтное значение
        this.piggyBankId = "default_piggy_bank_id"; // Устанавливаем дефолтное значение
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getPiggyBankId() {
        return piggyBankId;
    }

    public void setPiggyBankId(String piggyBankId) {
        this.piggyBankId = piggyBankId;
    }
}