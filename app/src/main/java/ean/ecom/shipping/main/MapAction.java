package ean.ecom.shipping.main;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by Shailendra (WackyCodes) on 27/09/2020 20:33
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
interface MapAction {

    // Set Map Marker..
    void setMapMarker(double latitude, double longitude, String title);

    // Draw path...
    void drawPathLine(GeoPoint fromPoint, GeoPoint toPoint);

    interface OnDrawMarker{
        void onDrawingPathLine(MapAction mapAction, GeoPoint fromPoints, GeoPoint toPoints);
        void onSetMarker( MapAction mapAction, GeoPoint markPoints, String title);
    }

}
