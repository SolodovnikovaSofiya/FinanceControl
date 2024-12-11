package com.example.financecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewAccountsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AccountAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewAccounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Account> accounts = dbHelper.getAllAccounts();
        adapter = new AccountAdapter(accounts, this);
        recyclerView.setAdapter(adapter);

        Button btnAddAccount = findViewById(R.id.btn_add_account);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAccountsActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем данные в адаптере
        List<Account> accounts = dbHelper.getAllAccounts();
        adapter.updateData(accounts);
    }
}