package ean.ecom.shipping.main.order;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.shipping.R;
import ean.ecom.shipping.database.OrderQuery;

import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.database.DBQuery.firebaseFirestore;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_COMPLETE_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_CURRENT_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_NOTIFICATION;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_PICKED_ORDERS;

public class OrderViewFragment extends Fragment implements GetOrderDetailsListener {

    public OrderViewFragment( String orderID, String shopID, String deliveryID) {
        // Required empty public constructor
        this.deliveryID = deliveryID;
        this.orderID = orderID;
        this.shopID = shopID;
    }

    private String orderID;
    private String shopID;
    private String deliveryID;
    private OrderQuery orderQuery;
    private OrderListModel orderModel;

    private RelativeLayout layoutDetails; //layout_details

    private LinearLayout layoutOTP; // order_otp_layout
    private TextView tvOrderID; // order_id_text
    private TextView tvOtpTitle; // otp_title_text
    private TextView tvShopOtp; // shop_otp_text
    private TextView tvOtpGuide; // otp_guide_text
    private EditText etCustomerOtp; // customer_otp_edit_text

    private TextView tvShopName; // shop_name
    private TextView tvShopId; // shop_id_text
    private TextView tvShopAddress; // shop_address_text

    private LinearLayout getShopDirectionBtn; // get_shop_direction_layout
    private LinearLayout getShippingDirectionBtn; // get_shipping_direction_layout

    private TextView tvShippingAddress; // shipping_address_text

    private RecyclerView recyclerOrderListView; // shipping_product_recycler

    private Button buttonCompleteDelivery; // order_complete_button
    private Button buttonCancelDelivery; // order_cancel_button
    private TextView acceptDeliveryBtn; /// accept_delivery_text

