package com.example.mrb.asynctaskdb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TitlePage extends MainActivity {
    public Button btnLetsPlay;
    public EditText edttxtUsername;
    public TextView txtTitle;
    String strUsername;
    public Intent goToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_page);
        btnLetsPlay = (Button)findViewById(R.id.btnLetsPlay);
        edttxtUsername = (EditText) findViewById(R.id.edttxtUsername);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        goToMain = new Intent (TitlePage.this,MainActivity.class);
    }

    public void GamePage (View vw){

         new DbInsert().execute();
    }

    public void Gamepage1(View vw) {
        strUsername = edttxtUsername.getText().toString();
        new DbInsert().execute(strUsername);

    }

    public class DbInsert extends AsyncTask<String,String,Boolean> {

        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private ContentValues name;

        @Override
        protected void onPreExecute(){
            atDatabaseHelper = new ATDatabaseHelper(TitlePage.this,null,null,0);
        }


        @Override
        protected Boolean doInBackground(String... params) {
            if (strUsername.length() == 0){
                publishProgress("Please enter a name");
                return false;
            }else{
                publishProgress("Name has been added");
            }

            return true;
        }

        protected void onPostExecute (Boolean result){
            if (result){

                goToMain.putExtra("name",strUsername);
                txtTitle.setText("");
                startActivity(goToMain);
            }
        }

        protected void onProgressUpdate (String... values){txtTitle.setText(values[0]);}
    }
}
