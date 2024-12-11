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

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private List<Account> accounts;
    private Context context;

    public AccountAdapter(List<Account> accounts, Context context) {
        this.accounts = accounts;
        this.context = context;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account account = accounts.get(position);
        holder.tvName.setText(account.getName());
        holder.tvBalance.setText(String.valueOf(account.getBalance()));
        holder.tvCurrency.setText(account.getCurrency());

        // Используем holder.getAdapterPosition() для получения текущей позиции
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountActivity.class);
                intent.putExtra("account_id", account.getId());
                context.startActivity(intent);
            }
        });

        holder.btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition(); // Получаем текущую позицию
                if (currentPosition != RecyclerView.NO_POSITION) {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    dbHelper.deleteAccount(accounts.get(currentPosition).getId());
                    accounts.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    // Метод для обновления данных в адаптере
    public void updateData(List<Account> newAccounts) {
        accounts.clear();
        accounts.addAll(newAccounts);
        notifyDataSetChanged();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBalance, tvCurrency;
        Button btnDeleteAccount;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvBalance = itemView.findViewById(R.id.tv_balance);
            tvCurrency = itemView.findViewById(R.id.tv_currency);
            btnDeleteAccount = itemView.findViewById(R.id.btn_delete_account);
        }
    }
}