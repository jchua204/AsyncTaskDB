package com.example.mrb.asynctaskdb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CreditsPage extends AppCompatActivity {

    Button btnBack;
    TextView txtCredits;
    BuildConfig btnHighscore;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_page);

        txtCredits = (TextView) findViewById(R.id.txtCredits);
        String Credits = getIntent().getStringExtra("credits");
        txtCredits.setText(Credits);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

    }
    public void rating (View vw){
        if (ratingBar.getRating() == 5){
            txtCredits.setText("Thank you for 5 stars! :)");
        }
        if (ratingBar.getRating() == 4){
            txtCredits.setText("Thank you for 4 stars! :)");
        }
        if (ratingBar.getRating() == 3){
            txtCredits.setText("Thank you for 3 stars! I hope we can improve :)");
        }
        if (ratingBar.getRating() == 2) {
            txtCredits.setText("Thank you for 5 stars!  We will do better next:)");
        }else{
            txtCredits.setText("Sorry about that.We are still incomplete");
        }
    }

    public void showAllHighscore(View vw) {
        new showHighscore().execute();
    }

    private class showHighscore extends AsyncTask<Void, String, Boolean> {
        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private Cursor crsrDBReader;
        private ArrayList<String> arylstAllItems;

        @Override
        protected void onPreExecute() {
            atDatabaseHelper = new ATDatabaseHelper(CreditsPage.this, null, null, 0);
            arylstAllItems = new ArrayList<String>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                db = atDatabaseHelper.getReadableDatabase();
            } catch (SQLiteException sqlEx) {
                return false;
            }

            crsrDBReader = db.rawQuery("SELECT * FROM LIST ", null);

            if (crsrDBReader != null) {
                if (crsrDBReader.moveToFirst()) {
                    while (crsrDBReader.isAfterLast() == false) {
                        String strItem = crsrDBReader.getString(crsrDBReader.getColumnIndex("HIGHSCORE"));

                        arylstAllItems.add(strItem);

                        crsrDBReader.moveToNext();
                    }
                }
                crsrDBReader.close();
            }

            atDatabaseHelper.close();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean value) {
            txtCredits.setText(arylstAllItems.toString());
        }
    }

    public void BackFromCredits (View vw){
        Intent goBackToCredits = new Intent();
        setResult(0, goBackToCredits);
        finish();
    }
}
