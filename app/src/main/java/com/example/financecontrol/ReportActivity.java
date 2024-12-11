package com.example.financecontrol;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        pieChart = findViewById(R.id.pie_chart);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<PieEntry> entries = dbHelper.getReportData();

        // Получаем массив цветов из ресурсов
        TypedArray typedArray = getResources().obtainTypedArray(R.array.category_colors);
        List<Integer> colors = new ArrayList<>();

        // Связываем цвета с категориями
        for (PieEntry entry : entries) {
            String category = entry.getLabel();
            int colorIndex = getCategoryIndex(category);
            colors.add(typedArray.getColor(colorIndex, 0));
        }
        typedArray.recycle();

        PieDataSet dataSet = new PieDataSet(entries, "Transaction report");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(16f);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);

        // Настраиваем легенду
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(20f);
        legend.setWordWrapEnabled(true);

        pieChart.invalidate();
    }

    // Метод для получения индекса категории
    private int getCategoryIndex(String category) {
        switch (category) {
            case "Food":
                return 0;
            case "Transport":
                return 1;
            case "Entertainment":
                return 2;
            case "Utilities":
                return 3;
            case "Health":
                return 4;
            case "Education":
                return 5;
            case "Shopping":
                return 6;
            case "Travel":
                return 7;
            case "Gifts":
                return 8;
            case "Other":
                return 9;
            default:
                return 0; // По умолчанию возвращаем первый цвет
        }
    }
}