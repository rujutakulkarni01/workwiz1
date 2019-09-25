package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StarredJobs extends AppCompatActivity {

    TextView tvStar;
    Button btnStarred;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starred_jobs);

        tvStar = findViewById(R.id.tvStar);
        btnStarred = findViewById(R.id.btnStarred);


        btnStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarredJobs.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
