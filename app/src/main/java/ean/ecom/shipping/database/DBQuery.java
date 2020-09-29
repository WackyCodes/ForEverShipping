package ean.ecom.shipping.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.shipping.main.OrderDetailsModel;

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

//    ListenerRegistration listenerRegistration;
    private static OrderDetailsModel orderDetailsModel;

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

    public static void getNewOrderNotification(String CITY_CODE ){

        firebaseFirestore.collection( "DELIVERY" ).document( CITY_CODE )
                .collection( "DELIVERY" )
                .whereEqualTo( "delivery_status", "ACCEPTED" )
                .addSnapshotListener( new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null){
                            for ( DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                orderDetailsModel = new OrderDetailsModel();

                                if (documentSnapshot.get( "shipping_geo_point" )!= null){
                                    orderDetailsModel.setShippingGeoPoint( documentSnapshot.getGeoPoint( "shipping_geo_point" ) );
                                }
                                if (documentSnapshot.get( "shop_geo_point" )!= null){
                                    orderDetailsModel.setShopGeoPoint( documentSnapshot.getGeoPoint( "shop_geo_point" ) );
                                }

                            }
                        }else{
                            return;
                        }
                    }
                } );
    }


}
