package com.feup.acme_cafe.ui.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.Product;
import com.feup.acme_cafe.data.model.Transaction;
import com.feup.acme_cafe.data.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ProductAdapter adapter;
    User user;
    List<Product> products;
    AlertDialog alertDialog;
    private Intent new_transaction_activity_intent;
    private String urlVoucher = "";
    private String urlTransaction = "";
    private String urlUpdateTotal = "";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (User) getIntent().getSerializableExtra("user");
        new_transaction_activity_intent = new Intent(this, NewTransactionActivity.class);
        products = user.getProducts();
        queue = Volley.newRequestQueue(this);
        urlVoucher = "http://" + Util.ip_address + ":3000/user/voucher";
        urlTransaction = "http://" + Util.ip_address + ":3000/user/transaction";
        urlUpdateTotal = "http://" + Util.ip_address + ":3000/user/total";

        getTransactions(user);
        getVouchers(user);
        updateTotalSpent(user);

        displayProducts();

        Button checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener((v) -> checkout(user));
    }

    private void updateTotalSpent(User user) {
        String id = user.getId();

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.GET, urlUpdateTotal + "/" + id, new JSONObject(),
                response -> {
                    Log.d("total spent response", response.toString());
                    user.setTotalSpent(response);
                },
                error -> {
                    setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                    Log.d("total spent error", error.toString());
                }
        ) {};
        queue.add(jsonobj);
    }

    public void getTransactions(User user) {
        Map<String, String> info= new HashMap<>();
        info.put("UserId", user.getId());
        List<JSONObject> list = new ArrayList<>();
        list.add(new JSONObject(info));

        JsonArrayRequest jsonobj = new JsonArrayRequest(Request.Method.POST, urlTransaction, new JSONArray(list),
                response -> {
                    Log.d("transactions response", response.toString());
                    user.setTransactions(response);
                },
                error -> {
                    setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                    Log.d("transactions error", error.toString());
                }
        ) {};
        queue.add(jsonobj);
    }

    private void getVouchers(User user) {

        Map<String,String> info= new HashMap<>();
        info.put("UserId", user.getId());
        List<JSONObject> list = new ArrayList<>();
        list.add(new JSONObject(info));

        JsonArrayRequest jsonobj = new JsonArrayRequest(Request.Method.POST, urlVoucher, new JSONArray(list),
                response -> {
                    Log.d("vouchers response", response.toString());
                    user.setVouchers(response);
                },
                error -> {
                    setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                    Log.d("vouchers error", error.toString());
                }
        ) {
        };
        queue.add(jsonobj);
    }

    private void checkout(User user) {
        int count = adapter.getCount();
        List<Product> products = new ArrayList<>();
        float total_value = 0;

        for (int i = 0; i < count; i++) {
            Product product = adapter.getItem(i);
            if (product.getCount() > 0) {
                total_value += (product.getPrice() * product.getCount());
                products.add(product);
            }
        }

        if(products.size() == 0) {
            setAndShowAlertDialog("Empty Cart", "Please choose a product to buy");
        } else {
            Transaction trans = new Transaction();
            trans.setProducts(products);
            trans.setTotal_value(total_value);

            user.setBasket(trans);

            new_transaction_activity_intent.putExtra("user", user);
            startActivity(new_transaction_activity_intent);
        }
    }

    private void displayProducts() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayShowHomeEnabled(true);
        }

        adapter = new ProductAdapter(this, user.getProducts());

        ListView productsList = findViewById(R.id.listview);
        productsList.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            Intent i = new Intent(this, NewTransactionActivity.class);
            i.putExtra("user", user);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return(true);
        }
        else if (item.getItemId() == R.id.profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("user", user);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return (true);
        }
        else if (item.getItemId() == R.id.logout) {
            finish();
            Intent i = new Intent(this, LoginActivity.class);
            try {
                User user = Util.loadUser(getApplicationContext());
                if (user != null)
                    Util.deletefile(getApplicationContext());
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return (true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    static class ProductAdapter extends ArrayAdapter<Product> {
        private final int layoutResource;
        private final Context mContext;

        ProductAdapter(@NonNull Context context, @NonNull List<Product> objects) {
            super(context, R.layout.row, objects);
            layoutResource = R.layout.row;
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

            Product p = getItem(position);

            if (p != null) {
                TextView name = line.findViewById(R.id.prod_name);
                TextView price = line.findViewById(R.id.prod_price);
                ImageView pic = line.findViewById(R.id.pic);

                p.setCount(0);
                Button increase = line.findViewById(R.id.increase);
                View finalLine = line;
                increase.setOnClickListener((v)->increaseQuantity(finalLine, p));

                Button decrease = line.findViewById(R.id.decrease);
                decrease.setOnClickListener((v)->decreaseQuantity(finalLine, p));

                if (price != null) {
                    price.setText(new DecimalFormat("#.00").format(p.getPrice()) + "€");
                }

                if (name != null) {
                    name.setText(p.getName());
                }

                if(pic != null){
                    Object[] urls = new Object[2];
                    urls[0] = p.getUrl();
                    urls[1] = pic;
                    new RetrieveBitmap().execute(urls);
                }
            }
            return line;
        }

        class RetrieveBitmap extends AsyncTask<Object, ImageView, Void> {

            private Exception exception;

            private Bitmap bmp;

            private ImageView pic;

            protected Void doInBackground(Object... urls) {
                pic = (ImageView) urls[1];
                try {
                    String url = "http://" + Util.ip_address + ":3000/icons/" + urls[0];
                    URL link = null;
                    try {
                        link = new URL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    bmp = null;
                    try {
                        bmp = BitmapFactory.decodeStream(link.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    this.exception = e;

                    return null;
                }
                return null;
            }

            protected void onPostExecute(Void feed) {
                pic.setImageBitmap(bmp);
            }
        }

        private void increaseQuantity(View line, Product p) {
            TextView amountText = line.findViewById(R.id.amount);
            int amount = Integer.parseInt(amountText.getText().toString()) + 1;
            amountText.setText(Integer.toString(amount));
            p.setCount(amount);
        }

        private void decreaseQuantity(View line, Product p) {
            TextView amountText = line.findViewById(R.id.amount);
            int amount = Integer.parseInt(amountText.getText().toString()) - 1;
            if(amount == -1) {
                amount = 0;
            }
            amountText.setText(Integer.toString(amount));
            p.setCount(amount);
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