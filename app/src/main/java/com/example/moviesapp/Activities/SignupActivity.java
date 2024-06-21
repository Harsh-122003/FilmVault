package com.example.moviesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.moviesapp.R;

public class SignupActivity extends AppCompatActivity {
    private EditText name_edittext, username_edittext,email_edittext, password_edittext;
    private AppCompatButton signupBtn;
    private String nameVal, emailVal, passwordVal;
    private StringBuilder usernameVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name_edittext = findViewById(R.id.name_edittext);
        username_edittext = findViewById(R.id.username_edittext);
        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        signupBtn = findViewById(R.id.signupBtn);

        usernameVal = new StringBuilder("");

        signupBtn.setOnClickListener(v -> {
            nameVal = name_edittext.getText().toString();
            usernameVal = usernameVal.delete(0, usernameVal.length());
            usernameVal.append(username_edittext.getText().toString());
            // usernameVal = username_edittext.getText().toString();
            emailVal = email_edittext.getText().toString();
            passwordVal = password_edittext.getText().toString();

            if(nameVal.isEmpty() || usernameVal.toString().isEmpty() || emailVal.isEmpty() || passwordVal.isEmpty())
            {
                Toast.makeText(SignupActivity.this, "Please Enter All Credentials Correctly", Toast.LENGTH_SHORT).show();
            }
            else
            {
                DataBaseHelper helper = new DataBaseHelper(SignupActivity.this);
                if(helper.existUser(usernameVal.toString()))
                {
                    Toast.makeText(this, "User Already Exist With This Username " +
                            "\n Please Try Using Different Username", Toast.LENGTH_LONG).show();
                }
                else
                {
                    helper.insertUser(nameVal, usernameVal.toString(), emailVal, passwordVal);
                    Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent login = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
        super.onBackPressed();
    }
}