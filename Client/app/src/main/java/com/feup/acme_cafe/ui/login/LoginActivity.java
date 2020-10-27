package com.feup.acme_cafe.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.feup.acme_cafe.R;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    public static final String EXTRA_MESSAGE = "com.feup.acme_cafe_app.USERNAME";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginUser(View view) {
        //TODO MainActivity is just as a placeholder
        Intent intent = new Intent( this, MainActivity.class);
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        //TODO Use username and password and perform login

        // Pass params to next page
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);
    }
}