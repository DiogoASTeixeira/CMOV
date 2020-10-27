package com.feup.acme_cafe.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.feup.acme_cafe.data.model.*;

import androidx.appcompat.app.AppCompatActivity;

import com.feup.acme_cafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.feup.acme_cafe_app.USERNAME";
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView registerText;
    private Button loginButton;
    private String urlLogin = "http://192.168.1.82:3000/user/login";
    private RequestQueue queue;
    private Intent register_intent;
    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerText = findViewById(R.id.register_text);
        queue = Volley.newRequestQueue(this);
        register_intent = new Intent( this, RegisterActivity.class);

        loginButton.setOnClickListener((v)->loginUser());
        registerText.setOnClickListener((v)->registerUser());
    }

    private void registerUser() {
        startActivity(register_intent);
    }

    public void loginUser() {

        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        HashMap info= new HashMap();
        info.put("email", email);
        info.put("password", password);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, urlLogin, new JSONObject(info),
                response -> {
                    try {
                        Object obj = response.get("user");
                        if (obj.toString().equals("null")){
                            setAndShowAlertDialog("Login Error", "Wrong username/password combination");
                            Log.d("login", "WRONG LOGIN");
                        }
                        else {
                            JSONObject jsonObj = response.getJSONObject("user");
                            User user = new User(jsonObj);
                            //getTransactions(user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                    Log.d("login error", error.toString());
                }
        ) {};

        queue.add(jsonobj);


        //Pass params to next page
        //intent.putExtra(EXTRA_MESSAGE, username);
        //startActivity(intent);
    }

    private void setAndShowAlertDialog(String title, String message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        alertDialog=dialog.create();
        alertDialog.show();
    }
}