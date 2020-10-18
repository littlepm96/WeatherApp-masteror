package com.example.weatherapp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        ArrayList<String> cities =new ArrayList<>();
        String a=new String("rrr");
        String c = new String("su");
        String m = new String( "suino");
        String r = new String( "su");
        String t = new String("molise'nt");

        cities.add(a);
        cities.add(c);
        cities.add(m);
        cities.add(r);
        cities.add(t);

        RecyclerView recyclerView =findViewById(R.id.view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new adapter(cities));
    };





}