package com.feup.acme_cafe.ui.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.Transaction;
import com.feup.acme_cafe.data.model.User;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "";
    TransactionActivity.TransactionAdapter adapter;
    User user;
    private String[] ids;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        user = (User) getIntent().getSerializableExtra("user");

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayShowHomeEnabled(true);
        }

        ids = new String[user.getTransactions().size()+1];
        for(int i = 0; i < user.getTransactions().size() ;i++){
            ids[i] = user.getTransactions().get(i).getId();
        }

        adapter = new TransactionAdapter(this, user.getTransactions());

        ListView transactionList = findViewById(R.id.listview);
        transactionList.setAdapter(adapter);

        transactionList.setOnItemClickListener((parent, view, position, id) -> openDetails(ids[position]));

        RequestQueue queue = Volley.newRequestQueue(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void openDetails(String id) {
        Intent i = new Intent(getApplicationContext(), DetailsTransactionActivity.class);
        i.putExtra("TransactionId", id);
        System.out.println(id);
        startActivity(i);
    }

    static class TransactionAdapter extends ArrayAdapter<Transaction> {
        private final int layoutResource;
        private final Context mContext;

        TransactionAdapter(@NonNull Context context, @NonNull List<Transaction> objects) {
            super(context, R.layout.rowdate, objects);
            layoutResource = R.layout.rowdate;
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View line = convertView;

            if (line == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(mContext);
                line = vi.inflate(layoutResource, null);
            }

            Transaction p = getItem(position);

            if (p != null) {
                TextView date = line.findViewById(R.id.voucher_name);
                TextView hora = line.findViewById(R.id.date);
                TextView price = line.findViewById(R.id.totaltransaction);
                String[] data = Util.parseDate(p.getDate());

                if (date != null) {
                    date.setText(data[0]);
                }

                if (hora != null) {
                    hora.setText(data[1]);
                }

                if (price != null) {
                    price.setText(new DecimalFormat("#.00").format(p.getTotal_value()) + "€");
                }
            }

            return line;
        }
    }
}
