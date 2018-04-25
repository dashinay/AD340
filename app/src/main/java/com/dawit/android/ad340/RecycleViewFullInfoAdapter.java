package com.dawit.android.ad340;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

public class RecycleViewFullInfoAdapter extends RecyclerView.Adapter<RecycleViewFullInfoAdapter.ViewHolder>{

    String[] items;
    Context context;
    View view1;
    ViewHolder viewHolder1;

    public RecycleViewFullInfoAdapter(Context context,String[] items){

        this.items = items;
        this.context = context;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text1;

        public ViewHolder(View v){

            super(v);

            text1 = (TextView)v.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public RecycleViewFullInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.recycleview_fullinfo,parent,false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.text1.setText(items[position]);

    }

    @Override
    public int getItemCount(){

        return items.length;
    }


}