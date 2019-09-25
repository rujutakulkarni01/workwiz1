package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginOtp extends AppCompatActivity {


    private String verificationId;
    ProgressBar progressBar;

    EditText etotp;
    FirebaseAuth mAuth;
    Button btnotp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification);

        mAuth = FirebaseAuth.getInstance();
        btnotp = findViewById(R.id.btnotp);
        etotp = findViewById(R.id.etotp);
        progressBar = findViewById(R.id.progressbar);

        String phoneNumber = getIntent().getStringExtra("phone number");
        sendVerificationCode(phoneNumber);



        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = etotp.getText().toString().trim();

                if (code.isEmpty() || code.length()<6){

                    etotp.setError("Invalid code");
                    etotp.requestFocus();
                    return;
                }


                verifyCode(code);


            }
        });


    }

    private void verifyCode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);

    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){


                            Toast.makeText(LoginOtp.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginOtp.this,SignUpActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {

                            Toast.makeText(LoginOtp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    private void sendVerificationCode(String number){
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                number,60,TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){

                etotp.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(LoginOtp.this,e.getMessage() , Toast.LENGTH_SHORT).show();

        }
    };



}


