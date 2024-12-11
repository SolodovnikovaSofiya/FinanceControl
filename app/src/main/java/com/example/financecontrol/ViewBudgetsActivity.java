package com.example.financecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewBudgetsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BudgetAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budgets);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewBudgets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Budget> budgets = dbHelper.getAllBudgets();
        adapter = new BudgetAdapter(budgets, this);
        recyclerView.setAdapter(adapter);

        Button btnAddBudget = findViewById(R.id.btn_add_budget);
        btnAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBudgetsActivity.this, BudgetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем данные в адаптере
        List<Budget> budgets = dbHelper.getAllBudgets();
        adapter.updateData(budgets);
    }
}