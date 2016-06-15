package com.example.mrb.asynctaskdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public EditText edtxtNewItem;
    private Button btnAddItem;
    private Button btnShowItems;
    private TextView txtvwOutput;
    private EditText edttxtNewHighscore;
    private EditText edttxtNewHighscoreA;
    PlaceToDraw drawBox;
    private Button btnGo;
    boolean blnGoingRight = false;
    boolean blnStop = false;
    int intScore;
    int intSpeed =4 ;
    private Button btnStop;

    private Button btnGoToMain;
    private EditText edttxtName;
    private TextView txtTitle;
    public String strUsername;
    String strScore;
    TitlePage titlePage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtxtNewItem = (EditText) findViewById(R.id.edtxtNewItem);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnShowItems = (Button) findViewById(R.id.btnShowItems);
        txtvwOutput = (TextView) findViewById(R.id.txtvwOutput);
        drawBox = (PlaceToDraw) findViewById(R.id.drawBox);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnGo = (Button) findViewById(R.id.btnGo);

        btnGoToMain = (Button) findViewById(R.id.btnLetsPlay);
        edttxtName = (EditText) findViewById(R.id.edttxtUsername);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
    }

    public void GamePage (View vw){
        strUsername = edttxtName.getText().toString();
        setContentView(R.layout.activity_main);
    }

    public void addNewItem(View vw) {
        new ItemAdder().execute(edtxtNewItem.getText().toString());
    }


    public void deleteAllItems(View vw) {
        new DeleteItems().execute();
    }

    public void showAllItems(View vw) {
        new ShowItems().execute();
    }

    public void showAllHighscore(View vw) {
        new showHighscore().execute();
    }

    public void AddNameAndScore(View vw) {
        strUsername = edtxtNewItem.getText().toString();
        edtxtNewItem.setText("");
        strScore.valueOf(intScore);

        ATDatabaseHelper atDatabaseHelper = new ATDatabaseHelper(this,null,null,0);
        SQLiteDatabase db;
        db = atDatabaseHelper.getWritableDatabase();
        db.close();
        new NameAndScore().execute(strScore);
    }



    public class NameAndScore extends AsyncTask<String, String, Boolean> {

        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private ContentValues cntntVals;


        @Override
        protected void onPreExecute() {
            atDatabaseHelper = new ATDatabaseHelper(MainActivity.this, null, null, 0);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String strScoreToAdd = params[0];
            cntntVals = new ContentValues();



            cntntVals.put("ITEM", strUsername);
            cntntVals.put("HIGHSCORE", strScoreToAdd);

            try {
                db = atDatabaseHelper.getWritableDatabase();
            } catch (SQLiteException sqlEx) {
                return false;
            }

            if (strScoreToAdd.length() == 0) {
                publishProgress("You must enter a value to add a new item.");
                return false;
            } else {

                atDatabaseHelper.insertElement(db, cntntVals);
                publishProgress("Item has been added to the database.");
            }
            db.close();
            atDatabaseHelper.close();
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            txtvwOutput.setText(values[0]);
        }
    }


    private class ItemAdder extends AsyncTask<String, String, Boolean> {
        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private ContentValues cntntVals;

        @Override
        protected void onPreExecute() {
            atDatabaseHelper = new ATDatabaseHelper(MainActivity.this, null, null, 0);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String strItemToAdd = params[0];

            cntntVals = new ContentValues();

            cntntVals.put("ITEM", strItemToAdd);

            try {
                db = atDatabaseHelper.getWritableDatabase();
            } catch (SQLiteException sqlEx) {
                return false;
            }

            if (strItemToAdd.length() == 0) {
                publishProgress("You must enter a value to add a new item.");
                return false;
            } else {
                atDatabaseHelper.insertElement(db, cntntVals);
                publishProgress("Item has been added to the database.");
            }

            atDatabaseHelper.close();
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            txtvwOutput.setText(values[0]);
        }
    }

    private class showHighscore extends AsyncTask<Void, String, Boolean> {
        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private Cursor crsrDBReader;
        private ArrayList<String> arylstAllItems;

        @Override
        protected void onPreExecute() {
            atDatabaseHelper = new ATDatabaseHelper(MainActivity.this, null, null, 0);
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
            txtvwOutput.setText(arylstAllItems.toString());
        }
    }

    private class ShowItems extends AsyncTask<Void, String, Boolean> {
        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private Cursor crsrDBReader;
        private ArrayList<String> arylstAllItems;

        @Override
        protected void onPreExecute() {
            atDatabaseHelper = new ATDatabaseHelper(MainActivity.this, null, null, 0);
            arylstAllItems = new ArrayList<String>();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                db = atDatabaseHelper.getReadableDatabase();
            } catch (SQLiteException sqlEx) {
                return false;
            }

            crsrDBReader = db.rawQuery("SELECT * FROM LIST", null);


            if (crsrDBReader != null) {
                if (crsrDBReader.moveToFirst()) {
                    while (crsrDBReader.isAfterLast() == false) {
                        String strItem = crsrDBReader.getString(crsrDBReader.getColumnIndex("ITEM"));

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
            txtvwOutput.setText(arylstAllItems.toString());
        }

    }

    private class DeleteItems extends AsyncTask<Void, String, Boolean> {
        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private Cursor crsrDBReader;
        private ArrayList<String> arylstAllItems;
        int numRowsDeleted;

        @Override
        protected void onPreExecute() {
            atDatabaseHelper = new ATDatabaseHelper(MainActivity.this, null, null, 0);
            arylstAllItems = new ArrayList<String>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                db = atDatabaseHelper.getReadableDatabase();
            } catch (SQLiteException sqlEx) {
                return false;
            }

            crsrDBReader = db.rawQuery("SELECT * FROM LIST", null);

            if (crsrDBReader != null) {
                if (crsrDBReader.moveToFirst()) {
                    while (crsrDBReader.isAfterLast() == false) {
                        String strItem = crsrDBReader.getString(crsrDBReader.getColumnIndex("ITEM"));
                        arylstAllItems.add(strItem);

                        crsrDBReader.moveToNext();
                    }
                }
                crsrDBReader.close();
            }
            try {
                numRowsDeleted = atDatabaseHelper.deleteElement(db, null, null);
                atDatabaseHelper.close();
            } catch (SQLiteException e) {
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean value) {
            txtvwOutput.setText(arylstAllItems.toString() + "have been deleted");
        }

    }

    public void onStop(View v) {
        if (btnStop.getText().toString().equals("PLAY AGAIN") ){

            btnStop.setText("Pause");
            btnGo.setText("GO");
            txtvwOutput.setText("Score:");
            intScore = 0;
            btnGo.setEnabled(true);
            drawBox.reset();
        }
        if (blnStop) {
            blnStop = false;
        } else {
            blnStop = true;
        }
    }

    public void onClick(View v) {
        new AnimationTask().execute();
    }

    private class AnimationTask extends AsyncTask<Void, Void, Void> {
        // NOTE: Android now has dedicated classes for handling different types of Animations.
        //       The Code here does work but is not optimal for a modern app.

        boolean blnAnimationDone;


        @Override
        protected void onPreExecute() {
            blnAnimationDone = false;
            if (blnGoingRight == true) {
                blnGoingRight = false;
            } else {
                blnGoingRight = true;
            }

            if (blnStop) {
                blnStop = false;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!blnAnimationDone) {
                // The Code below forces the background thread to wait 10 milliseconds
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                }
                if (blnStop == false)// The Code below calls the onProgressUpdate() method
                {
                    publishProgress();
                }
            }
            
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... params) {
            if (blnGoingRight == true) {
                // If the right edge of the rectangle is <= the width of drawBox
                if (drawBox.getFltRight() <= drawBox.getWidth()) {
                    drawBox.moveRight(intSpeed);
                    drawBox.Falling(intSpeed);

                    if (drawBox.getFallDown() >= drawBox.getHeight()){
                        Random rand = new Random();
                        int n = rand.nextInt(300) + 50;
                        drawBox.RandomPosition(n);
                        intScore++;
                        txtvwOutput.setText("Score:" + intScore);
                        if (intScore % 10 ==1) {
                            intSpeed += 3;
                        }
                    }
                } else {
                    blnAnimationDone = true;
                }
            } else {

                if (drawBox.getFltLeft() >= 0) {
                    drawBox.moveLeft(intSpeed);
                    drawBox.Falling(intSpeed);
                    if (drawBox.getFallDown() >= drawBox.getHeight()){
                        Random rand = new Random();
                        int n = rand.nextInt(600) + 50;
                        drawBox.RandomPosition(n);
                        intScore++;
                        txtvwOutput.setText("Score:" + intScore);
                        if (intScore % 10 ==1) {
                            intSpeed += 3;
                        }
                    }
                } else {
                    blnAnimationDone = true;
                }
            }
            if (drawBox.getRndNumber()-25 <= drawBox.getBlackBoxInt()+25 && (drawBox.getRndNumber()-25>=drawBox.getBlackBoxInt()-25)
                    || (drawBox.getRndNumber()+25>= drawBox.getBlackBoxInt()-25) &&(drawBox.getRndNumber()+25<= drawBox.getBlackBoxInt()+25))
            {
                if(drawBox.getFallDown()+ 25> 400 && (drawBox.getFallDown() + 25 <450) || (drawBox.getFallDown()-25 > 400 && drawBox.getFallDown()-25<450)) {
                    blnStop = true;
                    blnAnimationDone = true;
                    txtvwOutput.setText("GAME OVER! Score:" + intScore);

                    btnGo.setText("GAME OVER!");
                    btnGo.setEnabled(false);

                    btnStop.setText("PLAY AGAIN");

                    intSpeed = 4;

                }
            }


        }
    }



}