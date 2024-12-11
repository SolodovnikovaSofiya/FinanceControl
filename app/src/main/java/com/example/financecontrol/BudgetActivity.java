package com.example.financecontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class BudgetActivity extends AppCompatActivity {

    private EditText etAmount;
    private Spinner spCategory;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int budgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        etAmount = findViewById(R.id.et_amount);
        spCategory = findViewById(R.id.sp_category);
        btnSave = findViewById(R.id.btn_save);
        dbHelper = new DatabaseHelper(this);

        budgetId = getIntent().getIntExtra("budget_id", -1);
        if (budgetId != -1) {
            Budget budget = dbHelper.getBudgetById(budgetId);
            if (budget != null) {
                etAmount.setText(String.valueOf(budget.getAmount()));
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBudget();
            }
        });
    }

    private void saveBudget() {
        double amount = Double.parseDouble(etAmount.getText().toString());
        String category = spCategory.getSelectedItem().toString();

        if (budgetId != -1) {
            Budget budget = dbHelper.getBudgetById(budgetId);
            if (budget != null) {
                budget.setAmount(amount);
                budget.setCategory(category);
                dbHelper.updateBudget(budget);
            }
        } else {
            Budget budget = new Budget(amount, category);
            dbHelper.addBudget(budget);
        }

        finish();
    }
}