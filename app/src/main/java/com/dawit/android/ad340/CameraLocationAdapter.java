package com.dawit.android.ad340;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.dawit.android.ad340.CameraLocations.Camera;

import java.util.ArrayList;

public class CameraLocationAdapter extends RecyclerView.Adapter<CameraLocationAdapter.ViewHolder> {

    private final RequestQueue mRequestQueue;
    private final ImageLoader mImageLoader;
    ArrayList<Camera> data;
        Context context;
        View view;
        ViewHolder viewHolder;

        public CameraLocationAdapter(Context context, ArrayList<Camera> data) {

            this.data = data;
            this.context = context;

            mRequestQueue = Volley.newRequestQueue(context);
            mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final NetworkImageView image;
            public TextView text;
            //public ImageView image;

            public ViewHolder(View v) {

                super(v);

                text = (TextView) v.findViewById(R.id.textView);
                image = (NetworkImageView) v.findViewById(R.id.image);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            view = LayoutInflater.from(context).inflate(R.layout.recyclerview_cameras, parent, false);

            viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            viewHolder.text.setText(data.get(position).description);
            if(data.get(position).type.equals("sdot")) {
                viewHolder.image.setImageUrl(" http://www.seattle.gov/trafficcams/images/" + data.get(position).imageURL, mImageLoader);
            }
            else{
                viewHolder.image.setImageUrl("http://images.wsdot.wa.gov/nw/" + data.get(position).imageURL, mImageLoader);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
