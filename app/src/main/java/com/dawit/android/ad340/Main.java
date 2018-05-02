package com.dawit.android.ad340;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void leftTop(View view){
        Intent intent = new Intent(this, MovieList.class);
        startActivity(intent);
    }

    public void rightTop(View view){
        Toast.makeText(getApplicationContext(), "Right top button.", Toast.LENGTH_SHORT).show();
    }

    public void leftBottom(View view){
        Toast.makeText(getApplicationContext(), "Left bottom button.", Toast.LENGTH_SHORT).show();
    }

    public void rightBottom(View view){
        Toast.makeText(getApplicationContext(), "right bottom button.", Toast.LENGTH_SHORT).show();
    }
}
