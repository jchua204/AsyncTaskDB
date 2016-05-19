package com.example.mrb.asynctaskdb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    private EditText edtxtNewItem;
    private Button btnAddItem;
    private Button btnShowItems;
    private TextView txtvwOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtxtNewItem = (EditText) findViewById(R.id.edtxtNewItem);
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnShowItems = (Button) findViewById(R.id.btnShowItems);
        txtvwOutput = (TextView) findViewById(R.id.txtvwOutput);

    }

    public void addNewItem(View vw)
    {
        new ItemAdder().execute(edtxtNewItem.getText().toString());

    }

    public void showAllItems(View vw)
    {
        new ShowItems().execute();
    }

    private class ItemAdder extends AsyncTask<String, String, Boolean>
    {
        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private ContentValues cntntVals;

        @Override
        protected void onPreExecute()
        {
            atDatabaseHelper = new ATDatabaseHelper(MainActivity.this, null, null, 0);
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            String strItemToAdd = params[0];

            cntntVals = new ContentValues();

            cntntVals.put("ITEM", strItemToAdd);

            try
            {
                db = atDatabaseHelper.getWritableDatabase();
            }
            catch(SQLiteException sqlEx)
            {
                return false;
            }

            if (strItemToAdd.length() == 0)
            {
                publishProgress("You must enter a value to add a new item.");
                return false;
            }
            else
            {
                atDatabaseHelper.insertElement(db, cntntVals);
                publishProgress("Item has been added to the database.");
            }

            atDatabaseHelper.close();
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            txtvwOutput.setText(values[0]);
        }
    }

    private class ShowItems extends AsyncTask<Void, String, Boolean>
    {
        private SQLiteDatabase db;
        private ATDatabaseHelper atDatabaseHelper;
        private Cursor crsrDBReader;
        private ArrayList<String> arylstAllItems;

        @Override
        protected void onPreExecute()
        {
            atDatabaseHelper = new ATDatabaseHelper(MainActivity.this, null, null, 0);
            arylstAllItems = new ArrayList<String>();
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                db = atDatabaseHelper.getReadableDatabase();
            }
            catch(SQLiteException sqlEx)
            {
                return false;
            }

            crsrDBReader = db.rawQuery("SELECT * FROM LIST", null);

            if(crsrDBReader != null)
            {
                if(crsrDBReader.moveToFirst())
                {
                    while(crsrDBReader.isAfterLast() == false)
                    {
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
        protected void onPostExecute(Boolean value)
        {
            txtvwOutput.setText(arylstAllItems.toString());
        }

    }

}
