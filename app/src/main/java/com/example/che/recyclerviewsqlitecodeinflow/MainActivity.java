package com.example.che.recyclerviewsqlitecodeinflow;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private EditText mEditeTextName;
    private GroceryAdapter mAdapter;
    private TextView mTextViewAmount;
    private int mAmount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        mEditeTextName = findViewById(R.id.edittext_name);
        mTextViewAmount = findViewById(R.id.textview_amount);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new GroceryAdapter(this, getAllItem());
        recyclerView.setAdapter(mAdapter);

        Button buttonIncreas = findViewById(R.id.button_increase);
        Button buttonDecreas = findViewById(R.id.button_decrease);
        Button buttonAdd = findViewById(R.id.button_add);

        buttonIncreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increas();
            }
        });
        buttonDecreas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreas();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }
    private void increas(){
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));
    }
    private void decreas(){
        if(mAmount > 0) {
            mAmount--;
            mTextViewAmount.setText(String.valueOf(mAmount));
        }
    }
    private void addItem(){
        if(mEditeTextName.getText().toString().trim().length() == 0 || mAmount == 0){
            return;
        }

        String name = mEditeTextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);

        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItem());

        mEditeTextName.getText().clear();
    }
    private Cursor getAllItem() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}
