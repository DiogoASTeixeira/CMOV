package com.feup.acme_cafe.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.feup.acme_cafe.data.model.Transaction;
import com.feup.acme_cafe.data.model.User;
import com.feup.acme_cafe.R;

public class ProfileActivity extends AppCompatActivity {

    User user;
    Intent transaction_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra("user");
        transaction_intent = new Intent( this, TransactionActivity.class);
        transaction_intent.putExtra("user", user);

        TextView username=  findViewById(R.id.username);
        username.setText(user.getUsername());

        TextView name= findViewById(R.id.name);
        name.setText(user.getName());

        TextView balance= findViewById(R.id.spent);
        balance.setText(user.getTotal_spent().toString()+" €");

        TextView email = findViewById(R.id.email_prof);
        email.setText(user.getEmail());

        TextView vouchers=  findViewById(R.id.vouchers);
        vouchers.setText(user.getVouchers().size() + "");

        TextView previous_trans = findViewById(R.id.previous_trans);
        previous_trans.setOnClickListener((v)->previous(transaction_intent));

    }

    private void previous(Intent transaction_intent) {
        startActivity(transaction_intent);
    }
}
