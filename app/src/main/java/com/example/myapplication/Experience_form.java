package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.SignUpActivity.COLLECTION_NAME_KEY;

public class Experience_form extends AppCompatActivity {

    TextView tvCompany,tvTitle,tvStart,tvEnd;
    EditText etCompany,etTitle,etStart,etEnd;
    Button btnSave;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience_form);

        tvCompany = findViewById(R.id.company);
        tvTitle = findViewById(R.id.title);
        tvStart = findViewById(R.id.startDate);
        tvEnd = findViewById(R.id.endDate);

        etTitle = findViewById(R.id.etTitle);
        etCompany = findViewById(R.id.etCompany);
        etStart = findViewById(R.id.etStart);
        etEnd = findViewById(R.id.etEnd);

        btnSave = findViewById(R.id.btnExp);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadExperienceForm();
            }
        });
    }
   private void loadExperienceForm(){

       final userExperience userExperience = new userExperience();
       userExperience.setCompany(etCompany.getText().toString());
       userExperience.setTitle(etTitle.getText().toString());
       userExperience.setStart(etStart.getText().toString());
       userExperience.setEnd(etEnd.getText().toString());



       Map<String,Object> data1 = new HashMap<>();
       data1.put("company",etCompany.getText().toString());
       data1.put("title",etTitle.getText().toString());
       data1.put("start",etStart.getText().toString());
       data1.put("end",etEnd.getText().toString());
       db.collection(COLLECTION_NAME_KEY).document(user.getUid()).collection("Experience").document()
               .set(data1)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {


                       Toast.makeText(Experience_form.this, "Updated", Toast.LENGTH_SHORT).show();
                       Intent intent =  new Intent(Experience_form.this, MyProfileFragment.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                       finish();


                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               Toast.makeText(Experience_form.this, "Error has occurred" + e.getMessage(), Toast.LENGTH_SHORT).show();

           }
       });

   }
}
