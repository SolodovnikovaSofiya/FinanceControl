package com.example.financecontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {
    private EditText etName, etBalance;
    private Spinner spCurrency;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        etName = findViewById(R.id.et_name);
        etBalance = findViewById(R.id.et_balance);
        spCurrency = findViewById(R.id.sp_currency);
        btnSave = findViewById(R.id.btn_save);
        dbHelper = new DatabaseHelper(this);

        accountId = getIntent().getIntExtra("account_id", -1);
        if (accountId != -1) {
            Account account = dbHelper.getAccountById(accountId);
            if (account != null) {
                etName.setText(account.getName());
                etBalance.setText(String.valueOf(account.getBalance()));
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccount();
            }
        });
    }

    private void saveAccount() {
        String name = etName.getText().toString();
        double balance = Double.parseDouble(etBalance.getText().toString());
        String currency = spCurrency.getSelectedItem().toString();

        if (accountId != -1) {
            Account account = dbHelper.getAccountById(accountId);
            if (account != null) {
                account.setName(name);
                account.setBalance(balance);
                account.setCurrency(currency);
                dbHelper.updateAccount(account);
            }
        } else {
            Account account = new Account(name, balance, currency);
            dbHelper.addAccount(account);
        }

        finish();
    }
}