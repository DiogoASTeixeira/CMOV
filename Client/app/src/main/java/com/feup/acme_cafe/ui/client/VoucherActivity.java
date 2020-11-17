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
import com.feup.acme_cafe.data.model.Voucher;

import java.text.DecimalFormat;
import java.util.List;

public class VoucherActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "";
    User user;
    private String[] ids;
    ListView vouchersListView;
    Util.VoucherAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        user = (User) getIntent().getSerializableExtra("user");

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayShowHomeEnabled(true);
        }

        ids = new String[user.getTransactions().size()+1];
        for(int i = 0; i < user.getTransactions().size() ;i++){
            ids[i] = user.getTransactions().get(i).getId();
        }

        vouchersListView = findViewById(R.id.vouchers_list);
        adapter = new Util.VoucherAdapter(this, R.layout.row_voucher, user.getVouchers(), user);
        vouchersListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
