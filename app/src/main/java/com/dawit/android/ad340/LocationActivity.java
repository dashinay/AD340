package com.dawit.android.ad340;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private Location locationValue;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<CameraLocations.Camera> cameraList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        cameraList = new ArrayList<>();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        startDownload();

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationValue = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (locationValue != null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        else {
            new AlertDialog.Builder(LocationActivity.this)
                    .setTitle("Please activate location")
                    .setMessage("Click ok to goto settings else exit.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .show();
        }
    }

    private void startDownload() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2",null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateFromDownload(response);
                mGoogleApiClient.connect();
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
                    cameraList.add(new CameraLocations.Camera(camera.getString("Description"), camera.getString("ImageUrl"),
                            camera.getString("Type"), latLng));
                }
            }
        } catch(JSONException e){
            Log.d("Error", e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latLng = new LatLng(locationValue.getLatitude(), locationValue.getLongitude());
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Your location"));

        int index = 0;
        for(CameraLocations.Camera camera: cameraList){
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(camera.latLng).title(camera.description));
            marker.setZIndex(index++);

        }
        final Context context = getApplicationContext();
        map.setInfoWindowAdapter(new MyCustomInfoViewAdapter(this, cameraList));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.showInfoWindow();
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();
                    return true;
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
