package com.example.financecontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class EditTransactionActivity extends AppCompatActivity {

    private EditText etAmount, etDescription;
    private Spinner spCategory, spType;
    private Button btnSave, btnDelete;
    private int transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        etAmount = findViewById(R.id.et_amount);
        etDescription = findViewById(R.id.et_description);
        spCategory = findViewById(R.id.sp_category);
        spType = findViewById(R.id.sp_type);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        transactionId = getIntent().getIntExtra("transaction_id", -1);
        if (transactionId != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Transaction transaction = dbHelper.getTransactionById(transactionId);
            etAmount.setText(String.valueOf(transaction.getAmount()));
            etDescription.setText(transaction.getDescription());
            spCategory.setSelection(getIndex(spCategory, transaction.getCategory()));
            spType.setSelection(getIndex(spType, transaction.getType()));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTransaction();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransaction();
            }
        });
    }

    private void updateTransaction() {
        double amount = Double.parseDouble(etAmount.getText().toString());
        String description = etDescription.getText().toString();
        String category = spCategory.getSelectedItem().toString();
        String type = spType.getSelectedItem().toString();

        // Преобразуем amount в String
        String amountStr = String.valueOf(amount);

        // Используем Builder для создания объекта Transaction
        Transaction transaction = new Transaction.Builder(String.valueOf(transactionId), amountStr, description, category, type)
                .build();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.updateTransaction(transaction);

        finish();
    }

    private void deleteTransaction() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.deleteTransaction(transactionId);
        finish();
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }
}