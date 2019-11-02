package com.example.myapplication;

import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;

public class Feedback extends AppCompatActivity {
    EditText namedata ,emaildata,messagedata,rating;
    Button send,details,quiz;
    Firebase firebase;
    RatingBar ratingi,ratingp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namedata = findViewById(R.id.namedata);
        emaildata = findViewById(R.id.emaildata);
        messagedata = findViewById(R.id.messagedata);
        ratingi = findViewById(R.id.ratingi);
        ratingp = findViewById(R.id.ratingp);
        ratingi.setNumStars(5);
        ratingp.setNumStars(5);
        ratingi.setRating((float) 2.5);
        ratingp.setRating((float) 2.5);
        send = findViewById(R.id.btn_send);
        details = findViewById(R.id.btn_details);
        Firebase.setAndroidContext(this);


        String UniqueID =
                Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
        firebase = new Firebase("https://feedback1-85bb6.firebaseio.com/Users" + UniqueID);






        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setEnabled(true);
                final String name = namedata.getText().toString();
                final String email = emaildata.getText().toString();
                final String message = messagedata.getText().toString();

                final float star = ratingi.getRating();
                final float star2 = ratingp.getRating();

                Firebase child_star = firebase.child(" Star rating");
                child_star.setValue(star);

                Firebase child_star2 = firebase.child(" Star rating");
                child_star2.setValue(star2);





                Firebase child_name = firebase.child("Name");
                child_name.setValue(name);

                if (name.isEmpty()) {
                    namedata.setError("THIS IS AN REQUIRED FIELD");
                    send.setEnabled(false);

                } else {
                    namedata.setError(null);
                    send.setEnabled(true);
                }

                Firebase child_email = firebase.child("Email");
                child_email.setValue(email);

                if ( email.isEmpty())
                {
                    emaildata.setError("THIS IS AN REQUIRED FEILD");
                    send.setEnabled(false);
                }else
                {
                    emaildata.setError(null);
                    send.setEnabled(true);
                }

                Firebase child_message = firebase.child("Message");
                child_message.setValue(message);
                if (message.isEmpty())
                {
                    messagedata.setError("THIS IS AN REQUIRED FIELD");
                    send.setEnabled(false);
                }else
                {
                    messagedata.setError(null);
                    send.setEnabled(true);
                }



                Toast.makeText(Feedback.this,"YOUR DATA IS SAVED !",Toast.LENGTH_SHORT).show();
                details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(Feedback.this)
                                .setTitle("Sended Details :")
                                .setMessage("Name - " + name + "\n\nEmail - " +email + "\n\n Message - "+message + "\n\n Rating - "+star2 +" stars : "+star)
                                .show();
                    }
                });
            }

        });


    }
}