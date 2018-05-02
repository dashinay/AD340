package com.dawit.android.ad340;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

public class FullInfo extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager recylerViewLayoutManager;
    RecycleViewFullInfoAdapter recyclerViewAdapter;
    Context context;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_info);
        Intent intent = getIntent();
        String[] movie = intent.getStringArrayExtra("Movie");

        context = getApplicationContext();

        relativeLayout = findViewById(R.id.relativelayout);

        recyclerView = findViewById(R.id.my_recycler_view);

        recylerViewLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(recylerViewLayoutManager);

        recyclerViewAdapter = new RecycleViewFullInfoAdapter(context, movie);

        recyclerView.setAdapter(recyclerViewAdapter);

    }
}
