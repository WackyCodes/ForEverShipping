package ean.ecom.shipping.main;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by Shailendra (WackyCodes) on 27/09/2020 20:33
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
interface MapAction {

    // Set Map Marker..
    void setMapMarker(double latitude, double longitude, String title);

    // Draw path...
    void drawPathLine(@Nullable GeoPoint fromPoint,@Nullable  GeoPoint toPoint, @Nullable String addressLine);

    interface OnDrawMarker{
        void onDrawingPathLine(MapAction mapAction, GeoPoint fromPoints, GeoPoint toPoints, @Nullable String addressLine);
        void onSetMarker( MapAction mapAction, GeoPoint markPoints, String title);
    }

}
