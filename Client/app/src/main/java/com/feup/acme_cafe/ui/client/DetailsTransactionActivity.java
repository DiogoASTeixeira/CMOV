package com.feup.acme_cafe.ui.client;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.feup.acme_cafe.data.model.Product;
import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsTransactionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Util.ProductAdapter adapter;
    Double transactionTotal = 0.0;
    ArrayList<Product> products;
    AlertDialog alertDialog;
    ImageButton deleteButton;
    String urlTransaction = "";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        urlTransaction = "http://" + Util.ip_address + ":3000/user/transaction";
        user = (User) getIntent().getSerializableExtra("user");
        String id = getIntent().getStringExtra("TransactionId");
        Double discount = getIntent().getDoubleExtra("discount", 0);

        ListView listp = findViewById(R.id.productsDetails);
        products = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> info= new HashMap<>();
        info.put("TransactionId", id);
        List<JSONObject> list = new ArrayList<>();
        list.add(new JSONObject(info));

        String url = "http:/"+ Util.ip_address +":3000/product/transaction"; //IP Address
        JsonArrayRequest jsonobj = new JsonArrayRequest(Request.Method.POST, url, new JSONArray(list),
                response -> {
                    Log.d("details response", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonobject = response.getJSONObject(i);
                            String product_id = jsonobject.getString("id");
                            String name = jsonobject.getString("name");
                            Float price = Float.parseFloat(jsonobject.getString("value"));
                            Integer count = Integer.parseInt(jsonobject.getString("count"));
                            products.add((new Product(product_id, name, price, count)));
                            transactionTotal += ((price*count));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    TextView total= findViewById(R.id.sumproducts);
                    total.setText(new DecimalFormat("#.00").format(transactionTotal-discount) + " €");

                    Log.d("products:", products.toString());

                    adapter = new Util.ProductAdapter(this, R.layout.row_new, products);
                    listp.setAdapter(adapter);
                },
                error -> {
                    setAndShowAlertDialog("Error", "Details TransactionId");
                    Log.d("transactions error", error.toString());
                }
        ) {
        };
        queue.add(jsonobj);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener((v) -> deleteTransaction(id, user));
    }

    private void deleteTransaction(String id, User user) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http:/"+ Util.ip_address +":3000/product/transaction/" + id; //IP Address

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.DELETE, url, new JSONObject(),
                response -> {
                    Log.d("response", response.toString());
                    Intent i = new Intent(this, ProfileActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                    finish();
                },
                error -> {
                    setAndShowAlertDialog("Delete Transaction error", "Error deleting transaction");
                    Log.d("del transactions error", error.toString());
                }
        ) {
        };
        queue.add(jsonobj);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void setAndShowAlertDialog(String title, String message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        alertDialog=dialog.create();
        alertDialog.show();
    }
}
