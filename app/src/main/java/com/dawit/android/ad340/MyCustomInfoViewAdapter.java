package com.dawit.android.ad340;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

class MyCustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mymarkerview;
    private final RequestQueue mRequestQueue;
    private final ImageLoader mImageLoader;
    private Context context;
    private ArrayList<CameraLocations.Camera> cameraList;

    MyCustomInfoViewAdapter(Context context, ArrayList cameraList) {
        this.context = context;
        this.cameraList = cameraList;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        mymarkerview = layoutInflater
                .inflate(R.layout.recyclerview_cameras, null);

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

    public View getInfoWindow(Marker marker) {
        render(marker, mymarkerview);
        return mymarkerview;
    }

    public View getInfoContents(Marker marker) {
        return null;
    }

    private void render(Marker marker, View view) {
        NetworkImageView image = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.textView);

        int index = (int)marker.getZIndex();
        //for(int i = 0; i < cameraList.size(); i ++){
            //if(cameraList.get(i).description.equals(marker.getTitle())){
                textView.setText(cameraList.get(index).description);
                if(cameraList.get(index).type.equals("sdot")) {
                    image.setImageUrl(" http://www.seattle.gov/trafficcams/images/" + cameraList
                            .get(index).imageURL, mImageLoader);
                }
                else{
                    image.setImageUrl("http://images.wsdot.wa.gov/nw/" + cameraList
                            .get(index).imageURL, mImageLoader);
                }
                image.isInLayout();
            //}
        //}

    }
}
