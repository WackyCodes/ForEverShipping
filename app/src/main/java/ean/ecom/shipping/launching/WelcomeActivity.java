package ean.ecom.shipping.launching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import ean.ecom.shipping.MainActivity;
import ean.ecom.shipping.R;
import ean.ecom.shipping.other.CheckInternetConnection;
import ean.ecom.shipping.other.DialogsClass;
import ean.ecom.shipping.other.StaticMethods;
import ean.ecom.shipping.other.StaticValues;

import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.database.DBQuery.firebaseAuth;
import static ean.ecom.shipping.other.StaticMethods.showToast;
import static ean.ecom.shipping.other.StaticValues.STORAGE_PERMISSION;

public class WelcomeActivity extends AppCompatActivity implements CheckUserPermission {
    public static AppCompatActivity welcomeActivity;

    public static final int CHECK_STORAGE_PERMISSION = 1;
    public static final int GO_TO_MAIN_ACTIVITY = 2;

    // Check User Permission...
    CheckUserPermission.AdminPermissionPresenter adminPermissionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcome );
        welcomeActivity = this;
        CheckUserPermission.AppUsePermissionPresenter appUsePermissionPresenter = new CheckPermissionP( this, new UserPermissionM(), StaticValues.APP_VERSION );

        if( CheckInternetConnection.isInternetConnected( this ) ){
            // Start Checking...
            appUsePermissionPresenter.onCheckedStart();
        }

    }

    /// Storage Permission...
    public void askStoragePermission(){
        if(ContextCompat.checkSelfPermission( WelcomeActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission( WelcomeActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED){
            checkCurrentUser();
        }else {
            requestStoragePermission();
        }
    }
    private void requestStoragePermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE )
                || ActivityCompat.shouldShowRequestPermissionRationale( this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE )){

            new AlertDialog.Builder( this )
                    .setTitle( "Storage Permission" )
                    .setMessage( "Storage permission is needed, because of File Storage will be required!" )
                    .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions( WelcomeActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    }, STORAGE_PERMISSION );
                        }
                    } ).setNegativeButton( "Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
//                    requestStoragePermission();
                    finish();
                }
            } ).create().show();
        }else{
            ActivityCompat.requestPermissions( WelcomeActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
                            , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, STORAGE_PERMISSION );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== STORAGE_PERMISSION){
            if(grantResults.length>0
                    && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED
            ){
                showToast( this,"Permission is GRANTED..." );
                checkCurrentUser();
            }
            else{
                showToast( this,"Permission DENIED!" );
                requestStoragePermission();
            }
        }
    }

    // Check Current User : Is Login Already or Not
    private void checkCurrentUser(){
        // Load Area List...
//        DBQuery.getCityListQuery(); // Not Required!
        // Load Shop List.. > In main Activity...
        if (currentUser != null){
            // Getting Data from Local Directory. If user has login before, then we can get the data that we had created at signUp/login time
            String userMobile = StaticMethods.readFileFromLocal(this, "mobile" );
//            String email = StaticMethods.readFileFromLocal(this, "shop" );

            if (userMobile != null){
                // Now We have to check whether User has permission to use this app or not
                adminPermissionPresenter = new CheckPermissionP( this, new UserPermissionM(), userMobile, "");
                adminPermissionPresenter.onAdminPermissionCheckStart();
            }else{
                // In Case User data not found in local disk!
                firebaseAuth.signOut();
                startActivity( new Intent( WelcomeActivity.this, AuthActivity.class ) );
                finish();
            }

        }else{
            startActivity( new Intent( WelcomeActivity.this, AuthActivity.class ) );
            finish();
        }

    }

    @Override
    public void continueToNext(int stepNo) {
        switch (stepNo){
            case CHECK_STORAGE_PERMISSION:
                // Check Storage Permission, Before Enter in the App.!
                askStoragePermission();
                break;
            case GO_TO_MAIN_ACTIVITY:
                // Check Successfully!
                startActivity( new Intent( WelcomeActivity.this, MainActivity.class ) );
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void showAlertDialog(String msg) {
        DialogsClass.alertDialog( WelcomeActivity.this, "ALERT!" , msg).show();
    }

    @Override
    public void showActionDialog(String msg) {
        // NOT USED YET!
    }




}
