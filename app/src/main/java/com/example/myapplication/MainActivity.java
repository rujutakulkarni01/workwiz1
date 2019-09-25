package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.example.myapplication.MyProfile.email;
import static com.example.myapplication.MyProfile.name;
import static com.example.myapplication.MyProfile.phoneNo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText etEmail, etPassword, etName, etPhoneNo;
    FirebaseFirestore db;
    public DocumentReference docRef;
    ProgressBar progressBar;
    SharedPreferences sp;

    public static final String COLLECTION_NAME_KEY = "Users";
    public static final String NAME_KEY = "name";
    public static final String PHONE_KEY = "phoneNO";
    public static final String EMAIL_KEY = "email";
    String usrName, usrEmail, usrPhoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        etName = (EditText) findViewById(R.id.editTextName);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        db = FirebaseFirestore.getInstance();
        sp = getSharedPreferences("login",MODE_PRIVATE);

        if (sp.getBoolean("logged",false)){
            goToMainActivity();
        }





    }

    public void goToMainActivity() {

        Intent intent = new Intent(this,Dashboard.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.textViewSignUp:

                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;


            case R.id.buttonLogin:
                userLogin();
                sp.edit().putBoolean("logged",true).apply();
                break;
        }

    }


    private void userLogin() {

        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        if (email.isEmpty()) {

            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }


        if (password.isEmpty()) {

            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }


        if (password.length() < 6) {

            etPassword.setError("Minimum length of password should be 6");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                intent.putExtra("email",email);
                                intent.putExtra("name",name);
                                intent.putExtra("phoneNo",phoneNo);
                    startActivity(intent);
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "Sign in unsuccessful", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }
}
