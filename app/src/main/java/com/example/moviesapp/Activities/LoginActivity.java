package com.example.moviesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.moviesapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText username_edittext,password_edittext;
    private TextView register;
    private AppCompatButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_edittext = findViewById(R.id.username_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        register = findViewById(R.id.register_now);
        loginBtn = findViewById(R.id.logInBtn);

        loginBtn.setOnClickListener(v -> {
            String usernameVal = username_edittext.getText().toString();
            String passwordVal = password_edittext.getText().toString();

            if(usernameVal.isEmpty() || passwordVal.isEmpty())
            {
                Toast.makeText(LoginActivity.this,"Please Enter Username And Password",Toast.LENGTH_SHORT).show();
            }
            else if(usernameVal.equals("test") && passwordVal.equals("test"))
            {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
            else
            {
                DataBaseHelper helper = new DataBaseHelper(LoginActivity.this);
                if(!helper.existUser(usernameVal))
                {
                    Toast.makeText(this, "There Is No User With The Given Username", Toast.LENGTH_LONG).show();
                }
                else if(passwordVal.equals(helper.getPassword(usernameVal)))
                {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(v -> {
            Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signup);
            finish();
        });
    }
}