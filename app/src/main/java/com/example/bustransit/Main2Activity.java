package com.example.bustransit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {
CardView c11,c12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        c11=findViewById(R.id.c1);
        c12=findViewById(R.id.c2);
    }

    public void getStops(View view){
        Intent in=new Intent(this,HomeActivity.class);
        startActivity(in);
    }
    public void getBusses(View view){
        Intent in=new Intent(this,BusActivity.class);
        startActivity(in);
    }
}
