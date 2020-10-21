package ean.ecom.shipping.database;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.shipping.MainActivity;
import ean.ecom.shipping.main.order.CurrentOrderListModel;
import ean.ecom.shipping.notification.NotificationModel;
import ean.ecom.shipping.other.StaticValues;

import static ean.ecom.shipping.notification.NotificationFragment.orderNotificationAdaptor;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 03:40
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static void getOrderDetailsQuery(String CITY_CODE, String orderID) {

        firebaseFirestore.collection( "DELIVERY" ).document( CITY_CODE )
                .collection( "DELIVERY" ).document( orderID ).get()
                .addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();

                    Double latitude = (Double) documentSnapshot.get( "shipping_geo_point" );
                    Object Longitude = documentSnapshot.getGeoPoint( "shipping_geo_point" );

                    GeoPoint geoPoint = documentSnapshot.getGeoPoint( "shipping_geo_point" );

                    Map <String, Object> updateMap = new HashMap <>();
                    updateMap.put( "shipping_geo_point", geoPoint );
                    updateMap.put( "shipping_geo_poi", new GeoPoint( 25, 0 ) );


                } else {

                }
            }
        } );

    }

    //    ListenerRegistration listenerRegistration;
    public static ListenerRegistration userNotificationLR;
    public static List <CurrentOrderListModel> currentOrderListModelList = new ArrayList <>();
    public static List <NotificationModel> notificationModelList = new ArrayList <>();
    public static List <CurrentOrderListModel> orderNotificationList = new ArrayList <>();
    // Get Notify Every New Order...
    public static void getNewOrderNotification(String CITY_CODE) {

        userNotificationLR = firebaseFirestore.collection( "DELIVERY" )
                .document( CITY_CODE ).collection( "DELIVERY" )
                .whereEqualTo( "delivery_status", "ACCEPTED" )
                .addSnapshotListener( (queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null) {
                        if (orderNotificationList != null) {
                            orderNotificationList.clear();
                        } else {
                            orderNotificationList = new ArrayList <>();
                        }
                        int badgeCount = 0;
                        CurrentOrderListModel currentOrderListModel;
                        // Load Data...
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            currentOrderListModel = new CurrentOrderListModel();

                            if (documentSnapshot.get( "shipping_geo_point" ) != null) {
                                currentOrderListModel.setShipping_geo_point( documentSnapshot.getGeoPoint( "shipping_geo_point" ) );
                                StaticValues.MY_GEO_POINTS = documentSnapshot.getGeoPoint( "shipping_geo_point" );
                            }
                            if (documentSnapshot.get( "shop_geo_point" ) != null) {
                                currentOrderListModel.setShop_geo_point( documentSnapshot.getGeoPoint( "shop_geo_point" ) );
                            }

                            String delivery_id = documentSnapshot.getId();
//                            String shop_helpline = documentSnapshot.get( "shop_helpline" ).toString();
//                            String verify_otp = documentSnapshot.get( "verify_otp" ).toString();

                            String shop_id = documentSnapshot.get( "shop_id" ).toString();
                            String shop_name = documentSnapshot.get( "shop_name" ).toString();
                            String order_id = documentSnapshot.get( "order_id" ).toString();
                            String delivery_status = documentSnapshot.get( "delivery_status" ).toString();

                            String order_time_Str = documentSnapshot.get( "accepted_date" ).toString();
//                                Timestamp order_time = documentSnapshot.getTimestamp( "order_time" );

                            String shipping_address = documentSnapshot.get( "shipping_address" ).toString();
                            String shop_address = documentSnapshot.get( "shop_address" ).toString();

                            currentOrderListModel.setDelivery_id( delivery_id );
                            currentOrderListModel.setShop_id( shop_id );
                            currentOrderListModel.setShop_name( shop_name );
                            currentOrderListModel.setOrder_id( order_id );
                            currentOrderListModel.setDelivery_status( delivery_status );
                            currentOrderListModel.setOrder_time( order_time_Str );
                            currentOrderListModel.setShipping_address( shipping_address );
                            currentOrderListModel.setShop_address( shop_address );

                            // Add Model in the list...
                            orderNotificationList.add( currentOrderListModel );
                            if ( orderNotificationAdaptor != null ){
                                orderNotificationAdaptor.notifyDataSetChanged();
                            }

                            badgeCount = badgeCount + 1;

                        }
//                        if ( !orderNotificationList.contains( currentOrderListModel )){
//
//                        }

                        if ( badgeCount > 0 &&  MainActivity.badgeOrderCount != null){
                            MainActivity.badgeOrderCount.setVisibility( View.VISIBLE );
                            MainActivity.badgeOrderCount.setText(String.valueOf( badgeCount ));
                        }
                        else if (MainActivity.badgeOrderCount != null){
                            MainActivity.badgeOrderCount.setVisibility( View.INVISIBLE );
                        }

                    }
                } );
    }

    // Get User Own Data Collection Ref..
    public static CollectionReference getMyCollectionRef( String collection ){
        return firebaseFirestore.collection( "DELIVERY_BOY" ).document( currentUser.getUid() )
                .collection( collection );
    }

    // Currently on Shipping... Not Delivered Yet!
    public static void getCurrentShippingOrderList(){

        getMyCollectionRef( "MY_CURRENT_SHIPPING" )
                .orderBy( "index" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Add data in the list...

                }else{

                }
            }
        } );

    }

    // Rating List Given by Customers...
    public static void getMyRatingList(){

        getMyCollectionRef( "MY_RATING" )
                .orderBy( "index" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Add data in the list...

                }else{

                }
            }
        } );

    }

    // Delivery List Whichever has been delivered successfully!
    public static void getMyDeliveryList(){

        getMyCollectionRef( "MY_DELIVERY" )
                .orderBy( "index" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Add data in the list...

                }else{

                }
            }
        } );

    }




}
