package com.dawit.android.ad340;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ZombieLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zombie_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Zombie Layout");
        actionbar.setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                Toast.makeText(getApplicationContext(), "You clicked the setting button.", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
}
