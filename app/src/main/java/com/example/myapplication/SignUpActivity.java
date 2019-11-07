package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    ProgressBar progressBar;
    EditText etEmail, etPassword, etName, etPhoneNo;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public static final  String COLLECTION_NAME_KEY = "Users";
    private FirebaseUser user;
    Uri profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        etName = (EditText) findViewById(R.id.editTextName);
        etPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


    }


    @Override
    protected void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();

        if(user != null){
            //handle the already registered user


        }

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;


        }
    }

    private void registerUser() {

        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        final String phone = etPhoneNo.getText().toString().trim();

        if(name.isEmpty()) {

            etName.setError("name is required");
            etName.requestFocus();
            return;
        }

        if(phone.isEmpty()) {

            etPhoneNo.setError("Phone no is required");
            etPhoneNo.requestFocus();
            return;
        }

        if (phone.length()!=10){

            etPhoneNo.setError("Invalid phone no");
            etPhoneNo.requestFocus();
            return;
        }

        if(email.isEmpty()) {

            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {

            etPassword.setError("Password is requisers.setName((Stred");
            etPassword.requestFocus();
            return;
        }


        if (password.length()<6){

            etPassword.setError("Minimum length of password should be 6");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful())

                        {
                            loadData();
                        }

                        else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "Already regsitered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

   //   loadData();



    }
    private void loadData()
    {


        final Users users = new Users();
        users.setName(etName.getText().toString());
        users.setPhoneNo(etPhoneNo.getText().toString());
        users.setEmail(etEmail.getText().toString());
    //    users.setPassword(etPassword.getText().toString());
        db.collection(COLLECTION_NAME_KEY).document().set(users);

      //  db.collection(COLLECTION_NAME_KEY).document(etEmail.getText().toString()).set(users);

        Map<String,Object>data = new HashMap<>();
         data.put("name",etName.getText().toString());
         data.put("email",etEmail.getText().toString());
         data.put("phoneNo",etPhoneNo.getText().toString());
        db.collection(COLLECTION_NAME_KEY).document(user.getUid())
        .set(data)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                    Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent =  new Intent(SignUpActivity.this, Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SignUpActivity.this, "Error has occurred" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }
}






