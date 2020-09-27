package ean.ecom.shipping.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 03:40
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance(  );

    public  static StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    public static void getOrderDetailsQuery(String CITY_CODE, String orderID){

        firebaseFirestore.collection( "DELIVERY" ).document( CITY_CODE )
                .collection( "DELIVERY" )
                .document( orderID )
                .get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();

                            Double latitude = (Double) documentSnapshot.get( "shipping_geo_point" );
                            Object Longitude = documentSnapshot.getGeoPoint( "shipping_geo_point" );

                            GeoPoint geoPoint = documentSnapshot.getGeoPoint( "shipping_geo_point" );

                            Map<String, Object> updateMap = new HashMap <>();
                            updateMap.put( "shipping_geo_point", geoPoint );
                            updateMap.put( "shipping_geo_poi", new GeoPoint( 25,0 ) );


                        }else{

                        }

                    }
                } );




    }



}
