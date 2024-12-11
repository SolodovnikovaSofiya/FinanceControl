package com.example.financecontrol;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<Budget> budgets;
    private Context context;

    public BudgetAdapter(List<Budget> budgets, Context context) {
        this.budgets = budgets;
        this.context = context;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgets.get(position);
        holder.tvAmount.setText(String.valueOf(budget.getAmount()));
        holder.tvCategory.setText(budget.getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BudgetActivity.class);
                intent.putExtra("budget_id", budget.getId());
                context.startActivity(intent);
            }
        });

        holder.btnDeleteBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    dbHelper.deleteBudget(budgets.get(currentPosition).getId());
                    budgets.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }

    // Метод для обновления данных в адаптере
    public void updateData(List<Budget> newBudgets) {
        budgets.clear();
        budgets.addAll(newBudgets);
        notifyDataSetChanged();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvCategory;
        Button btnDeleteBudget;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            btnDeleteBudget = itemView.findViewById(R.id.btn_delete_budget);
        }
    }
}