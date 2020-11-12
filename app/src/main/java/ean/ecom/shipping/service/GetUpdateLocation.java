package ean.ecom.shipping.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Shailendra (WackyCodes) on 28/10/2020 09:36
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class GetUpdateLocation extends Service {

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void getDeviceLocation() {
        try {
            if (true) {
                Task <Location> locationResult = mFusedLocationProviderClient.getLastLocation();
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

    private void updateLocationUI() {
       /** if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
               /*
                if (myGoogleMap!=null) {
            myGoogleMap.setMyLocationEnabled( true );
            // Set Marker...
            try{
//                setMapMarker( myGoogleMap.getMyLocation().getLatitude(), myGoogleMap.getMyLocation().getLongitude(), "My Location" );
                LatLng markLatLong = new LatLng( myGoogleMap.getMyLocation().getLatitude(), myGoogleMap.getMyLocation().getLatitude() );
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom( markLatLong, 16 );
                myGoogleMap.animateCamera( cameraUpdate );

            }catch (Exception e){
                e.printStackTrace();
            }

            // Move Camera...
            myGoogleMap.setOnMyLocationChangeListener( location -> {
                // Set Marker...
                try{
                    LatLng markLatLong = new LatLng(  location.getLatitude(), location.getLongitude() );
                    myGoogleMap.moveCamera( CameraUpdateFactory.newLatLng( markLatLong ) );
                }catch (Exception e){
                    e.printStackTrace();
                }
            } );
        }
        else{
            Toast.makeText( getContext(), "Preparing...", Toast.LENGTH_SHORT ).show();
        }

            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        } */
    }



}
