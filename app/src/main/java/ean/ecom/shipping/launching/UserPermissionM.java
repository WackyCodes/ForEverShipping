package ean.ecom.shipping.launching;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.shipping.database.DBQuery;
import ean.ecom.shipping.other.DialogsClass;

import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.database.DBQuery.firebaseAuth;
import static ean.ecom.shipping.database.DBQuery.firebaseFirestore;
import static ean.ecom.shipping.other.StaticMethods.writeFileInLocal;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;

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
                    // TASK Failed!
                    onCheckFinisher.onQueryFailed(  );
                }
            }
        } );
    }

    // We are getting User Information after Checking Permission...
    @Override
    public void checkAdminPermission(final OnAdminCheckFinisher onCheckFinisher, String mobile, String email) {

        firebaseFirestore.collection( "DELIVERY_BOYS" )
                .document( mobile )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Boolean is_allowed = task.getResult().getBoolean( "is_allowed" );
                    if (is_allowed) {
                        try{
                            USER_ACCOUNT.setUser_email( task.getResult().get( "email" ).toString() );
                            USER_ACCOUNT.setUser_mobile( task.getResult().get( "mobile" ).toString() );
                            USER_ACCOUNT.setUser_id( task.getResult().get( "auth_id" ).toString() );
                            USER_ACCOUNT.setUser_city_code( task.getResult().get( "city_code" ).toString() );
                            USER_ACCOUNT.setUser_name( task.getResult().get( "name" ).toString() );
                            USER_ACCOUNT.setUser_image( task.getResult().get( "admin_photo" ).toString() );
                            USER_ACCOUNT.setUser_address( task.getResult().get( "address" ).toString() );

                        }catch (NullPointerException e){

                        }finally {
                            onCheckFinisher.onAdminPermissionCheckFinish( true );
                        }

                    }else{
                        onCheckFinisher.onAdminPermissionCheckFinish( false );
                    }

                }else{
                    onCheckFinisher.onAdminPermissionCheckFinish( false );
                }
            }
        } );

    }

    //// Checking User Auth Steps...
    public void onSignInListener(final CheckUserPermission.CheckIsUserRegistered checkIsUserRegistered, final String userMobile,  String email, String password){

        firebaseAuth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task) {
                        if (task.isSuccessful()){
                            // Assign Current User...
                            checkIsUserRegistered.onSignInResponse( true, userMobile );
                            DBQuery.currentUser = firebaseAuth.getCurrentUser();
                            // Success...
                            // Write in Local file
//                            writeDataInLocal( context, signInShopID.getText().toString().trim(), signInMobile.getText().toString() );

                        }else{
                            checkIsUserRegistered.onSignInResponse( false, userMobile );
                        }
                    }
                } );

    }

    public void onSignUpListener(final CheckUserPermission.CheckIsUserRegistered checkIsUserRegistered, final String userMobile, final String email, String password){

        firebaseAuth.createUserWithEmailAndPassword( email, password )
                .addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task) {
                        if (task.isSuccessful()){
                            // Assign Current User...
                            DBQuery.currentUser = firebaseAuth.getCurrentUser();
                            onSignUpUserUpdateId( checkIsUserRegistered, userMobile, firebaseAuth.getCurrentUser().getUid() );
                            // Success...
//                            Map <String, Object> updateMap = new HashMap <>();
//                            updateMap.put( "auth_id", firebaseAuth.getCurrentUser().getUid() );

                            // Write in Local file...
//                            writeDataInLocal( context, signInShopID.getText().toString().trim(), signInMobile.getText().toString() );
                        }else{
                            checkIsUserRegistered.onSignUpResponse( false, userMobile,null );
                        }
                    }
                } );

    }

    private void onSignUpUserUpdateId(final CheckUserPermission.CheckIsUserRegistered checkIsUserRegistered, final String userMobile, final String authID ){
        firebaseFirestore.collection( "DELIVERY_BOYS" )
                .document( userMobile )
                .update( "auth_id", authID )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        checkIsUserRegistered.onSignUpResponse( true, userMobile, authID );
                    }
                } );
    }

    // Checking User Register
    public void onUserRegisterCheckListener(final CheckUserPermission.CheckIsUserRegistered checkIsUserRegistered, String mobile){
//        checkIsUserRegistered.onNotRegisteredUser();
//        checkIsUserRegistered.onUserRegisteredChecked( false, "email" );

        firebaseFirestore.collection( "DELIVERY_BOYS" )
                .document( mobile )
                .addSnapshotListener( new EventListener <DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()){
//                            String authID = documentSnapshot.get( "auth_id" ).toString();
                            if (documentSnapshot.get( "auth_id" ) != null ){
                                // Show Pass...
                                String adminEmail = documentSnapshot.get( "email" ).toString();
                                checkIsUserRegistered.onUserRegisteredChecked( true, adminEmail );

                            }else{
                                // Show Create Pass..
                                String adminEmail = documentSnapshot.get( "email" ).toString();
                                checkIsUserRegistered.onUserRegisteredChecked( false, adminEmail );
                            }
                        }
                        else{
                            checkIsUserRegistered.onNotRegisteredUser();
                        }
                    }
                } );

    }




}
