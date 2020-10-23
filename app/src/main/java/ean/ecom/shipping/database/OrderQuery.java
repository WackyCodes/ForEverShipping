package ean.ecom.shipping.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.shipping.main.order.CurrentOrderListModel;
import ean.ecom.shipping.main.order.GetOrderDetailsListener;
import ean.ecom.shipping.other.StaticValues;

import static ean.ecom.shipping.database.DBQuery.currentOrderListModelList;
import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.database.DBQuery.firebaseFirestore;
import static ean.ecom.shipping.main.MainMapsFragment.shippingOrderAdaptor;
import static ean.ecom.shipping.other.StaticValues.ORDER_ACCEPTED;
import static ean.ecom.shipping.other.StaticValues.ORDER_CODE_ACCEPTED;
import static ean.ecom.shipping.other.StaticValues.ORDER_CODE_CANCEL;
import static ean.ecom.shipping.other.StaticValues.ORDER_CODE_COMPLETE;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;

/**
 * Created by Shailendra (WackyCodes) on 17/10/2020 09:56
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderQuery {
    public static ListenerRegistration orderUpdateListener;
    private GetOrderDetailsListener listener;

    public OrderQuery(GetOrderDetailsListener listener) {
        this.listener = listener;
    }

    public void onOrderUpdateListener( GetOrderDetailsListener listener, String deliveryID ){
        try{
            orderUpdateListener = firebaseFirestore.collection( "DELIVERY" )
                    .document( StaticValues.USER_ACCOUNT.getUser_city_code() ).collection( "DELIVERY" )
                    .document( deliveryID ).addSnapshotListener( (value, error) -> {

                        Map<String, Object> resultMap = new HashMap <>();

                        String order_status = value.get( "delivery_status" ).toString();

                        if(value.get( "delivery_by_uid" )!= null){
                            resultMap.put( "delivery_by_uid",  value.get( "delivery_by_uid" ).toString() );
                        }
                        if(value.get( "verify_otp" )!= null){
                            resultMap.put( "verify_otp",  value.get( "verify_otp" ).toString() );
                        }

                        if (listener != null){
                            listener.onUpdateDeliveryStatus( order_status, resultMap );
                        }

                    } );

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getOtpQuery(String deliveryID){
        firebaseFirestore.collection( "DELIVERY" )
                .document( "CITY_CODE" ).collection( "DELIVERY" )
                .document( deliveryID )
                .get().addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        listener.onAcceptedOrder( task.getResult().get( "verify_otp" ).toString() );
                    }else{
                        listener.onAcceptedOrder( "Failed" );
                    }
                } );
    }

    public void setCustomerOTPVerify( GetOrderDetailsListener listener,String shopId, String orderID, String OTP  ){

        firebaseFirestore.collection( "SHOPS" )
                .document( shopId ).collection( "ORDERS" )
                .document( orderID )
                .get()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        if (task.getResult().get( "success_delivery_otp" ).toString().equals( OTP )){
                            listener.onOrderCompleteNextStep( 0 ); // Verified OTP...
                        }else{
                            listener.onCustomerOTPNotVerified( false );
                        }
                    }else{
                        listener.onCustomerOTPNotVerified( true );
                    }

                } );
    }

    public void updateOrderOnShop( final GetOrderDetailsListener listener,
                                          String shopID, String orderID, Map<String, Object> updateMap, int updateRequest ){
        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .collection( "ORDERS" )
                .document( orderID )
                .update( updateMap )
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        if (updateRequest == 1){ // After Success accepted.. To Update UI...
//                            listener.onAcceptedOrder();
                            listener.showToast( "Order No." + orderID + " assign to you:)" );
                        }else if (updateRequest == 2){
//                            listener.showToast( "Order No." + orderID + " : Successfully Delivered.!" );
                            listener.onOrderCompleteNextStep( 1 ); //successfully Update in shop Order List...
                        }else if (updateRequest == 3){ // Cancel by Delivery Boy...
//                            listener.showToast( "Order No." + orderID + " : Cancelled.!" );
                            listener.onCancelledOrder( false );
                        }else if( updateRequest == 4){ // Cancel by Customer...
                            listener.onCancelledOrder( true );
                        }
                    }else{
                        if (updateRequest == 2){
                            listener.onOrderCompleteFailed( 1 ); //  Failed to update at shop Order list...
                        }else{
                            listener.onUpdateStatusFailed();
                        }
                    }
                } );

    }

    public void addOrderInCurrentDelivery(GetOrderDetailsListener listener, CurrentOrderListModel currentOrderListModel,
                                          String deliveryID, @Nullable String OTP, boolean isSuccessDelivered){

        firebaseFirestore.collection( "DELIVERY_BOYS" )
                .document( USER_ACCOUNT.getUser_mobile() )
                .collection( "DELIVERY" )
                .document( deliveryID )
                .set( currentOrderListModel )
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        // Set UI...
                        if ( isSuccessDelivered ){ // Success Delivered...
                            listener.onOrderCompleteNextStep( 2 ); // Success Update in Delivery List...
                        }else{ // Accepted
                            // Add into Current Shipping List...
                            listener.onAcceptedOrder( OTP );

                            for (CurrentOrderListModel model : currentOrderListModelList){
                                if (currentOrderListModel.getOrder_id().equals( model.getOrder_id() )){
                                    return;
                                }
                            }
                            // Add item...
                            currentOrderListModelList.add( currentOrderListModel );
                        }

                    }else{
                        if ( isSuccessDelivered ){
                            listener.onOrderCompleteFailed( 2 ); // Failed to update at Delivery list...
                        }
                    }
                } );

    }

    public void deleteDeliveryIdQuery(GetOrderDetailsListener listener, String deliveryID, boolean isRetry){
        try{
            firebaseFirestore.collection( "DELIVERY" )
                    .document( StaticValues.USER_ACCOUNT.getUser_city_code() ).collection( "DELIVERY" )
                    .document( deliveryID )
                    .delete()
                    .addOnCompleteListener( task -> {
                        if (task.isSuccessful()){
                            if (!isRetry){
                                listener.onOrderCompleteNextStep( 3 ); // Success delete temp Delivery Document...
                            }
                        }else{
                            if (!isRetry){
                                listener.onOrderCompleteFailed( 3 ); // Failed to delete temp Delivery Document..
                            }
                        }
                    } );
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void deleteMyDeliveryID( GetOrderDetailsListener listener, String deliveryID, String userMobile ){
        firebaseFirestore.collection( "DELIVERY_BOYS" )
                .document( userMobile ).collection( "DELIVERY" )
                .document( deliveryID )
                .delete()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()){
                        listener.showToast( "Successfully cancelled!" );
                    }else{
                        listener.showToast( "Please check Your Internet Connection!" );
                        deleteMyDeliveryID( listener, deliveryID, userMobile );
                    }
                } );

    }

    public static class UpdateOrderStatus implements Runnable{

        private Map<String, Object> updateMap;
        private String deliveryID;
        private String CityCode;
        private GetOrderDetailsListener mListener;
        private int requestCode;

        public UpdateOrderStatus(Map <String, Object> updateMap, String deliveryID, String CityCode, int requestCode, GetOrderDetailsListener listener) {
            this.updateMap = updateMap;
            this.deliveryID = deliveryID;
            this.CityCode = CityCode;
            this.requestCode = requestCode;
            this.mListener = listener;
        }

        @Override
        public void run() {
            firebaseFirestore.collection( "DELIVERY" )
                    .document( CityCode ).collection( "DELIVERY" )
                    .document( deliveryID )
                    .update( updateMap )
                    .addOnCompleteListener( task -> {
                        if (task.isSuccessful()){
                            // Auto Run Register Listener...
                            switch (requestCode){
                                case ORDER_CODE_CANCEL:
                                    mListener.onRequestToCancelOrder();
                                    break;
                                case ORDER_CODE_ACCEPTED:
                                case ORDER_CODE_COMPLETE:
                                default:
                                    break;
                            }
                        }else{
                            mListener.onUpdateStatusFailed();
                        }

                    } );
        }


    }


}
