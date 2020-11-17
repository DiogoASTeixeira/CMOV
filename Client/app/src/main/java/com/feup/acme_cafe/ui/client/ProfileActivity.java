package com.feup.acme_cafe.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.feup.acme_cafe.data.model.User;
import com.feup.acme_cafe.R;

import java.text.DecimalFormat;

public class ProfileActivity extends AppCompatActivity {

    User user;
    Intent transaction_intent;
    AlertDialog alertDialog;

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
        if(user.getTotal_spent() == 0){
            balance.setText("0€");
        } else {
            balance.setText(new DecimalFormat("#.00").format(user.getTotal_spent()) + "€");
        }
        TextView email = findViewById(R.id.email_prof);
        email.setText(user.getEmail());

        TextView vouchers=  findViewById(R.id.vouchers);
        vouchers.setText(user.getVouchers().size() + "");

        TextView previous_trans = findViewById(R.id.previous_trans);
        previous_trans.setOnClickListener((v)->previous(transaction_intent));

    }

    private void previous(Intent transaction_intent) {
        if(user.getTransactions().size() == 0){
            setAndShowAlertDialog("No previous transactions", "You dont have any previous transaction to be shown!");
        } else {
            startActivity(transaction_intent);
        }
    }

    private void setAndShowAlertDialog(String title, String message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        alertDialog=dialog.create();
        alertDialog.show();
    }
}
