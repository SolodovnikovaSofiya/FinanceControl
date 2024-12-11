package com.example.financecontrol;

public class Budget {
    private int id;
    private double amount;
    private String category;

    public Budget(int id, double amount, String category) {
        this.id = id;
        this.amount = amount;
        this.category = category;
    }

    public Budget(double amount, String category) {
        this.amount = amount;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}