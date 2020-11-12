package ean.ecom.shipping.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;


import java.util.ArrayList;
import java.util.List;

import ean.ecom.shipping.OnFragmentSetListener;
import ean.ecom.shipping.R;
import ean.ecom.shipping.database.DBQuery;
import ean.ecom.shipping.main.map.TaskLoadedCallback;
import ean.ecom.shipping.main.order.CurrentOrderUpdateListener;
import ean.ecom.shipping.other.ViewWeightAnimationWrapper;

import static ean.ecom.shipping.database.DBQuery.currentOrderListModelList;
import static ean.ecom.shipping.other.StaticValues.MAPVIEW_BUNDLE_KEY;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;

public class MainMapsFragment extends Fragment implements OnMapReadyCallback, MapAction
        , GoogleMap.OnInfoWindowClickListener
        , GoogleMap.OnPolylineClickListener
        , TaskLoadedCallback
        , OnFragmentSetListener
        , CurrentOrderUpdateListener {

    private final int NO_ORDER_STATE = 1;
    private final int EXTENDED_STATE = 2;
    private final int NORMAL_STATE = 3;

    public MainMapsFragment( OnFragmentSetListener mainListener) {
        this.mainListener = mainListener;
    }

    private OnFragmentSetListener mainListener;

    private MapView mMapView;
    private GoogleMap myGoogleMap;
    private GoogleApiClient wGoogleApiClient;
    private Polyline currentPolyline;
    private FusedLocationProviderClient mFusedLocationClient;

    private GeoApiContext mGeoApiContext = null;
    private ArrayList<PolylineData> polylineDataList = new ArrayList <>();
    private Marker mSelectedMarker = null;
    private ArrayList<Marker> mTripMarkers = new ArrayList <>();


    public static ShippingOrderAdaptor shippingOrderAdaptor;
    // Layout Variables...
    private RecyclerView shippingProductRecycler;
    private ImageView upDownImageBtn;
    private TextView textViewNoOrder; // no_order_text_view

    private int currentLayoutState = NORMAL_STATE;
    private ConstraintLayout mapLayout;
    private ConstraintLayout orderLayout;

    private ImageView myLocationBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_main_maps, container, false );

        mMapView = (MapView) view.findViewById( R.id.main_map_view );
        shippingProductRecycler = view.findViewById( R.id.shipping_product_recycler );
        upDownImageBtn = view.findViewById( R.id.up_image_view_btn );
        textViewNoOrder = view.findViewById( R.id.no_order_text_view );

        mapLayout = view.findViewById( R.id.map_view_const_layout );
        orderLayout = view.findViewById( R.id.current_order_const_layout );

        myLocationBtn = view.findViewById( R.id.my_location_image_view_btn );

        // Set Layout And Adaptor...
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        shippingProductRecycler.setLayoutManager( layoutManager );

        // Use this method to reduce the complexity of code...
        initGoogleMap( savedInstanceState );
        // On Button Clicks...
        onButtonClick();

        mFusedLocationClient = LocationServices
                .getFusedLocationProviderClient(getActivity());

        // hide my location Default Button ...
        View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        locationButton.setVisibility( View.INVISIBLE );

        return view;
    }

    private void initGoogleMap(@Nullable Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle( MAPVIEW_BUNDLE_KEY );
        }
        mMapView.onCreate( mapViewBundle );
        mMapView.getMapAsync( this );

        // Initiate GeoApiContext...
        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder().apiKey( getString( R.string.google_maps_key ) )
                    .build();
        }
    }

    private GeoApiContext getmGeoApiContext(){
        if(mGeoApiContext == null){
            mGeoApiContext = new GeoApiContext.Builder().apiKey( getString( R.string.google_maps_key ) )
                    .build();
            return mGeoApiContext;
        }else{
            return mGeoApiContext;
        }
    }

    private void onStartFragment(){
        // Notify..
        shippingOrderAdaptor = new ShippingOrderAdaptor( this, this, currentOrderListModelList, this );
        shippingProductRecycler.setAdapter( shippingOrderAdaptor );
        shippingOrderAdaptor.notifyDataSetChanged();

        // Get Current Order List...
        if (currentOrderListModelList.size() == 0){
            upDownImageBtn.setVisibility( View.GONE );
            textViewNoOrder.setText( "Please wait..." );
            DBQuery.getCurrentOrderList( this, USER_ACCOUNT.getUser_mobile() );
        }else{
            upDownImageBtn.setVisibility( View.VISIBLE );
            textViewNoOrder.setVisibility( View.GONE );
            shippingProductRecycler.setVisibility( View.VISIBLE );
        }
    }

    private void onButtonClick(){
        // Shipping Recycler view btn...
        upDownImageBtn.setOnClickListener( v -> {
            if (currentLayoutState != NO_ORDER_STATE){
                if (currentLayoutState == NORMAL_STATE){
                    upDownImageBtn.setImageResource( R.drawable.ic_baseline_keyboard_arrow_down_24 );
                    setCurrentLayoutState( currentLayoutState, EXTENDED_STATE );
                }else{
                    upDownImageBtn.setImageResource( R.drawable.ic_outline_keyboard_arrow_up_24 );
                    setCurrentLayoutState( currentLayoutState, NORMAL_STATE );
                }
            }
        } );

        // My Location Button Click...
        myLocationBtn.setOnClickListener( v -> setMyLocationBtn() );
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

    // Zoom Camera to the Route...
    public void zoomRoute(List<LatLng> lstLatLngRoute) {

        if (myGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        myGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }

    // Refresh The Map or Reset The Map...
    private void resetMap(){
        if(myGoogleMap != null) {
            myGoogleMap.clear();

//            if(mClusterManager != null){
//                mClusterManager.clearItems();
//            }
//
//            if (mClusterMarkers.size() > 0) {
//                mClusterMarkers.clear();
//                mClusterMarkers = new ArrayList<>();
//            }
//
            if(polylineDataList.size() > 0){
                polylineDataList.clear();
                polylineDataList = new ArrayList<>();
            }
        }
    }

    // Set Any Marker Point to locate....
    @Override
    public void setMapMarker(double latitude, double longitude, String title){
        // wBuildGoogleApiClient();
        LatLng markLatLong = new LatLng( latitude, longitude );
        if (myGoogleMap != null){
            myGoogleMap.addMarker( new MarkerOptions()
                    .position( markLatLong )
                    .title( title ) );
//            myGoogleMap.moveCamera( CameraUpdateFactory.newLatLng( markLatLong ) );
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom( markLatLong, 16 );
            myGoogleMap.animateCamera( cameraUpdate );
        }

    }

    @Override
    public void drawPathLine( @Nullable GeoPoint fromPoint,@Nullable GeoPoint toPoint, @Nullable String addressLine ) {
        // Set Custom Marker......
//        LatLng fromLocation = new LatLng( fromPoint.getLatitude(), fromPoint.getLongitude() );
//        LatLng endLocation = new LatLng( toPoint.getLatitude(), toPoint.getLongitude() );
//
//        MarkerOptions fromMarkerOp = new MarkerOptions().position( fromLocation );
//        MarkerOptions endMarkerOp = new MarkerOptions().position( endLocation );
//
//        com.google.android.gms.maps.model.Marker fromMarker = myGoogleMap.addMarker( fromMarkerOp );
//        com.google.android.gms.maps.model.Marker endMarker = myGoogleMap.addMarker( endMarkerOp );

        // Calculate Directions....
//        calculateDirections(fromMarker, endMarker );
/*
        String mapUrl = getMapUrl( fromMarkerOp.getPosition(), endMarkerOp.getPosition(), "driving" );
        GetMapData getMapData = new GetMapData( this );
        getMapData.execute( mapUrl, "driving" );

 */
        if (toPoint != null){
            /// Get Map Direction....
            onGetDirectionInMap( String.valueOf( toPoint.getLatitude() ), String.valueOf( toPoint.getLongitude() ), null);
        }else {
            onGetDirectionInMap( null, null, addressLine);
        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = myGoogleMap.addPolyline((PolylineOptions) values[0]);
    }

    // Remove Markers...
    private void removeMarker(){
        for(Marker marker : mTripMarkers){
            marker.remove();
        }
    }

    // reset Selected Markers...
    private void resetClickMarker(){
        if(mSelectedMarker != null){
            mSelectedMarker.setVisible( true );
            mSelectedMarker = null;
            removeMarker();
        }
    }

    private void setMyLocationBtn(){
        if (ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText( getContext(), "Turn ON your GPS Location !", Toast.LENGTH_SHORT ).show();
            return;
        }
        if (myGoogleMap == null){
            return;
        }

        // Set My Location....
        myGoogleMap.setMyLocationEnabled( true );
        myGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        setMyLocation();

    }

    @Override
    public void onCurrentOrderUpdates( String deliveryID, String updateStatus ) {
        if (currentOrderListModelList.size() != 0){
            for (int size = 0; size < currentOrderListModelList.size(); size ++){
                if ( deliveryID.equals( currentOrderListModelList.get( size ).getDelivery_id() )){
                    // Changes...
                    currentOrderListModelList.get( size ).setDelivery_status( updateStatus );
                    shippingOrderAdaptor.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onLoadingOrderResponse(boolean isComplete) {
        if (isComplete){
            // Initital weight....
//            mapLayout = 75;
//            orderLayout = 25;

            if (currentOrderListModelList.size() > 0){
                upDownImageBtn.setVisibility( View.VISIBLE );
                textViewNoOrder.setVisibility( View.GONE );
                if(shippingProductRecycler.getVisibility() != View.VISIBLE)
                    shippingProductRecycler.setVisibility( View.VISIBLE );
                // Adaptor...
                if (shippingOrderAdaptor != null){
                    shippingOrderAdaptor.notifyDataSetChanged();
//                    shippingOrderAdaptor = new ShippingOrderAdaptor( this, this, currentOrderListModelList, this );
//                    shippingProductRecycler.setAdapter( shippingOrderAdaptor );
                }
                // Notify..

                if (currentLayoutState == NO_ORDER_STATE || currentLayoutState == EXTENDED_STATE ){
                    setCurrentLayoutState( currentLayoutState, NORMAL_STATE );
                }
            }else{
                upDownImageBtn.setVisibility( View.GONE );
                shippingProductRecycler.setVisibility( View.GONE );
                textViewNoOrder.setVisibility( View.VISIBLE );
                textViewNoOrder.setText( "You don't have current orders" );

                setCurrentLayoutState( currentLayoutState, NO_ORDER_STATE );
            }
        }else{
            showToast( "Failed to load your orders, check your internet connection !" );
        }
    }

    // Google Map Info Window Click...
    @Override
    public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
        // If User Click on Their Location Tap
        if(marker.getSnippet().equals("Your Location")){
            resetClickMarker();
            marker.hideInfoWindow();
            mSelectedMarker = marker;
        }
        else{
            // If User Click Another Location Window...

        }


    }

    // Calculate Directions...
    private void calculateDirections(Marker fromLocation, Marker toLocation){


        Log.d("MainMapsFragment", "calculateDirections: calculating directions.");
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                fromLocation.getPosition().latitude,
                fromLocation.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest( getmGeoApiContext() );

        directions.alternatives(true);
        directions.origin( new com.google.maps.model.LatLng(
                        toLocation.getPosition().latitude,
                        toLocation.getPosition().longitude ) );

        Log.d("MainMapsFragment", "calculateDirections: destination: " + destination.toString());

        directions.destination(destination)
                .setCallback(
                new PendingResult.Callback<DirectionsResult>() {
                    @Override
                    public void onResult(DirectionsResult result) {
                        // Adding Polyline to Result...
                        addPolyLinesToMap( result );
//                        showToast("ON RESULT : " +result.toString());
                    }

                    @Override
                    public void onFailure(Throwable e) {
//                        showToast("ON FAILED: " + e.getMessage() + " : "+ e.getCause());
                        Log.e( "GET_DIRECTION","Exception: "+ e.getMessage() + " : "+ e.getLocalizedMessage() );
                    }
                });
    }

    private void addPolyLinesToMap(final DirectionsResult result) {
        // Adding Process into Main Thread...!
        new Handler( Looper.getMainLooper()).post( new Runnable() {
            @Override
            public void run() {
                Log.d("MainMapsFragment", "run: result routes: " + result.routes.length);
                double timeDuration = 0;
                // To Remover Duplicate Data...
                polylineDataList.clear();
                // Adding All The Route in the List...
                for(DirectionsRoute route: result.routes){
                    Log.d("MainMapsFragment", "run: leg: " + route.legs[0].toString());
                    List <com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();
                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){
                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }

                    Polyline polyline = myGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor( ContextCompat.getColor(getActivity(), R.color.colorGray));
                    polyline.setClickable(true);
                    polylineDataList.add( new PolylineData( polyline, route.legs[0] ) );
                    double tempDuration = route.legs[0].duration.inSeconds;
                    // Assign Max for Once...
                    if (timeDuration < 1){
                        timeDuration = 1 + tempDuration;
                    }
                    // Logic to Calculate Shortest path
                    if ( tempDuration < timeDuration ){
                        timeDuration = tempDuration;
                        // Add Default Line ...
                        onPolylineClick( polyline );
                        // Zooming Camera to the Route...
                        zoomRoute( polyline.getPoints() );
                    }

                    // Hide selected Marker...
                    mSelectedMarker.setVisible( false );
                }
            }
        });
    }

    // Here We have A Method, Iff user Click on Any Route...
    @Override
    public void onPolylineClick(Polyline polyline) {

        for(PolylineData polylineData: polylineDataList){
            Log.d("MainMapsFragment", "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                polylineData.getPolyline().setZIndex(1);

                // Set Custom Marker......
                LatLng endLocation = new LatLng(
                    polylineData.getLeg().endLocation.lat,
                    polylineData.getLeg().endLocation.lng
                );
                com.google.android.gms.maps.model.Marker marker = myGoogleMap.addMarker( new MarkerOptions()
                    .position( endLocation )
                    .title( "Distance :" + polylineData.getLeg().distance )
                    .snippet( "Duration : "+ polylineData.getLeg().duration ));
                marker.showInfoWindow();

                // Adding Markers in the Marker List...
                mTripMarkers.add( marker );
            }
            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getActivity(), R.color.colorGray));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }

    private void onGetDirectionInMap(@Nullable String latitude,@Nullable String longitude, @Nullable String addressLine){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Want to open Google Map?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> {
//                    String latitude = String.valueOf(marker.getPosition().latitude);
//                    String longitude = String.valueOf(marker.getPosition().longitude);
                    Uri gmmIntentUri;

                    if (addressLine != null){
                        gmmIntentUri = Uri.parse("google.navigation:q=" + addressLine);
                        //Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
                    }else {
                        gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                    }

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    try{
                        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }catch (NullPointerException e){
//                                Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage() );
                        mainListener.showToast(  "Couldn't open map! Not found Map Application." );
                    }
                    dialog.dismiss();
                } )
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss() );
        final AlertDialog alert = builder.create();
        alert.show();
    }


    // ========= Google Maps and Methods...====================================================
    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("OnMapReady", "Map is Ready!");
        myGoogleMap = map;
//        setMyMap( 0, 0 );
//        map.addMarker( new MarkerOptions().position( new LatLng( 0, 0 ) ).title( "Marker" ) );
        setMyLocationBtn();
        // Set On Info Window Click Active..
        myGoogleMap.setOnInfoWindowClickListener( this );
        // Set On PolyLine Click Listener...
        myGoogleMap.setOnPolylineClickListener( this );

    }

    @Override
    public void onResume() {

        onLoadingOrderResponse(true);

        setMyLocationBtn();

        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onStart() {
        setMyLocationBtn();
        super.onStart();
        mMapView.onStart();
        // on Set Fragment...
        onStartFragment();
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

    // Get Last Location...
    private void setMyLocation() {
        try {
            Task <Location> locationResult = mFusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener( task -> {
                if (task.isSuccessful()) {
                    try{
                        // Set the map's camera position to the current location of the device.
                        Location location = task.getResult();
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 18);
//                    myGoogleMap.moveCamera(update);
                        myGoogleMap.animateCamera( update );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(String msg) {
        mainListener.showToast( msg );
    }


    private String getMapUrl(LatLng from, LatLng end, String mode){
        // Origin of route
        String str_origin = "origin=" + from.latitude + "," + from.longitude;
        // Destination of route
        String str_dest = "destination=" + end.latitude + "," + end.longitude;
        // Mode
        String modes = "mode=" + mode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + modes;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }


    @Override
    public void setTitle(String title) {
        mainListener.setTitle( title );
    }

    @Override
    public void onBackPressed(int From, String backTitle) {

    }

    @Override
    public void setNextFragment(Fragment fragment) {
        mainListener.setNextFragment( fragment );
    }

    // Change Height ...
    private void updateWeight(ConstraintLayout firstLayout, int in1, int fin1, ConstraintLayout secondLayout, int in2, int fin2 ){
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(firstLayout);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                in1, // Initial
                fin1); // Target
        mapAnimation.setDuration(600);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(secondLayout);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                in2,
                fin2);
        recyclerAnimation.setDuration(600);

        mapAnimation.start();
        recyclerAnimation.start();
    }

    private void setCurrentLayoutState( int currentStage, int targetStage ){
        switch (currentStage){
            case NORMAL_STATE :
                switch (targetStage){
                    case EXTENDED_STATE :// if user extends...
                        updateWeight( mapLayout, 75, 25, orderLayout, 25, 75 );
                        break;
                    case NO_ORDER_STATE : // If No Order are exist...
                        updateWeight( mapLayout, 75, 90, orderLayout, 25, 10 );
                        break;
                    default: break;
                }
                break;
            case EXTENDED_STATE :
                switch (targetStage){
                    case NORMAL_STATE : // if user compressed...
                        updateWeight( mapLayout, 25, 75, orderLayout, 75, 25 );
                        break;
                    case NO_ORDER_STATE :  // If No Order are exist...
                        updateWeight( mapLayout, 25, 90, orderLayout, 75, 10 );
                        break;
                    default: break;
                }
                break;
            case NO_ORDER_STATE :
                switch (targetStage){
                    case NORMAL_STATE :// if user compressed...
                        updateWeight( mapLayout, 90, 75, orderLayout, 10, 25 );
                        break;
                    case EXTENDED_STATE :  // if user extends...
                        updateWeight( mapLayout, 90, 20, orderLayout, 10, 75 );
                        break;
                    default: break;
                }
                break;
            default: break;
        }
        currentLayoutState = targetStage;
    }




}