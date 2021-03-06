package ean.ecom.shipping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;
import ean.ecom.shipping.database.DBQuery;
import ean.ecom.shipping.main.MainMapsFragment;
import ean.ecom.shipping.profile.myorder.MyOrdersActivity;
import ean.ecom.shipping.service.LocationService;

import static ean.ecom.shipping.database.DBQuery.currentOrderListModelList;
import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.other.StaticValues.ERROR_DIALOG_REQUEST;
import static ean.ecom.shipping.other.StaticValues.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static ean.ecom.shipping.other.StaticValues.PERMISSIONS_REQUEST_ENABLE_GPS;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;
import static ean.ecom.shipping.other.StaticValues.mapServicePackage;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentSetListener {
    private String TAG = "MainActivity";

    private FragmentManager mainActivityManager;

    // FrameLayout...
    private FrameLayout mainFrameLayout;
    private FrameLayout deliveryProductListFrameLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static TextView badgeNotifyCount;
    public static TextView badgeOrderCount;
    // Drawer...User info
    public static CircleImageView drawerImage;
    public static TextView drawerName;
    public static TextView drawerEmail;
    public static LinearLayout drawerCityLayout;
    public static TextView drawerCityTitle;
    public static TextView drawerCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        // Assign manager...
        mainActivityManager = getSupportFragmentManager();

        toolbar = findViewById( R.id.appToolbar );
        drawer = findViewById( R.id.drawer_layout );
        navigationView = findViewById( R.id.nav_view );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( "4Ever Mall" );
            getSupportActionBar().setSubtitle( "" );
        }catch (NullPointerException ignored){ }

        // setNavigationItemSelectedListener()...
        navigationView.setNavigationItemSelectedListener( MainActivity.this );
        navigationView.getMenu().getItem( 0 ).setChecked( true );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this,drawer,toolbar,
                R.string.navigation_Drawer_Open,R.string.navigation_Drawer_close);
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        // Drawer Variable assign...
        drawerName = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_UserName );
        drawerEmail = navigationView.getHeaderView( 0 ).findViewById( R.id.drawer_userEmail );

        // Assign...
        mainFrameLayout = findViewById( R.id.main_activity_frame_layout );
        deliveryProductListFrameLayout = findViewById( R.id.on_delivery_product_frame_layout );

//        drawerName.setText( ADMIN_DATA_MODEL.getAdminName() ); // Admin Name...
//        drawerEmail.setText( ADMIN_DATA_MODEL.getAdminEmail() ); // Admin Email...

//        // Get Current Order List...
//        if (currentOrderListModelList.size() == 0){
//            DBQuery.getCurrentOrderList( USER_ACCOUNT.getUser_mobile() );
//        }

    }

    // --------  Menu And Navigation....
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main_activity_options, menu);
        // Check First whether any item in cart or not...

        // notification badge...
        MenuItem notificationItem = menu.findItem( R.id.menu_notification_main );
        notificationItem.setActionView( R.layout.badge_notification_layout );
        badgeNotifyCount = notificationItem.getActionView().findViewById( R.id.badge_count );
//        if (AdminQuery.notificationModelList.size() > 0){
//            badgeNotifyCount.setVisibility( View.VISIBLE );
//            badgeNotifyCount.setText( String.valueOf( AdminQuery.notificationModelList.size() ) );
//        }else{
//            badgeNotifyCount.setVisibility( View.GONE );
//        }
        notificationItem.getActionView().setOnClickListener( v -> {
//            Intent notifyIntent = new Intent( MainActivity.this, SetFragmentActivity.class);
//            notifyIntent.putExtra( "FRAGMENT_CODE", SetFragmentActivity.FRAGMENT_NOTIFICATION_OTHERS );
//            startActivity( notifyIntent );
        } );

        // notification Order badge...
        MenuItem notifyOrderItem = menu.findItem( R.id.menu_notification_orders );
        notifyOrderItem.setActionView( R.layout.badge_order_notification_layout );
        badgeOrderCount = notifyOrderItem.getActionView().findViewById( R.id.badge_notify_order_count );
//        if (AdminQuery.notificationModelList.size() > 0){
//            badgeOrderCount.setVisibility( View.VISIBLE );
//            badgeOrderCount.setText( String.valueOf( AdminQuery.notificationModelList.size() ) );
//        }else{
//            badgeOrderCount.setVisibility( View.GONE );
//        }
        notifyOrderItem.getActionView().setOnClickListener( v -> {
            Intent notifyIntent = new Intent( MainActivity.this, SetFragmentActivity.class);
            notifyIntent.putExtra( "FRAGMENT_CODE", SetFragmentActivity.FRAGMENT_NOTIFICATION_ORDERS );
            startActivity( notifyIntent );
        } );

        // new Order Notification List...
        DBQuery.getNewOrderNotification( USER_ACCOUNT.getUser_city_code() );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
//            if (isFragmentIsMyCart){
////                setFragment( new HomeFragment(), M_HOME_FRAGMENT );
//                wCurrentFragment = M_HOME_FRAGMENT;
//                navigationView.getMenu().getItem( 0 ).setChecked( true );
//                mainActivityForCart.finish();
//            }
            return true;
        }else
