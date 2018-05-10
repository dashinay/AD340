package com.dawit.android.ad340;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.dawit.android.ad340.MESSAGE";
    SharedPreferences sharedPref;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("AD340 Application");
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()){
                            case R.id.nav_movies:
                                Intent intent= new Intent(getApplicationContext(), MovieList.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_about:
                                Intent intent2= new Intent(getApplicationContext(), About.class);
                                startActivity(intent2);
                                break;
                        }
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24px);

        //get the previously stored entry
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "";
        String savedInfo = sharedPref.getString(getString(R.string.main_shared_pref), defaultValue);
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(savedInfo);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        //prevent from navigating to different activity
        if(!message.isEmpty()) {
            //save the entry before
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.main_shared_pref), message);
            editor.commit();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        Toast.makeText(getApplicationContext(),"Please enter the value", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
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