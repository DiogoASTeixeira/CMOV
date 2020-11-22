package com.feup.acme_cafe_terminal;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe_terminal.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    JSONObject data = null;
    int orderId = -1;
    JSONArray productData = null;
    private RequestQueue queue;
    boolean hasReadCodeOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        updateProductList();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrReader = new Intent(MainActivity.this, QRAct.class);
                int code = 66;
                startActivityForResult(qrReader, code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 66 && resultCode == 66)
        {
           hasReadCodeOnce = true;
            try {
                this.data = new JSONObject(data.getDataString());
            } catch (JSONException |NullPointerException e) {
                this.data = null;
            }
        }
    }

    public JSONObject getMyData() {
        return data;
    }

    public JSONArray getProductData() {
        return productData;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public boolean hasReadCodeOnce() {
        return hasReadCodeOnce;
    }

    public void resetReadState() {
        hasReadCodeOnce = false;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateProductList()
    {
        Snackbar.make(findViewById(R.id.fab), R.string.wait_server, Snackbar.LENGTH_INDEFINITE).show();
        String urlProducts = "http://" + Constants.ip_address + ":3000/product";
        JsonArrayRequest jsonObj = new JsonArrayRequest(Request.Method.GET, urlProducts, new JSONArray(),
                response -> {
                    productData = response;
                    Snackbar.make(findViewById(R.id.fab), R.string.connection_success, Snackbar.LENGTH_SHORT).show();
                    findViewById(R.id.fab).setEnabled(true);
                },
                error -> {
                    Snackbar.make(findViewById(R.id.fab), error.toString() , Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, view -> {
                                updateProductList();
                            })
                            .show();
                    //error.toString();
                }
        ) {};
        queue.add(jsonObj);
    }


}