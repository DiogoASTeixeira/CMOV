package com.feup.acme_cafe.ui.client;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.feup.acme_cafe.data.model.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.feup.acme_cafe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.feup.acme_cafe_app.USERNAME";
    private EditText emailEditText;
    private EditText passwordEditText;
    private String urlLogin = "";
    private String urlVoucher = "";
    private String urlTransaction = "";
    private RequestQueue queue;
    private Intent register_intent;
    private Intent main_activity_intent;
    AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.login);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        TextView registerText = findViewById(R.id.register_text);
        queue = Volley.newRequestQueue(this);
        register_intent = new Intent( this, RegisterActivity.class);
        main_activity_intent = new Intent(this, MainActivity.class);
        urlLogin = "http://" + Util.ip_address + ":3000/user/login";
        urlVoucher = "http://" + Util.ip_address + ":3000/user/voucher";
        urlTransaction = "http://" + Util.ip_address + ":3000/user/transaction";

        try {
            User user = Util.loadUser(getApplicationContext());
            if (user != null)
                getTransactions(user);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        loginButton.setOnClickListener((v)->loginUser());
        registerText.setOnClickListener((v)->registerUser());
    }

    public void getProducts(User user) {
        String urlProducts = "http://" + Util.ip_address + ":3000/product";
        JsonArrayRequest jsonobj = new JsonArrayRequest(Request.Method.GET, urlProducts, new JSONArray(),
                response -> {
                    Log.d("products response", response.toString());
                    try {
                        user.setProducts(response);
                        try {
                            Util.saveUser(user, getApplicationContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        openStore(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                    Log.d("products error", error.toString());
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
                    getVouchers(user);
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
                    getProducts(user);
                },
                error -> {
                    setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                    Log.d("vouchers error", error.toString());

                }
        ) {
        };
        queue.add(jsonobj);
    }

    private void openStore(User user) {
        main_activity_intent.putExtra("user", user);
        startActivity(main_activity_intent);
    }

    private void registerUser() {
        startActivity(register_intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loginUser() {

        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final User[] user = new User[1];

        final String encrypted_password = PasswordUtil.generateEncryptedPassword(password);

        HashMap<String, String> info= new HashMap<>();
        info.put("email", email);
        info.put("password", encrypted_password);

        if(!email.equals("") && !password.equals("")) {
            JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, urlLogin, new JSONObject(info),
                    response -> {
                        try {
                            Object obj = response.get("user");
                            if (obj.toString().equals("null")) {
                                setAndShowAlertDialog("Login Error", "Wrong username/password combination");
                                Log.d("login", "WRONG LOGIN");
                            } else {
                                JSONObject jsonObj = response.getJSONObject("user");
                                user[0] = new User(jsonObj);
                                getTransactions(user[0]);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                        Log.d("login error", error.toString());
                    }
            ) {
            };
            queue.add(jsonobj);
        } else {
            setAndShowAlertDialog("Login Error", "All fields must be filled");
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