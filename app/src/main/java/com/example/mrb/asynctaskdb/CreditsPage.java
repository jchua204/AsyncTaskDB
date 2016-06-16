package com.example.mrb.asynctaskdb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreditsPage extends AppCompatActivity {
    Button btnBack;
    TextView txtCredits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_page);

        txtCredits = (TextView) findViewById(R.id.txtCredits);
        String Credits = getIntent().getStringExtra("credits");
        txtCredits.setText(Credits);
    }
    public void BackFromCredits (View vw){
        Intent goBackToCredits = new Intent();
        setResult(0, goBackToCredits);
        finish();
    }
}