//        if (id == R.id.menu_order_main){
//            return true;
//        }else
        if (id == R.id.menu_notification_main){
//            Intent catIntent = new Intent( MainActivity.this, NotificationActivity.class);
//            startActivity( catIntent );
            return true;
        } else
            return super.onOptionsItemSelected( item );
    }

    int mainNavItemId;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer( GravityCompat.START );
        mainNavItemId = menuItem.getItemId();
        // ------- On Item Click...
        // Home Nav Option...
        if ( mainNavItemId == R.id.nav_home ){
            // index - 0
            getSupportActionBar().setTitle( R.string.app_title );
            return true;
        }else if(mainNavItemId == R.id.menu_my_account){
            // GO TO MY DELIVERY / ORDERS ACTIVITY
//            startActivity( new Intent(MainActivity.this, MyOrdersActivity.class ) );
            showToast( "My Account" );
            return false;
        }else if(mainNavItemId == R.id.menu_my_delivery){
            // GO TO MY DELIVERY / ORDERS ACTIVITY
            startActivity( new Intent(MainActivity.this, MyOrdersActivity.class ) );
            return false;
        }
        else
            // Bottom Options...
            if ( mainNavItemId == R.id.menu_log_out ){
                // index - 5
                /**   if (currentUser != null){
                 // TODO : Show Dialog to logOut..!
                 // Sign Out Dialog...
                 final Dialog signOut = new Dialog( MainActivity.this );
                 signOut.requestWindowFeature( Window.FEATURE_NO_TITLE );
                 signOut.setContentView( R.layout.dialog_sign_out );
                 signOut.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                 signOut.setCancelable( false );
                 ImageView imageView = signOut.findViewById( R.id.sign_out_image );
                 Glide.with( this ).load( "sample" ).apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_black_24dp ) ).into( imageView );
                 final Button signOutOkBtn = signOut.findViewById( R.id.sign_out_ok_btn );
                 Button signOutCancelBtn = signOut.findViewById( R.id.sign_out_cancel_btn );
                 signOut.show();

                 signOutOkBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                signOutOkBtn.setEnabled( false );
                firebaseAuth.signOut();
                currentUser = null;
                navigationView.getMenu().getItem( 0 ).setChecked( true );
                navigationView.getMenu().getItem( 5 ).setEnabled( false );
                signOut.dismiss();
                finish();
                }
                } );
                 signOutCancelBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                signOut.dismiss();
                // TODO : Sign Out
                }
                } );

                 return false;
                 } */
            }
        return false;
    }

    private void setNavUnChecked(){
        navigationView.getMenu().getItem( 0 ).setChecked( true );
//        navigationView.getMenu().getItem( 1 ).setChecked( false );
//        navigationView.getMenu().getItem( 2 ).setChecked( false );
//        navigationView.getMenu().getItem( 3 ).setChecked( false );
    }

    // Fragment Transaction...
    public void setFragment( Fragment fragment){
        if (mainActivityManager == null){
            mainActivityManager = getSupportFragmentManager();
        }
        try{
            FragmentTransaction fragmentTransaction = mainActivityManager.beginTransaction();
            fragmentTransaction.add( mainFrameLayout.getId(),fragment );
            fragmentTransaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkMapServices()){
            if (mLocationPermissionGranted){
                startLocationService();
                setMainMapsFragment();
            }else{
                getLocationPermission();
            }
        }

        // Set UnChecked for other nav options...
        setNavUnChecked();
    }

    //----------------------------
    private boolean mLocationPermissionGranted = false;

    private void setMainMapsFragment(){
        // Set Map Fragment...
        setFragment(  new MainMapsFragment ( this ) );
//        Toast.makeText( this, "Fragment Set!", Toast.LENGTH_SHORT ).show();
    }

    // Checking all the Google Map Permission ------------------------------------------------------
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            setMainMapsFragment();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION );
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d( TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "OnRequestPermissionResult: called. RequestCode: "+ requestCode);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    setMainMapsFragment();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent grantResults) {
        super.onActivityResult(requestCode, resultCode, grantResults);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                    // On Resume ...
                    startLocationService();
                    setMainMapsFragment();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }
    // Checking all the Google Map Permission ------------------------------------------------------

    // Starting Location Service....
    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
//        this.startService(serviceIntent);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                MainActivity.this.startForegroundService(serviceIntent);
            }else{
                startService(serviceIntent);
            }
        }
    }
    // Checking is Location Service is Already Running or Not....
    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(mapServicePackage.equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        Toast.makeText( this, "service not running..", Toast.LENGTH_SHORT ).show();
        return false;
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setDisplayShowTitleEnabled( true );
        getSupportActionBar().setTitle( title );
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onBackPressed(int From, String backTitle) {
        setTitle( backTitle );
    }

    @Override
    public void setNextFragment(Fragment fragment) {
        FragmentTransaction transaction = mainActivityManager.beginTransaction();
        transaction.replace( mainFrameLayout.getId(), fragment ).addToBackStack( null );
        transaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left, R.anim.slide_from_left, R.anim.slide_out_from_right );
        transaction.commit();
    }

}
