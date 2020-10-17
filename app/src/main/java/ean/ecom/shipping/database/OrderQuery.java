package ean.ecom.shipping.database;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.shipping.main.order.GetOrderDetailsListener;

import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.database.DBQuery.firebaseFirestore;

/**
 * Created by Shailendra (WackyCodes) on 17/10/2020 09:56
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderQuery {

    public static ListenerRegistration orderUpdateListener;

    public static void onOrderUpdateListener( String deliveryID, final GetOrderDetailsListener listener ){
        orderUpdateListener = firebaseFirestore.collection( "DELIVERY" )
                .document( "CITY_CODE" ).collection( "DELIVERY" )
                .document( deliveryID ).addSnapshotListener( (value, error) -> {

                    Map<String, Object> resultMap = new HashMap <>();

                    String order_status = value.get( "delivery_status" ).toString();

                    if(value.get( "delivery_by_uid" )!= null){
                        resultMap.put( "UID",  value.get( "delivery_by_uid" ).toString() );
                    }
                    if(value.get( "verify_otp" )!= null){
                        resultMap.put( "verify_otp",  value.get( "verify_otp" ).toString() );
                    }

                    if (listener != null){
                        listener.onUpdateDeliveryStatus( order_status, resultMap );
                    }

                } );

    }

    public static void updateOrderOnShop( final GetOrderDetailsListener listener,
                                          String shopID, String orderID, Map<String, Object> updateMap, int updateRequest ){

//        updateMap.put( "delivery_date", "sfds" );
//        updateMap.put( "delivery_day", "sfds" );
//        updateMap.put( "delivery_time", "sfds" );

        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .collection( "ORDERS" )
                .document( orderID )
                .update( updateMap )
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        if (updateRequest == 1){ // After Success accepted.. To Update UI...
//                            listener.onAcceptedOrder();
                            listener.showToast( "Order No." + orderID + " assign to you:)" );
                        }
                    }else{
                        listener.onUpdateStatusFailed();
                    }
                } );

    }

    public static class UpdateOrderStatus implements Runnable{

        private Map<String, Object> updateMap;
        private String orderID;
        private String CityCode;
        private GetOrderDetailsListener listener;

        public UpdateOrderStatus(Map <String, Object> updateMap, String orderID, String CityCode, GetOrderDetailsListener listener) {
            this.updateMap = updateMap;
            this.orderID = orderID;
            this.CityCode = CityCode;
            this.listener = listener;
        }

        @Override
        public void run() {
            firebaseFirestore.collection( "DELIVERY" )
                    .document( CityCode ).collection( "DELIVERY" )
                    .document( orderID )
                    .update( updateMap )
                    .addOnCompleteListener( task -> {
                        if (task.isSuccessful()){
//                        getOrderDetailsListener.onUpdateResult( "PROCESS" );
                            // Auto Run Register Listener...
                        }else{
                            listener.onUpdateStatusFailed();
                        }

                    } );
        }


    }



}
