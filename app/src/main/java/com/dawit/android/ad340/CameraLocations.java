package com.dawit.android.ad340;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CameraLocations extends FragmentActivity{
    private RecyclerView recyclerView;
    private LinearLayoutManager recylerViewLayoutManager;
    private CameraLocationAdapter recyclerViewAdapter;
    private ArrayList<Camera> cameraList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_locations);

        cameraList = new ArrayList<>();

        recyclerView = findViewById(R.id.my_recycler_view);
        recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerViewAdapter = new CameraLocationAdapter(getApplicationContext(), cameraList);

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkInfo info = getActiveNetworkInfo();
        Toast.makeText(getApplicationContext(), info.getState().toString(), Toast.LENGTH_LONG).show();
        startDownload();
    }

    private void startDownload() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2",null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        updateFromDownload(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    public void updateFromDownload(JSONObject json) {
        JSONObject camera;
        try {
            JSONArray reader = json.getJSONArray("Features");
            for (int i = 0; i < reader.length(); i++) {
                JSONObject feature = reader.getJSONObject(i);
                JSONArray coordinate = feature.getJSONArray("PointCoordinate");
                LatLng latLng = new LatLng(coordinate.getDouble(0), coordinate.getDouble(1));
                JSONArray cameras = feature.getJSONArray("Cameras");
                for (int j = 0; j < cameras.length(); j++) {
                    camera = cameras.getJSONObject(j);
                    cameraList.add(new Camera(camera.getString("Description"), camera.getString("ImageUrl"),
                            camera.getString("Type"), latLng));
                }
            }
            } catch(JSONException e){
                Log.d("Error", e.getMessage());
            }

        recyclerViewAdapter.notifyDataSetChanged();
        }

    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    static class Camera{
        String description;
        String imageURL;
        String type;
        LatLng latLng;

        public Camera(String description, String imageURL, String type, LatLng latLng) {
            this.description = description;
            this.imageURL = imageURL;
            this.type = type;
            this.latLng = latLng;
        }
    }
}
