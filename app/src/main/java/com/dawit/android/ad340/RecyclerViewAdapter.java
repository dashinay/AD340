package com.dawit.android.ad340;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    String[][] items;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView text1;
    TextView text2;
    ArrayList<String> titles;
    ArrayList<String> years;
    private int position;

    public RecyclerViewAdapter(Context context,String[][] items){

        this.items = items;
        this.context = context;
        titles = new ArrayList<String>();
        years = new ArrayList<String>();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final RecyclerViewAdapter mAdapter;
        public TextView text1;
        public TextView text2;
        Context context;
        String[][] items;
        private int position;

        public ViewHolder(View v, RecyclerViewAdapter adapter, Context context, String[][] items){

            super(v);
            this.mAdapter = adapter;
            this.context = context;
            this.items = items;
            text1 = (TextView)v.findViewById(R.id.textview1);
            text2 = (TextView)v.findViewById(R.id.textview2);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, FullInfo.class);
            String[] movie = items[position];
            intent.putExtra("Movie", movie);
            context.startActivity(intent);
        }

        public void setPosition(int position){
            this.position = position;
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.recyclerview_items,parent,false);

        viewHolder1 = new ViewHolder(view1, this, context, items);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        this.position = position;
        holder.setPosition(position);
        for(int i = 0; i < items.length; i++){
            titles.add(items[i][0]);
            years.add(items[i][1]);
        }
        holder.text1.setText(titles.get(position));
        holder.text2.setText(years.get(position));
    }

    @Override
    public int getItemCount(){

        return items.length;
    }


}