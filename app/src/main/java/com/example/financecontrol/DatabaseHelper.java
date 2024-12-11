package com.example.financecontrol;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance_control.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблиц
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT)");
        db.execSQL("CREATE TABLE transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, description TEXT, category TEXT, type TEXT)");
        db.execSQL("CREATE TABLE budgets (id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, category TEXT)");
        db.execSQL("CREATE TABLE accounts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, balance REAL, currency TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS budgets");
        db.execSQL("DROP TABLE IF EXISTS accounts");
        onCreate(db);
    }
    // Методы для работы с пользователями
    public void addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        db.insert("users", null, values);
        db.close();
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"id"}, "username=? AND password=?", new String[]{username, password}, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"id"}, "username=?", new String[]{username}, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    // Методы для работы с транзакциями
    public void addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", transaction.getAmount());
        values.put("description", transaction.getDescription());
        values.put("category", transaction.getCategory());
        values.put("type", transaction.getType());
        db.insert("transactions", null, values);
        db.close();
    }

    public Transaction getTransactionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("transactions", new String[]{"id", "amount", "description", "category", "type"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String transactionId = String.valueOf(cursor.getInt(0));
            String amount = String.valueOf(cursor.getDouble(1));
            String description = cursor.getString(2);
            String category = cursor.getString(3);
            String type = cursor.getString(4);

            Transaction transaction = new Transaction.Builder(transactionId, amount, description, category, type)
                    .walletId("default_wallet_id")
                    .piggyBankId("default_piggy_bank_id")
                    .build();

            cursor.close();
            db.close();
            return transaction;
        } else {
            db.close();
            return null;
        }
    }
    public double getTotalSpentByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(amount) AS total FROM transactions WHERE category = ?";
        Cursor cursor = db.rawQuery(query, new String[]{category});

        double totalSpent = 0;
        if (cursor.moveToFirst()) {
            int totalIndex = cursor.getColumnIndex("total");
            if (totalIndex != -1) { // Проверяем, что столбец существует
                totalSpent = cursor.getDouble(totalIndex);
            }
        }

        cursor.close();
        db.close();
        return totalSpent;
    }

    public Budget getBudgetByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM budgets WHERE category = ?";
        Cursor cursor = db.rawQuery(query, new String[]{category});

        Budget budget = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int amountIndex = cursor.getColumnIndex("amount");
            int categoryIndex = cursor.getColumnIndex("category");

            if (idIndex != -1 && amountIndex != -1 && categoryIndex != -1) { // Проверяем, что все столбцы существуют
                int id = cursor.getInt(idIndex);
                double amount = cursor.getDouble(amountIndex);
                String budgetCategory = cursor.getString(categoryIndex);
                budget = new Budget(id, amount, budgetCategory);
            }
        }

        cursor.close();
        db.close();
        return budget;
    }
    public void updateTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", transaction.getAmount());
        values.put("description", transaction.getDescription());
        values.put("category", transaction.getCategory());
        values.put("type", transaction.getType());
        db.update("transactions", values, "id=?", new String[]{String.valueOf(transaction.getId())});
        db.close();
    }

    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("transactions", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Методы для работы с бюджетами
    public void addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", budget.getAmount());
        values.put("category", budget.getCategory());
        db.insert("budgets", null, values);
        db.close();
    }

    public Budget getBudgetById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("budgets", new String[]{"id", "amount", "category"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int budgetId = cursor.getInt(0);
            double amount = cursor.getDouble(1);
            String category = cursor.getString(2);
            cursor.close();
            db.close();
            return new Budget(budgetId, amount, category);
        } else {
            db.close();
            return null;
        }
    }

    public void updateBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", budget.getAmount());
        values.put("category", budget.getCategory());
        db.update("budgets", values, "id=?", new String[]{String.valueOf(budget.getId())});
        db.close();
    }
    public List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("budgets", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int amountIndex = cursor.getColumnIndex("amount");
                int categoryIndex = cursor.getColumnIndex("category");

                if (idIndex != -1 && amountIndex != -1 && categoryIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    double amount = cursor.getDouble(amountIndex);
                    String category = cursor.getString(categoryIndex);
                    budgets.add(new Budget(id, amount, category));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return budgets;
    }
    public void deleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("budgets", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Методы для работы со счетами
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("balance", account.getBalance());
        values.put("currency", account.getCurrency());
        db.insert("accounts", null, values);
        db.close();
    }

    public Account getAccountById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("accounts", new String[]{"id", "name", "balance", "currency"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int accountId = cursor.getInt(0);
            String name = cursor.getString(1);
            double balance = cursor.getDouble(2);
            String currency = cursor.getString(3);
            cursor.close();
            db.close();
            return new Account(accountId, name, balance, currency);
        } else {
            db.close();
            return null;
        }
    }
    public void deleteAccount(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("accounts", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("accounts", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("name");
                int balanceIndex = cursor.getColumnIndex("balance");
                int currencyIndex = cursor.getColumnIndex("currency");

                if (idIndex != -1 && nameIndex != -1 && balanceIndex != -1 && currencyIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    double balance = cursor.getDouble(balanceIndex);
                    String currency = cursor.getString(currencyIndex);
                    accounts.add(new Account(id, name, balance, currency));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accounts;
    }
    public void updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("balance", account.getBalance());
        values.put("currency", account.getCurrency());
        db.update("accounts", values, "id=?", new String[]{String.valueOf(account.getId())});
        db.close();
    }

    public List<PieEntry> getReportData() {
        List<PieEntry> entries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT category, SUM(amount) AS total FROM transactions GROUP BY category";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int totalIndex = cursor.getColumnIndex("total");
                int categoryIndex = cursor.getColumnIndex("category");

                if (totalIndex != -1 && categoryIndex != -1) {
                    float total = cursor.getFloat(totalIndex);
                    String category = cursor.getString(categoryIndex);
                    entries.add(new PieEntry(total, category));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return entries;
    }
}