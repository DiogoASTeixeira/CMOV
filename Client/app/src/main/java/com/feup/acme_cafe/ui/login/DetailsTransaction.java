package com.feup.acme_cafe.ui.login;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.feup.acme_cafe.data.model.Product;
import com.feup.acme_cafe.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsTransaction extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Util.ProductAdapter adapter;
    private RequestQueue queue;
    Float transactionTotal = 0f;
    ArrayList<Product> products;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        String id = getIntent().getStringExtra("TransactionId");

        ListView listp = findViewById(R.id.productsDetails);
        products = new ArrayList();
        queue = Volley.newRequestQueue(this);

        Map info= new HashMap();
        info.put("TransactionId", id);
        List list = new ArrayList();
        list.add(new JSONObject(info));

        System.out.println(id);

        String url = "http:/"+ Util.ip_address +":3000/product/transaction"; //IP Address
        JsonArrayRequest jsonobj = new JsonArrayRequest(Request.Method.POST, url, new JSONArray(list),
                response -> {
                    Log.d("details response", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject jsonobject = response.getJSONObject(i);
                            String name = jsonobject.getString("name");
                            Float price = Float.parseFloat(jsonobject.getString("value"));
                            Integer count = Integer.parseInt(jsonobject.getString("count"));
                            for (int j=0;j<count;j++)
                            {
                                products.add(new Product(name, price, url));
                                transactionTotal += price;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    TextView total= findViewById(R.id.sumproducts);
                    total.setText(transactionTotal.toString()+" €");
                    adapter = new Util.ProductAdapter(this, R.layout.row, products);
                    listp.setAdapter(adapter);
                },
                error -> {
                    setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                    Log.d("transactions error", error.toString());

                }
        ) {
        };
        queue.add(jsonobj);

        Button back = findViewById(R.id.back);
        back.setOnClickListener((v)->finish());
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
