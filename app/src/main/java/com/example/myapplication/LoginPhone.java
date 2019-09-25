package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginPhone extends AppCompatActivity {
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    EditText etPhone;
    Spinner spinner;
    Button btnPhone;
    CountryCodePicker ccp;
    public static String number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_phone);

        etPhone = findViewById(R.id.etphone);
        btnPhone = findViewById(R.id.btnPhone);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(etPhone);

     /*   spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //  String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = etPhone.getText().toString().trim();
                String phoneNumber = ccp.getFullNumberWithPlus();

                if(number.isEmpty() || number.length() < 10){

                    etPhone.setError("Enter a valid number");
                    etPhone.requestFocus();
                    return;
                }


                Intent intent = new Intent(LoginPhone.this, LoginOtp.class);
                intent.putExtra("phone number",phoneNumber);
                startActivity(intent);




            }
        });

    }

    public void send_sms(View view)
    {


        String phoneNumber = ccp.getFullNumberWithPlus();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                60, TimeUnit.SECONDS,this,mcallback);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){

            Intent intent = new Intent(LoginPhone.this,Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}