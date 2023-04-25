package uk.readingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailAddressLogin;
    TextInputEditText passwordLogin;
    Button button_Register1;
    Button button_Login1;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddressLogin = findViewById(R.id.emailAddressLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        button_Register1 = findViewById(R.id.button_Register1);
        button_Login1 = findViewById(R.id.button_Login1);

    mAuth = FirebaseAuth.getInstance();

    button_Login1.setOnClickListener(view -> {
        loginUser();
    });
    button_Register1.setOnClickListener(view -> {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    });
    }

    private void loginUser(){
        String email = emailAddressLogin.getText().toString();
        String password = passwordLogin.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailAddressLogin.setError("Email cannot be empty");
            emailAddressLogin.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            passwordLogin.setError("Password cannot be empty");
            passwordLogin.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Login Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


}