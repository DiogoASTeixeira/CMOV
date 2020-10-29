package com.feup.acme_cafe.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

public class TransactionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "";
    TransactionActivity.TransactionAdapter adapter;
    private RequestQueue queue;
    User user;
    private String ids[];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        user = (User) getIntent().getSerializableExtra("user");

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            //bar.setIcon(R.drawable.medium_logo2);
            bar.setDisplayShowHomeEnabled(true);
        }

        ids = new String[user.getTransactions().size()+1];
        for(int i = 0; i < user.getTransactions().size() ;i++){
            ids[i] = user.getTransactions().get(i).getId();
        }

        adapter = new TransactionActivity.TransactionAdapter(this, R.layout.rowdate, user.getTransactions());

        ListView transactionList = findViewById(R.id.listview);
        transactionList.setAdapter(adapter);

        transactionList.setOnItemClickListener((parent, view, position, id) -> {
            openDetails(ids[position]);
        });

        queue = Volley.newRequestQueue(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void openDetails(String id) {
        Intent i = new Intent(getApplicationContext(), DetailsTransaction.class);
        i.putExtra("TransactionId", id);
        startActivity(i);
    }

    class TransactionAdapter extends ArrayAdapter<Transaction> {
        private int layoutResource;
        private Context mContext;

        TransactionAdapter(@NonNull Context context, int resource, @NonNull List<Transaction> objects) {
            super(context, resource, objects);
            layoutResource = resource;
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
                TextView date = line.findViewById(R.id.prod_name);
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
                    price.setText(p.getTotal_value() + "€");
                }

                /*if (id != null) {
                    Log.d("id", p.getId());
                    id.setText(p.getId());
                }*/
            }

            return line;
        }
    }
}
