package com.example.moviesapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviesapp.R;

public class SignupActivity extends AppCompatActivity {
    private EditText name_edittext, username_edittext,email_edittext, password_edittext;
    private AppCompatButton signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name_edittext = findViewById(R.id.name_edittext);
        username_edittext = findViewById(R.id.username_edittext);
        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameVal = name_edittext.getText().toString();
                String usernameVal = username_edittext.getText().toString();
                String emailVal = email_edittext.getText().toString();
                String passwordVal = password_edittext.getText().toString();

                if(nameVal.isEmpty() || usernameVal.isEmpty() || emailVal.isEmpty() || passwordVal.isEmpty())
                {
                    Toast.makeText(SignupActivity.this, "Please Enter All Credentials Correctly", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DataBaseHelper helper = new DataBaseHelper(SignupActivity.this);
                    helper.deleteUser("");
                    helper.insertUser(nameVal, usernameVal, emailVal, passwordVal);
                    Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}