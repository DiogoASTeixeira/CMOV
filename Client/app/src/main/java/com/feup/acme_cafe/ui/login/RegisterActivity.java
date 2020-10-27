package com.feup.acme_cafe.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe.R;
import com.feup.acme_cafe.data.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.feup.acme_cafe_app.USERNAME";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText nifEditText;
    private EditText cardnumberEditText;
    private EditText cardcvsEditText;
    private EditText emailEditText;
    private TextView loginText;
    private Button registerButton;

    private String urlLogin = "http://192.168.1.82:3000/user/register";
    private RequestQueue queue;
    private Intent login_intent;
    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        nameEditText = findViewById(R.id.name);
        cardnumberEditText = findViewById(R.id.card_number);
        cardcvsEditText = findViewById(R.id.card_cvs);
        emailEditText = findViewById(R.id.email);
        loginText = findViewById(R.id.login_text);
        nifEditText = findViewById(R.id.nif);
        queue = Volley.newRequestQueue(this);
        login_intent = new Intent( this, LoginActivity.class);

        loginText.setOnClickListener((v)->login());
        registerButton.setOnClickListener((v)->register());
    }

    private void register() {
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        final String name = nameEditText.getText().toString();
        final String card_number = cardnumberEditText.getText().toString();
        final String card_cvs = cardcvsEditText.getText().toString();
        final String nif = nifEditText.getText().toString();
        UUID id = UUID.randomUUID();

        HashMap info= new HashMap();
        info.put("id", id);
        info.put("email", email);
        info.put("username", username);
        info.put("name", name);
        info.put("password", password);
        info.put("card_number", card_number);
        info.put("card_cvs", card_cvs);
        info.put("nif", nif);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, urlLogin, new JSONObject(info),
                response -> {
                    try {
                        Object obj = response.get("user");
                        if (obj.toString().equals("null")){
                            setAndShowAlertDialog("Register Error", "Something went wrong with the register");
                            Log.d("register", "WRONG REGIST");
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
                    Log.d("register error", error.toString());
                }
        ) {};

        queue.add(jsonobj);
    }

    private void login() {
        startActivity(login_intent);
    }

    private void setAndShowAlertDialog(String title, String message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        alertDialog=dialog.create();
        alertDialog.show();
    }

}