package ean.ecom.ean_shipping.launching;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import static ean.ecom.ean_shipping.database.DBQuery.firebaseFirestore;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 04:24
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class UserPermissionM implements CheckUserPermission.CheckAppUsePermission, CheckUserPermission.CheckAdminPermission {

    @Override
    public void checkAppPermission(final OnCheckFinisher onCheckFinisher, final String verCode) {
        firebaseFirestore.collection( "PERMISSION" ).document( "APP_USE_PERMISSION" )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    boolean isAllowed = task.getResult().getBoolean( verCode );
                    if ( isAllowed ){
                        onCheckFinisher.onCheckedFinish( true );
                    }else{
                        onCheckFinisher.onCheckedFinish( false );
                    }
                }else {

                }
            }
        } );
    }

    @Override
    public void checkAdminPermission(OnAdminCheckFinisher onCheckFinisher, String mobile, String email) {
        // If Permission Granted..!!
        onCheckFinisher.onAdminPermissionCheckFinish( true );
        //..
    }



}
