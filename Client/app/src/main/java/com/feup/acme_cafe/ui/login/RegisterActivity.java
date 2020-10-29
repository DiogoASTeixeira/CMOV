package com.feup.acme_cafe.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.feup.acme_cafe.R;

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

    private String urlLogin = "http://192.168.1.77:3000/user/register";
    private RequestQueue queue;
    private Intent login_intent;
    AlertDialog alertDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void register() {
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        final String name = nameEditText.getText().toString();
        final String card_number = cardnumberEditText.getText().toString();
        final String card_cvs = cardcvsEditText.getText().toString();
        final String nif = nifEditText.getText().toString();
        UUID id = UUID.randomUUID();

        final String encrypt_password = PasswordUtil.generateEncryptedPassword(password);

        System.out.println(encrypt_password);

        HashMap info= new HashMap();
        info.put("id", id);
        info.put("email", email);
        info.put("username", username);
        info.put("name", name);
        info.put("password", encrypt_password);
        info.put("card_number", card_number);
        info.put("card_cvs", card_cvs);
        info.put("nif", nif);

        if(!email.equals("") && !username.equals("") && !name.equals("") && !password.equals("") && !card_number.equals("") && !card_cvs.equals("") && !nif.equals("")) {
            JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, urlLogin, new JSONObject(info),
                    response -> {
                        try {
                            Object obj = response.get("user");
                            if (obj.toString().equals("null")) {
                                setAndShowAlertDialog("Register Error", "Something went wrong with the register");
                                Log.d("register", "WRONG REGIST");
                            } else {
                                setAndShowAlertDialog("Register Success", "Please Login now");
                                Log.d("register", "Successful REGIST");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        setAndShowAlertDialog("Server Error", "Unexpected Server Error");
                        Log.d("register error", error.toString());
                    }
            ) {
            };

            queue.add(jsonobj);
            login();
        }
        else {
            setAndShowAlertDialog("Register Error", "All the fields must be filled");
        }
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