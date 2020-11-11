package com.example.weatherapp2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Preferiti extends AppCompatActivity {

    private static final String EXTRA_MESSAGE = "com.example.weatherapp2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferiti);

        SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();

        //Apertura DB e visualizzazione del contenuto


        ArrayList<String> city = PreferitiTable.selectAll(db);


        Collections.sort(city, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        //FINE DB

        RecyclerView recyclerView = findViewById(R.id.view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterPreferiti(city));
    }

    ;

    //ELIMINA PREFERITO

    public void delpreferiti(View view) {
        ArrayList<String> strings = new ArrayList<>();

        SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();
        PreferitiTable.deleteAll(db);


        finish();
    }

    public void setpreferiti(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        TextView Text = (TextView) view;
        String message = Text.getText().toString();
        Intent data = new Intent();
        data.putExtra("key", message);

        setResult(RESULT_OK, data);
        finish();

        // intent.putExtra(EXTRA_MESSAGE, message);


    }


}

