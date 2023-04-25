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

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText emailAddressRegister;
    TextInputEditText passwordRegister;
    Button button_Register2;
    Button button_Login2;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailAddressRegister = findViewById(R.id.emailAddressRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        button_Register2 = findViewById(R.id.button_Register2);
        button_Login2 = findViewById(R.id.button_Login2);

        mAuth = FirebaseAuth.getInstance();

        button_Register2.setOnClickListener(view -> {
            createUser();
        });

        button_Login2.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        String email = emailAddressRegister.getText().toString();
        String password = passwordRegister.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailAddressRegister.setError("Email cannot be empty");
            emailAddressRegister.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            passwordRegister.setError("Password cannot be empty");
            passwordRegister.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}