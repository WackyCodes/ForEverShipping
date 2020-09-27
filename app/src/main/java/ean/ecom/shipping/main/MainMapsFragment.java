package ean.ecom.shipping.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import ean.ecom.shipping.R;

import static ean.ecom.shipping.other.StaticValues.MAPVIEW_BUNDLE_KEY;

public class MainMapsFragment extends Fragment implements OnMapReadyCallback, MapAction {

    private MapView mMapView;
    private GoogleMap myGoogleMap;
    private GoogleApiClient wGoogleApiClient;
    Location wLocation;
    LocationRequest wLocationRequest;

    // Layout Variables...
    private RecyclerView shippingProductRecycler;
    private ImageView upDownImageBtn;

    private ImageView myLocationBtn;

    //
    private boolean isUp = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_main_maps, container, false );

        mMapView = (MapView) view.findViewById( R.id.main_map_view );
        shippingProductRecycler = view.findViewById( R.id.shipping_product_recycler );
        upDownImageBtn = view.findViewById( R.id.up_image_view_btn );

        myLocationBtn = view.findViewById( R.id.my_location_image_view_btn );

        // Set Layout And Adaptor...
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        shippingProductRecycler.setLayoutManager( layoutManager );
        // Adaptor...
        ShippingOrderAdaptor adaptor = new ShippingOrderAdaptor( new ArrayList <OrderDetailsModel>(), this ); // TODO : ADD LIST
        shippingProductRecycler.setAdapter( adaptor );
        // Notify..
        adaptor.notifyDataSetChanged();


        // Use this method to reduce the complexity of code...
        initGoogleMap( savedInstanceState );
        // On Button Clicks...
        onButtonClick();


        return view;
    }

    private void initGoogleMap(@Nullable Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle( MAPVIEW_BUNDLE_KEY );
        }
        mMapView.onCreate( mapViewBundle );
        mMapView.getMapAsync( this );
    }

    private void onButtonClick(){
        // Shipping Recycler view btn...
        upDownImageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUp = !isUp;
                upDownImageBtn.setEnabled( false );
                if (isUp){
                    upDownImageBtn.setImageResource( R.drawable.ic_baseline_keyboard_arrow_down_24 );
                    // TODO : Set Layout
                }else{
                    upDownImageBtn.setImageResource( R.drawable.ic_outline_keyboard_arrow_up_24 );
                    // TODO : Set Layout
                }
                upDownImageBtn.setEnabled( true );
            }
        } );

        // My Location Button Click...
        myLocationBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyLocationBtn();
            }
        } );

    }

 /**   @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById( R.id.map_view_fragment );
//        if (mapFragment != null) {
//            mapFragment.getMapAsync( callback );
//        }
    }
  */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );

        Bundle mapViewBundle = outState.getBundle( MAPVIEW_BUNDLE_KEY );
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle( MAPVIEW_BUNDLE_KEY, mapViewBundle );
        }

        mMapView.onSaveInstanceState( mapViewBundle );
    }
    // ========= Private Local Use Methods ====================================================
    // Set Any Marker Point to locate....
    @Override
    public void setMapMarker(double latitude, double longitude){
        // wBuildGoogleApiClient();
        LatLng sydney = new LatLng( latitude, longitude );
        if (myGoogleMap != null){
            myGoogleMap.addMarker( new MarkerOptions().position( sydney ).title( "Marker in Sydney" ) );
            myGoogleMap.moveCamera( CameraUpdateFactory.newLatLng( sydney ) );
        }
    }

    @Override
    public void drawPathLine(GeoPoint fromPoint, GeoPoint toPoint) {
        // Draw Line...
    }

    private void setMyLocationBtn(){
        if (ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText( getContext(), "Turn ON your GPS Location !", Toast.LENGTH_SHORT ).show();
            return;
        }
        if (myGoogleMap!=null) {
            myGoogleMap.setMyLocationEnabled( true );
            // Set Marker...
//            setMapMarker( myGoogleMap.getMyLocation().getLatitude(), myGoogleMap.getMyLocation().getLongitude() );
        }

    }

    // ========= Google Maps and Methods...====================================================
    @Override
    public void onMapReady(GoogleMap map) {
        myGoogleMap = map;
//        setMyMap( 0, 0 );

//        map.addMarker( new MarkerOptions().position( new LatLng( 0, 0 ) ).title( "Marker" ) );
        setMyLocationBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

// ========= Google Maps and Methods...====================================================







}