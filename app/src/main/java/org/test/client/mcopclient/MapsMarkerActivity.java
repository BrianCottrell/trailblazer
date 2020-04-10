package org.test.client.mcopclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.support.v7.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    public boolean tracking = false;
    private double initialLat = 0.0;
    private double initialLon = 0.0;
    private double forceLat = 0.0;
    private double forceLon = 0.0;

    private GoogleMap googleMap;
    private Marker myMarker;
    private double myLatitude = 0.0;
    private double myLongitude = 0.0;

    private double latAdj = 0.0;
    private double lonAdj = 0.0;
    private double oldLat = 0.0;
    private double oldLon = 0.0;

    private Button btn_default;
    private Button btn_red;
    private Button btn_green;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        SupportMapFragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tracking = getIntent().getBooleanExtra("TRACKING", false);
        forceLat = getIntent().getDoubleExtra("FLATITUDE", 0.0);
        forceLon = getIntent().getDoubleExtra("FLONGITUDE", 0.0);

        btn_default = (Button) findViewById(R.id.map_button_default);
        btn_red = (Button) findViewById(R.id.map_button_red);
        btn_green = (Button) findViewById(R.id.map_button_green);

        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng myLoc = new LatLng(myLatitude, myLongitude);
//                BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.token_default);
                BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("token_default",100,100));
                MarkerOptions marker = new MarkerOptions()
                        .position(myLoc)
                        .alpha(0.8f)
                        .anchor(0.5f, 0.5f)
                        .icon(bm)
                        .title("Review");
                googleMap.addMarker(marker);

            }
        });
        btn_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng myLoc = new LatLng(myLatitude, myLongitude);
//                BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.token_default);
                BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("token_green",100,100));
                MarkerOptions marker = new MarkerOptions()
                        .position(myLoc)
                        .alpha(0.8f)
                        .anchor(0.5f, 0.5f)
                        .icon(bm)
                        .title("Clear");
                googleMap.addMarker(marker);

            }
        });
        btn_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng myLoc = new LatLng(myLatitude, myLongitude);
//                BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.token_default);
                BitmapDescriptor bm = BitmapDescriptorFactory.fromBitmap(resizeMapIcons("token_red",100,100));
                MarkerOptions marker = new MarkerOptions()
                        .position(myLoc)
                        .alpha(0.8f)
                        .anchor(0.5f, 0.5f)
                        .icon(bm)
                        .title("Danger");
                googleMap.addMarker(marker);

            }
        });

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private static final int COLOR_ORANGE_ARGB = 0x30F57F17;

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // 34.042709, -118.432666 = home
        // 34.044736, -118.433889
        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(34.044736, -118.433889),
                        new LatLng(34.046736, -118.429889),
                        new LatLng(34.043736, -118.428889),
                        new LatLng(34.042736, -118.431889),
                        new LatLng(34.041736, -118.434889),
                        new LatLng(34.044736, -118.433889)));

        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon.setTag("Search Area");
        polygon.setFillColor(COLOR_ORANGE_ARGB);

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) { return; }
        }
        double curLatitude = location.getLatitude();
        double curLongitude = location.getLongitude();
        if (initialLat == 0) {
            initialLat = curLatitude;
            initialLon = curLongitude;
        }

        myLatitude = (forceLat == 0) ? curLatitude : forceLat;
        myLongitude = (forceLon == 0) ? curLongitude : forceLon;

        this.googleMap = googleMap;

        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
//        LatLng sydney = new LatLng(-33.852, 151.211);
        LatLng myLoc = new LatLng(myLatitude, myLongitude);

        BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.tracking);
        MarkerOptions marker = new MarkerOptions()
                .position(myLoc)
                .alpha((float) 0.5)
                .anchor(0.5f,0.5f)
                .icon(bm)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("My Location")
                ;
        myMarker = googleMap.addMarker(marker);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(myLoc));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 17));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateLocation();
            }
        }, 0, 2000);

    }
    public void updateLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) { return; }
        }

        if (forceLat == 0) {
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();
        } else {
            myLatitude = (location.getLatitude() - initialLat) + forceLat;
            myLongitude = (location.getLongitude() - initialLon) + forceLon;
        }

        if (tracking) {
//            double random = ThreadLocalRandom.current().nextDouble(min, max);

            double rangeMin = 0.0001;
            double rangeMax = 0.0001;
            Random r = new Random();
            latAdj += rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            lonAdj += rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            myLatitude += latAdj;
            myLongitude += lonAdj;

            double dist = Math.abs(oldLat - myLatitude) + Math.abs(oldLon - myLongitude);
//            if (dist < 0.0006) { return; }  // About 210 feet

            if (dist > 0.0006) {    // About 210 feet
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        LatLng myLoc = new LatLng(oldLat, oldLon);
                        BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.bluecircle80);
                        MarkerOptions marker = new MarkerOptions()
                                .position(myLoc)
                                .alpha((float) 0.3)
                                .anchor(0.5f, 0.5f)
                                .icon(bm)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title("Scanned");
                        googleMap.addMarker(marker);
                    }
                });

                oldLat = myLatitude;
                oldLon = myLongitude;
            }
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
//                Log.d("UI thread", "Loc: "+myLatitude+" , "+myLongitude);
                LatLng myLoc = new LatLng(myLatitude, myLongitude);
                myMarker.setPosition(myLoc);
            }
        });
    }
}
