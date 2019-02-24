package com.codeking123.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Details extends AppCompatActivity {

     Button mPrivacyButton;
     Button mAboutDeveloperButton;
     Button mAboutAppButton;
     ImageButton mBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newscreen);
        mAboutAppButton=findViewById(R.id.aboutApp);
        mAboutDeveloperButton=findViewById(R.id.aboutDeveloper);
        mPrivacyButton=findViewById(R.id.aboutPrivacy);
        mBackButton=findViewById(R.id.backButton);
        mAboutAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(Details.this,AboutApp.class);
                startActivity(myIntent);
            }
        });
        mPrivacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(Details.this,Privacy.class);
                startActivity(myIntent);
            }
        });
        mAboutDeveloperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(Details.this,AboutDeveloper.class);
                startActivity(myIntent);
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
