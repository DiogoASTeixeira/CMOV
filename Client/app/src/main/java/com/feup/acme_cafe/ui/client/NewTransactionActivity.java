package com.feup.acme_cafe.ui.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.feup.acme_cafe.data.model.Transaction;
import com.feup.acme_cafe.data.model.User;
import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.Voucher;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

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
    Bitmap bitmap;

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

        finishButton = findViewById(R.id.vouchers_check);
        finishButton.setOnClickListener((v) -> generateQRCode());

        TextView totalView = findViewById(R.id.voucher_dicount);
        totalView.setText(new DecimalFormat("#.00").format(basket.getTotal_value()) + " €");

        TextView totalwithdiscountview = findViewById(R.id.totalwithdiscount);

        totalwithdiscountview.setText(new DecimalFormat("#.00").format(basket.getTotal_value()) + " €");

        voucherAdapter();
    }

    private void generateQRCode()  {
        HashMap<String, Object> transaction = parseTransaction();

        List<JSONObject> list = new ArrayList<>();
        list.add(new JSONObject(transaction));

        String transactionStr = list.get(0).toString();

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(transactionStr, null, QRGContents.Type.TEXT, smallerDimension);

        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            ImageView qrcode = findViewById(R.id.QR_Image);
            qrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.v("QRCode Generation", e.toString());
        }
    }

    public HashMap<String, Object> parseTransaction() {
        //Create voucher Map
        TextView voucherView = findViewById(R.id.selectedVoucherText);
        String voucherName = voucherView.getText().toString();
        Voucher voucher = null;
        for(int i = 0; i < user.getVouchers().size(); i++){
            if(user.getVouchers().get(i).getName().equals(voucherName))
                voucher = user.getVouchers().get(i);
        }
        HashMap<String, Object> voucherMap = new HashMap<>();
        if(voucher != null) {
            voucherMap.put("id", voucher.getId());
        } else {
            voucherMap = null;
        }

        //create products Map
        ArrayList<HashMap<String, Object>> products = new ArrayList<>();
        for(int i = 0; i < user.getBasket().getProducts().size(); i++){
            HashMap<String, Object> product = new HashMap<>();
            product.put("id", user.getBasket().getProducts().get(i).getId());
            for(int j = 0; j < user.getBasket().getProducts().get(i).getCount(); j++){
                products.add(product);
            }
        }

        //create TransactionMap
        HashMap<String, Object> transactionMap = new HashMap<>();
        transactionMap.put("UserId", user.getId());
        transactionMap.put("voucher", voucherMap);
        transactionMap.put("products", products);

        return transactionMap;
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

            if(voucherView.getText().toString().contains("Normal Voucher")){
                double discount = 0.05 * basket.getTotal_value();
                double new_total = basket.getTotal_value() - discount;
                total_with_discount.setText(new DecimalFormat("#.00").format(new_total));
            } else if (voucherView.getText().toString().contains("Coffee Voucher")) {
                double coffee_price = getCoffeePrice(basket);
                if(coffee_price > 0){
                    double new_total = basket.getTotal_value() - coffee_price;
                    total_with_discount.setText(new DecimalFormat("#.00").format(new_total));
                } else {
                    setAndShowAlertDialog();
                    basket.setVoucher("No Voucher Selected");
                    voucherView.setText(basket.getVoucher());
                    total_with_discount.setText(new DecimalFormat("#.00").format(basket.getTotal_value()));
                }
            } else {
                total_with_discount.setText(new DecimalFormat("#.00").format(basket.getTotal_value()));
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