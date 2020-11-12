package ean.ecom.shipping.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.shipping.other.StaticValues;

import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.database.DBQuery.firebaseFirestore;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;

/**
 * Created by Shailendra (WackyCodes) on 29/09/2020 11:47
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */


public class LocationService extends Service {


    private static final String TAG = "LocationService";

    private FusedLocationProviderClient mFusedLocationClient;
    private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
    private final static long FASTEST_INTERVAL = 2000; /* 5 sec */


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("service noti")
                    .setContentText("").build();

            startForeground(1, notification);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand: called.");
        getLocation();
        return START_NOT_STICKY;
    }

    private void getLocation() {

        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
//        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText( this, "Location Permission Denied!", Toast.LENGTH_SHORT ).show();
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
            return;
        }
        Log.d(TAG, "getLocation: getting location information.");
        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        Log.d(TAG, "onLocationResult: got location result.");

                        Location location = locationResult.getLastLocation();
                        Log.d(TAG, "onLocationResult: got location.");

                        if (location != null) {
//                        User user = ((UserClient)(getApplicationContext())).getUser();
                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                             UserLocation userLocation = new UserLocation(user, geoPoint, Calendar.getInstance().getTime() );
                            Log.d(TAG, "onLocationResult: got geoPoint ." + geoPoint.toString());

                            Map<String, Object> updateMap = new HashMap <>();
                            updateMap.put( "my_geo_point", geoPoint );

                            StaticValues.MY_GEO_POINTS = geoPoint;
//                        updateMap.put( "last_location_time", Timestamp.now() );
//                            Timestamp timestamp = Timestamp.now();
                            Log.d(TAG, "onLocationResult: Updating ... .");

                            if ( currentUser != null ){
                                saveUserLocation( updateMap );
                                Log.d(TAG, "saveUserLocation: Updating ... .");
                            }else{
                                Log.d(TAG, "saveUserLocation: Null User ... .");
                            }
                        }else{
                            Toast.makeText( LocationService.this, "Location Null", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onLocationAvailability(LocationAvailability locationAvailability) {
                        super.onLocationAvailability( locationAvailability );
                    }
                },
                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
    }

    private void saveUserLocation(final Map<String, Object> updateMap ){
        if ( USER_ACCOUNT.getUser_mobile() != null)
            firebaseFirestore.collection( "DELIVERY_BOYS" )
                .document( StaticValues.USER_MOBILE )
                .update( updateMap )
                .addOnSuccessListener( new OnSuccessListener <Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onComplete: \ninserted user location into database." +
                                "\n Lat Long (Geo Point) : " + updateMap.get( "my_geo_point" ).toString());

                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed TO Update");
                    }
                } );

    }

    private void getDeviceLocation() {
        try {
            if (true) {
                Task <Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        Location location = task.getResult();
                        LatLng currentLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
//                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
//                                    DEFAULT_ZOOM);
//                            googleMap.moveCamera(update);
                    }
                } );
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

}