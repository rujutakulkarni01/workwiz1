package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.myapplication.MyProfile.COLLECTION_NAME_KEY;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        View headerView = navigationView.getHeaderView(0);

    }

        @Override
        protected void onStart() {
            super.onStart();

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

                        final TextView name = findViewById(R.id.nav_name);
                        final TextView email = findViewById(R.id.nav_email);
                        name.setText(users.getName());
                        email.setText(users.getEmail());

                    }
                }
            });

        }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home){

            Intent intent = new Intent(Dashboard.this, Dashboard.class);
            startActivity(intent);
        }
        if (id == R.id.nav_profile) {
                Intent intent = new Intent(Dashboard.this, MyProfile.class);
                startActivity(intent);
        }
        if (id == R.id.nav_feedback){

            Intent intent = new Intent(Dashboard.this, Feedback.class);
            startActivity(intent);
        }
        if (id == R.id.nav_logout){
           FirebaseAuth.getInstance().signOut();
        }
        /* else if (id == R.id.activeJobs) {

            Intent intent = new Intent(Dashboard.this, ActiveJobs.class);
            startActivity(intent);

        } else if (id == R.id.starredJobs) {

            Intent intent = new Intent(Dashboard.this, StarredJobs.class);
            startActivity(intent);


        }

        else if (id == R.id.statusNotification) {

            Intent intent = new Intent(Dashboard.this, StatusNotification.class);
            startActivity(intent);


        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