    private ProgressDialog dialog;
    private Thread acceptOrderThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_order_view, container, false );
        orderQuery = new OrderQuery( this );
        dialog = new ProgressDialog( getContext() );
        dialog.setTitle( "Please wait..." );

        layoutDetails = view.findViewById( R.id.layout_details );

        layoutOTP = view.findViewById( R.id.order_otp_layout );
        tvOrderID = view.findViewById( R.id.order_id_text );
        tvOtpTitle = view.findViewById( R.id.otp_title_text );
        tvShopOtp = view.findViewById( R.id.shop_otp_text );
        tvOtpGuide = view.findViewById( R.id.otp_guide_text );
        etCustomerOtp = view.findViewById( R.id.customer_otp_edit_text );

        tvShopName = view.findViewById( R.id.shop_name );
        tvShopId = view.findViewById( R.id.shop_id_text );
        tvShopAddress = view.findViewById( R.id.shop_address_text );

        getShopDirectionBtn = view.findViewById( R.id.get_shop_direction_layout );
        getShippingDirectionBtn = view.findViewById( R.id.get_shipping_direction_layout );

        tvShippingAddress = view.findViewById( R.id.shipping_address_text );
        recyclerOrderListView = view.findViewById( R.id.shipping_product_recycler );

        buttonCompleteDelivery = view.findViewById( R.id.order_complete_button );
        buttonCancelDelivery = view.findViewById( R.id.order_cancel_button );
        acceptDeliveryBtn = view.findViewById( R.id.accept_delivery_text );

        // set Default View....
        Thread loadDataThread = new Thread(new LoadOrderData( orderID, shopID, USER_ACCOUNT.getUser_city_code(), this ));
        loadDataThread.start();
        showDialog();

        setOnButtonClick(view);

        return view;
    }

    private void setOnButtonClick(View view){
        acceptDeliveryBtn.setOnClickListener( v->{
            acceptDeliveryBtn.setEnabled( false );
            onAcceptButtonClick();
        } );
    }

    /// Set Data...
    private void setOrderData(){
        String dStatus = orderModel.getDeliveryStatus();
        if (dStatus.equals( "ACCEPTED" )){
            setDefaultLayout( VIEW_ORDER_FROM_NOTIFICATION );
            // Add Listener...
            orderQuery.onOrderUpdateListener( deliveryID );

        }else if( dStatus.equals( "PICKED" )){
            setDefaultLayout( VIEW_ORDER_PICKED_ORDERS );
        }else if( dStatus.equals( "SUCCESS" )){
            setDefaultLayout( VIEW_ORDER_FROM_COMPLETE_ORDERS );
        }else if( dStatus.equals( "PROCESS" )){
            setDefaultLayout( VIEW_ORDER_FROM_CURRENT_ORDERS );
            // Get OTP ..
            orderQuery.getOtpQuery( orderModel.getDeliveryID() );
        }
        setProductItemLayout();

        dismissDialog();
    }

    // Set Products Item's Details. Recycler
    private void setProductItemLayout(){
        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
        layoutManager.setOrientation( RecyclerView.VERTICAL );
        recyclerOrderListView.setLayoutManager( layoutManager );

        OrderItemListAdaptor adaptor = new OrderItemListAdaptor( orderModel.getOrderProductItemsList() );
        recyclerOrderListView.setAdapter( adaptor );
        adaptor.notifyDataSetChanged();
    }

    private void setDefaultLayout(int viewID){
        switch (viewID){
            case VIEW_ORDER_FROM_NOTIFICATION:
                buttonCompleteDelivery.setVisibility( View.GONE );
                buttonCancelDelivery.setVisibility( View.GONE );
                acceptDeliveryBtn.setVisibility( View.VISIBLE );
                layoutOTP.setVisibility( View.GONE );
                break;
            case VIEW_ORDER_FROM_CURRENT_ORDERS:
                buttonCompleteDelivery.setVisibility( View.GONE );
                buttonCancelDelivery.setVisibility( View.VISIBLE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.VISIBLE );
                etCustomerOtp.setVisibility( View.GONE );
                tvShopOtp.setVisibility( View.VISIBLE );
                break;
            case VIEW_ORDER_FROM_COMPLETE_ORDERS:
                buttonCompleteDelivery.setVisibility( View.GONE );
                buttonCancelDelivery.setVisibility( View.GONE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.GONE );
                getShippingDirectionBtn.setVisibility( View.INVISIBLE );
                break;
            case VIEW_ORDER_PICKED_ORDERS:
                buttonCompleteDelivery.setVisibility( View.VISIBLE );
                buttonCancelDelivery.setVisibility( View.GONE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.VISIBLE );
                tvShopOtp.setVisibility( View.GONE );
                etCustomerOtp.setVisibility( View.VISIBLE );
                break;
            default:
                break;
        }
    }

    private void onAcceptButtonClick(){
        showDialog();
        Map<String, Object> updateMap = new HashMap();
        updateMap.put( "delivery_status", "PROCESS" );
        updateMap.put( "delivery_by_uid", currentUser.getUid() );
        acceptOrderThread = new Thread( new OrderQuery.UpdateOrderStatus(updateMap, deliveryID, USER_ACCOUNT.getUser_city_code(), this) );
        acceptOrderThread.start();
    }

    @Override
    public void onReceiveDetails( OrderListModel orderListModel ) {
        orderModel = orderListModel;

        tvOrderID.setText( orderModel.getOrderID() );
        tvShopName.setText( orderModel.getShippingName() );
        tvShopId.setText( shopID );
        tvShopAddress.setText( orderModel.getShippingAddress() );
        // Shipping...
        tvShippingAddress.setText( orderModel.getShippingAddress() );

        // Set Other Data....
        setOrderData();
    }

    @Override
    public void onReceiveFailed() {
        dismissDialog();
        showToast( "Product Loading failed! Please check your internet and try again!" );
    }

    @Override
    public void showDialog() {
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void onUpdateStatusFailed() {
        dismissDialog();
        if (acceptDeliveryBtn.getVisibility() == View.VISIBLE)
            acceptDeliveryBtn.setEnabled( true );
    }

    @Override
    public void onUpdateDeliveryStatus(String updateResultID, Map <String, Object> result) {
        // when user click on Accept Btn...
        if (updateResultID.toUpperCase().equals( "PROCESS" )){
            acceptDeliveryBtn.setVisibility( View.GONE );
            if (acceptOrderThread != null && acceptOrderThread.isAlive()){
                acceptOrderThread.suspend();
            }
            //  Check whether Order accepted by you or not?
            String resultUID = result.get( "UID" ).toString();
            if(resultUID.equals( currentUser.getUid() )){
                // Update Order in the Product...
                Map<String, Object> updateMap = new HashMap();
                updateMap.put( "delivery_by_uid", currentUser.getUid() );
                updateMap.put( "delivery_by_name", USER_ACCOUNT.getUser_name() );
                updateMap.put( "delivery_by_mobile", USER_ACCOUNT.getUser_mobile() );
                updateMap.put( "delivery_id", deliveryID );
                updateMap.put( "delivery_vehicle_no", USER_ACCOUNT.getUser_vehicle_number() );
//                updateMap.put( "delivery_status", "PROCESS" );
                orderQuery.updateOrderOnShop( this, shopID, orderID, updateMap, 1);

                // Set UI
                onAcceptedOrder( result.get( "verify_otp" ).toString() );
            }
            else{
                // Show Alert Message...
                // TODO: Alert Message....
            }

        } else
            // when Shop Keeper update : OUT_FOR_DELIVERY...
        if(updateResultID.toUpperCase().equals( "PICKED" )){
            buttonCompleteDelivery.setVisibility( View.VISIBLE );
            buttonCancelDelivery.setVisibility( View.GONE );
            acceptDeliveryBtn.setVisibility( View.GONE );
            layoutOTP.setVisibility( View.VISIBLE );
            tvShopOtp.setVisibility( View.GONE );
            etCustomerOtp.setVisibility( View.VISIBLE );
            // Set Data...
            tvOtpTitle.setText( "Enter OTP" );
            tvOtpGuide.setText( "Ask OTP from user." );
        }else
            // when ANY Delivery has been success | click on Complete Btn...
        if(updateResultID.toUpperCase().equals( "SUCCESS" )){

        }else
            // if any Customer Cancel Order ...
        if(updateResultID.toUpperCase().equals( "CANCELLED" )){

        }else
            // when user click on CANCEL Btn...
        if(updateResultID.toUpperCase().equals( "ACCEPTED" )) {

        }
    }

    // Set UI after Accept Order to Delivery...!
    @Override
    public void onAcceptedOrder(String otp) {
        buttonCompleteDelivery.setVisibility( View.GONE );
        buttonCancelDelivery.setVisibility( View.VISIBLE );
        acceptDeliveryBtn.setVisibility( View.GONE );
        layoutOTP.setVisibility( View.VISIBLE );

        tvShopOtp.setVisibility( View.VISIBLE );
        etCustomerOtp.setVisibility( View.GONE );
        // Set Data...
        tvOtpTitle.setText( "Shop OTP" );
        tvShopOtp.setText( otp );
        tvOtpGuide.setText( "Show this OTP at Shop" );
        dismissDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderID = null;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( getContext(), msg, Toast.LENGTH_SHORT ).show();
    }

    /**  Order Status
     *          1. WAITING - ( For Accept )
     *          2. ACCEPTED - ( Preparing )
     *          3. PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
     *          4. PROCESS  - When Any Delivery Boy Accept to Delivering...
     *          5. PICKED - ( On Delivery ) OUT_FOR_DELIVERY...
     *          6. SUCCESS - Success Full Delivered..!
     *          7. CANCELLED -  When Order has been cancelled by user...
     *          8. FAILED -  when PayMode Online and payment has been failed...
     *          9. PENDING - when Payment is Pending...
     *
     */

    private class LoadOrderData implements Runnable{

        private String orderID;
        private String shopId;
        private String CityCode;
        private GetOrderDetailsListener listener;

        public LoadOrderData( String orderID, String shopId, String CityCode, GetOrderDetailsListener listener) {
            this.orderID = orderID;
            this.shopId = shopId;
            this.CityCode = CityCode;
            this.listener = listener;
        }

        @Override
        public void run() {
            firebaseFirestore.collection( "SHOPS" )
                    .document( shopId ).collection( "ORDERS" )
                    .document( orderID )
                    .get()
                    .addOnCompleteListener( task -> {
                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();
                            // Assign new OrderListModel...
                            OrderListModel orderListModel = new OrderListModel();

                            orderListModel.setOrderID( documentSnapshot.get( "order_id" ).toString() );
                            orderListModel.setDeliveryStatus( documentSnapshot.get( "delivery_status" ).toString() );
                            orderListModel.setPayMode( documentSnapshot.get( "pay_mode" ).toString() );

                            orderListModel.setDeliveryCharge( documentSnapshot.get( "delivery_charge" ).toString() );
                            // billing_amounts
                            orderListModel.setBillingAmounts( documentSnapshot.get( "billing_amounts" ).toString() );
                            // total_amounts
                            orderListModel.setProductAmounts( documentSnapshot.get( "total_amounts" ).toString() );

                            orderListModel.setCustAuthID( documentSnapshot.get( "order_by_auth_id" ).toString() );
                            orderListModel.setCustName( documentSnapshot.get( "order_by_name" ).toString() );
                            orderListModel.setCustMobile( documentSnapshot.get( "order_by_mobile" ).toString() );

                            orderListModel.setShippingName( documentSnapshot.get( "order_accepted_by" ).toString() );
                            orderListModel.setShippingAddress( documentSnapshot.get( "order_delivery_address" ).toString() );
                            orderListModel.setShippingPinCode( documentSnapshot.get( "order_delivery_pin" ).toString() );

                            orderListModel.setOrderDate( documentSnapshot.get( "order_date" ).toString() );
                            orderListModel.setOrderDay( documentSnapshot.get( "order_day" ).toString() );
                            orderListModel.setOrderTime( documentSnapshot.get( "order_time" ).toString() );

//                            orderListModel.setDeliverySchedule( documentSnapshot.get( "delivery_schedule_time" ).toString() );

//                            long no_of_products = (long)documentSnapshot.get( "no_of_products" );

                            List<OrderProductsModel> orderSubList = (ArrayList <OrderProductsModel>) documentSnapshot.getData().get( "products_list" );

                            orderListModel.setOrderProductItemsList( orderSubList );

                            // add Model Item in the List...
                            listener.onReceiveDetails( orderListModel );

                        }else{
                            listener.onReceiveFailed();
                        }

                    } );
        }

    }

}