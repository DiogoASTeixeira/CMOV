package com.feup.acme_cafe.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.feup.acme_cafe.data.model.Transaction;
import com.feup.acme_cafe.data.model.User;
import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.Voucher;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewTransactionActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    final static int DIMENSION = 500;
    final static String CH_SET = "ISO-8859-1";
    private Transaction basket;
    Button finishButton;
    User user;
    AlertDialog alertDialog;
    Util.ProductAdapter adapter;
    ListView productsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        user = (User) getIntent().getSerializableExtra("user");

        if (user.getBasket()!=null){
            if (!user.getBasket().getProducts().isEmpty())
                basket=user.getBasket();
            else {
                basket = new Transaction();
                user.setBasket(basket);
            }
        }
        else
        {
            basket = new Transaction();
            user.setBasket(basket);
        }

        productsListView = findViewById(R.id.productsNew);
        adapter = new Util.ProductAdapter(this, R.layout.row_new, basket.getProducts());
        productsListView.setAdapter(adapter);

        finishButton = findViewById(R.id.generateQRcode);
        //finishButton.setOnClickListener((v) -> generateQRcode());

        TextView totalView = findViewById(R.id.prod_price);
        totalView.setText(basket.getTotal_value() + " €");

        totalView = findViewById(R.id.prod_price);
        totalView.setText(basket.getTotal_value() + " €");

        TextView totalwithdiscountview = findViewById(R.id.totalwithdiscount);

        totalwithdiscountview.setText(basket.getTotal_value() + " €");

        voucherAdapter();
    }

    public String parseTransaction(Float discount, String voucher) {
        StringBuilder contents = new StringBuilder();
        for (int i = 0; i < basket.getProducts().size(); i++) {
            contents.append(basket.getProducts().get(i).getId()).append(";").append(basket.getProducts().get(i).getPrice().toString());
            if (i < basket.getProducts().size() - 1)
                contents.append("|");
        }
        if (voucher.equals(""))
            voucher = "0";
        contents.append(",").append(voucher).append(",").append(discount).append(",").append(user.getId());
        return contents.toString();
    }

    public void backButton(View view) {
        Intent i = new Intent(this, MainActivity.class);
        user.setBasket(new Transaction());
        i.putExtra("user", user);
        startActivity(i);
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);

    }

    private void voucherAdapter(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Voucher");
        List<Voucher> vouchers_aux = user.getVouchers();

        String[] vouchers = new String[vouchers_aux.size()];

        for(int i = 0; i < vouchers_aux.size(); i++) {
            vouchers[i] = vouchers_aux.get(i).getName();
        }

        builder.setItems(vouchers, (dialog, which) -> {
            TextView voucherView = findViewById(R.id.selectedVoucherText);
            TextView total_with_discount = findViewById(R.id.totalwithdiscount);

            String voucher = vouchers[which];
            if (basket.getVoucher().equals(voucher)){
                basket.setVoucher("No Voucher Selected");
            }
            else
                basket.setVoucher(voucher);

            voucherView.setText(basket.getVoucher());

            if(voucherView.getText().equals("Normal Voucher")){
                double discount = 0.05 * basket.getTotal_value();
                double new_total = basket.getTotal_value() - discount;
                total_with_discount.setText(String.valueOf(new_total));
            } else if (voucherView.getText().equals("Coffee Voucher")) {
                double coffee_price = getCoffeePrice(basket);
                if(coffee_price > 0){
                    double new_total = basket.getTotal_value() - coffee_price;
                    total_with_discount.setText(String.valueOf(new_total));
                } else {
                    setAndShowAlertDialog();
                    basket.setVoucher("No Voucher Selected");
                }
            } else {
                total_with_discount.setText(String.valueOf(basket.getTotal_value()));
            }

        });
        Button voucherButton = findViewById(R.id.selectVoucherButton);
        voucherButton.setOnClickListener((v) ->  builder.show());
    }

    private double getCoffeePrice(@NotNull Transaction basket) {
        double coffee_price = 0;

        for(int i = 0; i < basket.getProducts().size(); i++){
            if(basket.getProducts().get(i).getName().equals("Coffee")){
                coffee_price = basket.getProducts().get(i).getPrice();
            }
        }

        return coffee_price;
    }

    private void setAndShowAlertDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("You can only use a coffee voucher when you choose to buy a coffee!");
        dialog.setTitle("Coffee Voucher Error");
        alertDialog=dialog.create();
        alertDialog.show();
    }

}