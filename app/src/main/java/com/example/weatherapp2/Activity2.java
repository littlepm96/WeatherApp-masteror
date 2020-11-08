package com.example.weatherapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Activity2 extends AppCompatActivity {

    private static final String EXTRA_MESSAGE ="com.example.weatherapp2" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

                SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();

        //Apertura DB e visualizzazione del contenuto


        ArrayList<String> city=  preferitiTable.selectAll(db);



        Collections.sort(city, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

                                //FINE DB

        RecyclerView recyclerView =findViewById(R.id.view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new adapter(city));
    };

                //ELIMINA PREFERITO

    public void delpreferiti(View view) {
        ArrayList<String> strings=new ArrayList<>();

        SQLiteDatabase db = MyDatabase.getInstance(getApplicationContext()).getWritableDatabase();
        preferitiTable.deleteAll(db);


        finish();
    }

    public void setpreferiti(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        TextView Text = (TextView) view;
        String message = Text.getText().toString();
        Intent data = new Intent();
        data.putExtra("key",message);

        setResult(RESULT_OK,data);
        finish();

       // intent.putExtra(EXTRA_MESSAGE, message);



    }

    

}

