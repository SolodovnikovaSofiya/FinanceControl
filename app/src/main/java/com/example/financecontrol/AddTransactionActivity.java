package com.example.financecontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etDescription;
    private Spinner spCategory, spType;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        etAmount = findViewById(R.id.et_amount);
        etDescription = findViewById(R.id.et_description);
        spCategory = findViewById(R.id.sp_category);
        spType = findViewById(R.id.sp_type);
        btnSave = findViewById(R.id.btn_save);

        // Настройка адаптера для Spinner spType
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(typeAdapter);

        // Настройка адаптера для Spinner spCategory
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        // Обработка изменения выбора в Spinner spType
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                if (selectedType.equals("Income")) {
                    spCategory.setVisibility(View.GONE);
                } else {
                    spCategory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ничего не делать
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTransaction();
            }
        });
    }

    private void saveTransaction() {
        String amount = etAmount.getText().toString();
        String description = etDescription.getText().toString();
        String category = spCategory.getVisibility() == View.VISIBLE ? spCategory.getSelectedItem().toString() : "Income";
        String type = spType.getSelectedItem().toString();

        Transaction transaction = new Transaction(amount, description, category, type);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Проверяем бюджет по категории
        if (type.equals("Expense")) {
            double totalSpent = dbHelper.getTotalSpentByCategory(category);
            Budget budget = dbHelper.getBudgetByCategory(category);

            if (budget != null) {
                double budgetAmount = budget.getAmount();
                double newTotalSpent = totalSpent + Double.parseDouble(amount);

                if (newTotalSpent > budgetAmount) {
                    double overspend = newTotalSpent - budgetAmount;
                    Toast.makeText(this, "You have exceeded the budget by " + overspend + " money!", Toast.LENGTH_LONG).show();
                }
            }
        }
        dbHelper.addTransaction(transaction);

        finish();
    }
}