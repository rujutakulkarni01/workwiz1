package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class MyProfile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    ImageView profile;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvName;
    Uri profileUri;
    ProgressBar progressBar;
    StorageTask uploadTask;
    String uid;
    FirebaseUser user;
    Dialog myDialog;
    TextView addExperience, addSkills, tvExperience, tvSkills;
    public static String name, email, phoneNo;
    List<String>list = new ArrayList<String>();


    public static final String COLLECTION_NAME_KEY = "Users";
    String profileUrl;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private boolean executed;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhn);
        tvName = findViewById(R.id.tvName);
        profile = findViewById(R.id.profile);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        collectionReference = db.collection(COLLECTION_NAME_KEY);
        user = mAuth.getCurrentUser();
        myDialog = new Dialog(this);
        addExperience = findViewById(R.id.addExperience);
        addSkills = findViewById(R.id.addSkills);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executed = showImageChooser();
                if (executed == true) {

                    dialog();
                }


            }
        });


        addExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyProfile.this, Experience_form.class);
                startActivity(intent);

            }
        });

        addSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyProfile.this, Skills.class);
                startActivity(i);

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();


        CollectionReference collectionReference = db.collection(COLLECTION_NAME_KEY);
        DocumentReference docRef = db.collection(COLLECTION_NAME_KEY).document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Users users = new Users();
                    document.getData();
                    users.setName((String) document.get("name"));
                    users.setPhoneNo((String) document.get("phoneNo"));
                    users.setEmail((String) document.get("email"));


                    name = users.getName();
                    email = users.getEmail();
                    phoneNo = users.getPhoneNo();


                    tvName.setText(name);
                    tvEmail.setText(email);
                    tvPhone.setText(phoneNo);

                }
            }
        });
        loadUserInfo();
        getExperienceForm();
        getSkills();


    }

    private void loadUserInfo() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .centerCrop()
                        .into(profile);
            }


        }
    }

    private void saveUserInformation() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profileUrl != null) {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(profileUrl))
                    .build();

            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(MyProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            profileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileUri);
                profile.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImageToFirebaseStorage() {

        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (profileUri != null) {


            profileImageRef.putFile(profileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            //   profileUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            profileImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    profileUrl = task.getResult().toString();
                                    Log.i("URL", profileUrl);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            Toast.makeText(MyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }

    }

    private boolean showImageChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
        executed = true;
        return executed;

    }

    private void dialog() {


        final AlertDialog.Builder builder
                = new AlertDialog.Builder(MyProfile.this);
        builder.setMessage("Are you sure you want to change the profile?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveUserInformation();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getExperienceForm() {

        DocumentReference docRef = db.collection(COLLECTION_NAME_KEY).document(user.getUid()).collection("Experience").document(user.getUid());

        db.collection(COLLECTION_NAME_KEY).document(user.getUid()).collection("Experience").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<userExperience> list = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                userExperience experience = documentSnapshot.toObject(userExperience.class);
                                list.add(experience);
                            }
                            ListView listView = findViewById(R.id.listExperience);
                            ExperienceAdapter experienceAdapter = new ExperienceAdapter(MyProfile.this, list);
                            listView.setAdapter(experienceAdapter);
                        } else {
                            Log.d("MyProfile", "Error getting document", task.getException());
                        }
                    }
                });
    }

    private void getSkills() {
        DocumentReference docRef = db.collection(COLLECTION_NAME_KEY).document(user.getUid()).collection("Skills").document(user.getUid());

        db.collection(COLLECTION_NAME_KEY).document(user.getUid()).collection("Skills").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    list.add((documentSnapshot.get("skill")).toString());
                }
                ArrayAdapter<String>adapter = new ArrayAdapter<String>(MyProfile.this,android.R.layout.simple_selectable_list_item,list);
                ListView listView = findViewById(R.id.listSkills);
                listView.setAdapter(adapter);
            }
        });

    }
}


