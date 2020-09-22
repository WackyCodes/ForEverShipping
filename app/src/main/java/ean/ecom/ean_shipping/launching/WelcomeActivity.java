package ean.ecom.ean_shipping.launching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import ean.ecom.ean_shipping.R;
import ean.ecom.ean_shipping.other.CheckInternetConnection;
import ean.ecom.ean_shipping.other.DialogsClass;
import ean.ecom.ean_shipping.other.StaticMethods;
import ean.ecom.ean_shipping.other.StaticValues;

import static ean.ecom.ean_shipping.database.DBQuery.currentUser;
import static ean.ecom.ean_shipping.database.DBQuery.firebaseAuth;
import static ean.ecom.ean_shipping.database.DBQuery.firebaseFirestore;
import static ean.ecom.ean_shipping.other.StaticMethods.showToast;
import static ean.ecom.ean_shipping.other.StaticValues.STORAGE_PERMISSION;

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
        // Start Checking...
        appUsePermissionPresenter.onCheckedStart();

        if( CheckInternetConnection.isInternetConnected( this ) ){

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

    private void checkCurrentUser(){
        // Load Area List...
//        DBQuery.getCityListQuery(); // Not Required!
        // Load Shop List.. > In main Activity...
        if (currentUser != null){
            String userMobile = StaticMethods.readFileFromLocal(this, "mobile" );
            String email = StaticMethods.readFileFromLocal(this, "shop" );

            if (userMobile != null && email != null){
                adminPermissionPresenter = new CheckPermissionP( this, new UserPermissionM(), userMobile , email );
                adminPermissionPresenter.onAdminPermissionCheckStart();
            }else{
                firebaseAuth.signOut();
//                startActivity( new Intent( WelcomeActivity.this, AuthActivity.class ) );
//                finish();
            }

        }else{
//            startActivity( new Intent( WelcomeActivity.this, AuthActivity.class ) );
//            finish();
        }

    }

    @Override
    public void continueToNext(int stepNo) {
        switch (stepNo){
            case CHECK_STORAGE_PERMISSION:
                break;
            case GO_TO_MAIN_ACTIVITY:
                break;
            default:
                break;
        }
    }

    @Override
    public void showAlertDialog(String msg) {

    }

    @Override
    public void showActionDialog(String msg) {

    }




}
