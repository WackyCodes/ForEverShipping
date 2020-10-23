package ean.ecom.shipping.main.order;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
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

import ean.ecom.shipping.OnFragmentSetListener;
import ean.ecom.shipping.R;
import ean.ecom.shipping.database.OrderQuery;
import ean.ecom.shipping.other.DialogsClass;
import ean.ecom.shipping.other.StaticMethods;

import static ean.ecom.shipping.database.DBQuery.currentOrderListModelList;
import static ean.ecom.shipping.database.DBQuery.firebaseFirestore;
import static ean.ecom.shipping.database.DBQuery.orderNotificationList;
import static ean.ecom.shipping.main.MainMapsFragment.shippingOrderAdaptor;
import static ean.ecom.shipping.notification.NotificationFragment.orderNotificationAdaptor;
import static ean.ecom.shipping.other.StaticValues.ORDER_ACCEPTED;
import static ean.ecom.shipping.other.StaticValues.ORDER_CANCELLED;
import static ean.ecom.shipping.other.StaticValues.ORDER_CODE_ACCEPTED;
import static ean.ecom.shipping.other.StaticValues.ORDER_CODE_CANCEL;
import static ean.ecom.shipping.other.StaticValues.ORDER_PICKED;
import static ean.ecom.shipping.other.StaticValues.ORDER_PROCESS;
import static ean.ecom.shipping.other.StaticValues.ORDER_SUCCESS;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_COMPLETE_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_CURRENT_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_NOTIFICATION;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_PICKED_ORDERS;

public class OrderViewFragment extends Fragment implements GetOrderDetailsListener, OnFragmentSetListener {

    public OrderViewFragment(OnFragmentSetListener parentListener, CurrentOrderListModel currentOrderListModel ) {
        // Required empty public constructor
        this.parentListener = parentListener;

        this.currentOrderListModel = currentOrderListModel;
        this.deliveryID = currentOrderListModel.getDelivery_id();
        this.orderID = currentOrderListModel.getOrder_id();
        this.shopID = currentOrderListModel.getShop_id();
        this.shopName = currentOrderListModel.getShop_name();
    }

    private CurrentOrderListModel currentOrderListModel;
    private OnFragmentSetListener parentListener;

    private String orderID;
    private String shopID;
    private String shopName;
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

    private TextView tvSuccessDelivered; /// order_success_text

    private ProgressDialog dialog;
    private Thread acceptOrderThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_order_view, container, false );
        orderQuery = new OrderQuery( this );
        dialog = new ProgressDialog( getContext() );
        dialog.setTitle( "Please wait..." );
        dialog.setCancelable( false );

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
        tvSuccessDelivered = view.findViewById( R.id.order_success_text );

        // set Default View....
        Thread loadDataThread = new Thread(new LoadOrderData( orderID, shopID, this ));
        loadDataThread.start();
        showDialog();

        setOnButtonClick(view);

        return view;
    }

    private void setOnButtonClick(View view){
        // accept order button clicked
        acceptDeliveryBtn.setOnClickListener( v->{
            acceptDeliveryBtn.setEnabled( false );
            showDialog();
            Map<String, Object> updateMap = new HashMap();
            updateMap.put( "delivery_status", ORDER_PROCESS );
            updateMap.put( "delivery_by_uid", USER_ACCOUNT.getUser_mobile() );
            acceptOrderThread = new Thread( new OrderQuery.UpdateOrderStatus(updateMap, deliveryID, USER_ACCOUNT.getUser_city_code(), ORDER_CODE_ACCEPTED, this) );
            acceptOrderThread.start();
        } );
        // Cancel Order Button Clicked...
        buttonCancelDelivery.setOnClickListener( v->{
            buttonCancelDelivery.setEnabled( false );
            showDialog();
            Map<String, Object> updateMap = new HashMap();
            updateMap.put( "delivery_status", ORDER_ACCEPTED );
            updateMap.put( "delivery_by_uid", null );
            acceptOrderThread = new Thread( new OrderQuery.UpdateOrderStatus(updateMap, deliveryID, USER_ACCOUNT.getUser_city_code(), ORDER_CODE_CANCEL, this) );
            acceptOrderThread.start();
        } );
        // on Complete Order Button Clicked...
        buttonCompleteDelivery.setOnClickListener( v->{
            // check...
            if (TextUtils.isEmpty( etCustomerOtp.getText().toString() )){
                etCustomerOtp.setError( "Required!" );
                return;
            }
            if (etCustomerOtp.getText().toString().length() < 4){
                etCustomerOtp.setError( "Invalid OTP!" );
                return;
            }
            // call method...
            showDialog();
            buttonCompleteDelivery.setEnabled( false );
            orderQuery.setCustomerOTPVerify( this, shopID, orderID, etCustomerOtp.getText().toString().trim() );
        } );

        // Direction : Shop Direction button clicked...
        getShopDirectionBtn.setOnClickListener( v->{
            showToast( "Not found!" ); // TODO : Direction...
        } );
        // Direction : Customer Direction button clicked...
        getShippingDirectionBtn.setOnClickListener( v->{
            showToast( "Not found!" ); // TODO: Direction...
        } );
    }

    /// Set Data...
    private void setOrderData(){
        String dStatus = orderModel.getDeliveryStatus();
        if (dStatus.equals( ORDER_ACCEPTED )){
            setDefaultLayout( VIEW_ORDER_FROM_NOTIFICATION );
        }else if( dStatus.equals( ORDER_PICKED )){
            setDefaultLayout( VIEW_ORDER_PICKED_ORDERS );
        }else if( dStatus.equals( ORDER_SUCCESS )){
            setDefaultLayout( VIEW_ORDER_FROM_COMPLETE_ORDERS );
        }else if( dStatus.equals( ORDER_PROCESS )){
            setDefaultLayout( VIEW_ORDER_FROM_CURRENT_ORDERS );
            // Get OTP ..
            orderQuery.getOtpQuery( orderModel.getDeliveryID() );
        }else{
            // Default...
            acceptDeliveryBtn.setVisibility( View.VISIBLE );
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
                acceptDeliveryBtn.setEnabled( true );
                layoutOTP.setVisibility( View.GONE );
                tvOrderID.setText( orderID );
                tvSuccessDelivered.setVisibility( View.GONE );
                break;
            case VIEW_ORDER_FROM_CURRENT_ORDERS:
                buttonCompleteDelivery.setVisibility( View.GONE );
                buttonCancelDelivery.setVisibility( View.VISIBLE );
                buttonCancelDelivery.setEnabled( true );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.VISIBLE );
                etCustomerOtp.setVisibility( View.GONE );
                tvShopOtp.setVisibility( View.VISIBLE );
                tvSuccessDelivered.setVisibility( View.GONE );
                break;
            case VIEW_ORDER_FROM_COMPLETE_ORDERS:
                buttonCompleteDelivery.setVisibility( View.GONE );
                buttonCancelDelivery.setVisibility( View.GONE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.GONE );
                tvSuccessDelivered.setVisibility( View.VISIBLE );
                getShippingDirectionBtn.setVisibility( View.INVISIBLE );
                break;
            case VIEW_ORDER_PICKED_ORDERS:
                buttonCompleteDelivery.setVisibility( View.VISIBLE );
                buttonCompleteDelivery.setEnabled( true );
                buttonCancelDelivery.setVisibility( View.GONE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.VISIBLE );
                tvShopOtp.setVisibility( View.GONE );
                etCustomerOtp.setVisibility( View.VISIBLE );
                tvSuccessDelivered.setVisibility( View.GONE );
                break;
            default:
                acceptDeliveryBtn.setVisibility( View.VISIBLE );
                break;
        }
    }

    @Override
    public void onReceiveDetails( OrderListModel orderListModel ) {
        orderModel = orderListModel;

        tvOrderID.setText( orderModel.getOrderID() );
        tvShopName.setText( shopName );
        tvShopId.setText( shopID );
        tvShopAddress.setText( orderModel.getShippingAddress() );
        // Shipping...
        tvShippingAddress.setText( orderModel.getShippingAddress() );

        /**  Add Listener... Only Call when Order not success! */
        if (! orderModel.getDeliveryStatus().equals( ORDER_SUCCESS )){
            orderQuery.onOrderUpdateListener( this, deliveryID );
        }
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
        if (!dialog.isShowing()){
            dialog.show();
        }
    }

    @Override
    public void dismissDialog() {
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onUpdateStatusFailed() {
        dismissDialog();
        showToast( "Failed! Check your internet connection and try again!" );
        if (acceptDeliveryBtn.getVisibility() == View.VISIBLE)
            acceptDeliveryBtn.setEnabled( true );
        if (buttonCancelDelivery.getVisibility() == View.VISIBLE)
            buttonCancelDelivery.setEnabled( true );
    }

    @Override
    public void onUpdateDeliveryStatus(String updateResultID, Map <String, Object> result) {
        // when user click on Accept Btn...
        if (updateResultID.toUpperCase().equals( ORDER_PROCESS )){
            acceptDeliveryBtn.setVisibility( View.GONE );
            if (acceptOrderThread != null && acceptOrderThread.isAlive()){
                acceptOrderThread.suspend();
            }
            orderID = orderModel.getOrderID();
            //  Check whether Order accepted by you or not?
//            String resultUID = result.get( "UID" ).toString();
            if(result.get( "delivery_by_uid" ).toString().equals( USER_ACCOUNT.getUser_mobile() )){

                if (getIndexOfCurrentListModel( orderID ) == -1){
                    // Update Order in the Product...
                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put( "delivery_by_uid", USER_ACCOUNT.getUser_mobile() );
                    updateMap.put( "delivery_by_name", USER_ACCOUNT.getUser_name() );
                    updateMap.put( "delivery_by_mobile", USER_ACCOUNT.getUser_mobile() );
                    updateMap.put( "delivery_id", deliveryID );

                    updateMap.put( "delivery_vehicle_no", USER_ACCOUNT.getUser_vehicle_number() );
                    // Update Order In the Shop Order List...
                    orderQuery.updateOrderOnShop( this, shopID, orderID, updateMap, 1);
                    // add Model into Current Order List...
                    orderQuery.addOrderInCurrentDelivery( this, currentOrderListModel, deliveryID, result.get( "verify_otp" ).toString(), false );
                }else{
                    onAcceptedOrder( result.get( "verify_otp" ).toString() );
                }
            }
            else{
                dismissDialog();
                acceptDeliveryBtn.setVisibility( View.INVISIBLE );
                // Show Alert Message...
//                showToast( "Not Matched" );
                DialogsClass.alertDialog( getContext(), "Order Accepted by Other!", "Sorry! Order has been assign to someone else..! Check other orders" )
                .setPositiveButton( "OK", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().finish();
                } ).show();
            }

            // Remove item from Notification List....
            for (CurrentOrderListModel model : orderNotificationList){
                if (model.getOrder_id().equals( orderID )){
                    orderNotificationList.remove( model );
                    if ( orderNotificationAdaptor != null ){
                        orderNotificationAdaptor.notifyDataSetChanged();
                    }
                    break;
                }
            }

        } else
            // when Shop Keeper update : OUT_FOR_DELIVERY...
        if(updateResultID.toUpperCase().equals( ORDER_PICKED )){
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
            // if any Customer Cancel Order ...
        if(updateResultID.toUpperCase().equals( ORDER_CANCELLED )) {
            // cancel.... by Customer... TODO: Set Push up Notification to Delivery Boy...
            DialogsClass.alertDialog( getContext(), "Order Cancelled!", " Order has been cancelled by customer, Now you don't need to pickup order!" )
                    .setPositiveButton( "OK", (dialog, which) -> {
                        dialog.dismiss();
                        getActivity().finish();
                    } ).show();

            Map<String, Object> updateMap = new HashMap();
            updateMap.put( "delivery_by_uid", null );
            updateMap.put( "delivery_by_name", null );
            updateMap.put( "delivery_by_mobile", null );
            updateMap.put( "delivery_vehicle_no", null );
//                updateMap.put( "delivery_status", "PROCESS" );
            orderQuery.updateOrderOnShop( this, shopID, orderID, updateMap, 4);
        }

    }

    @Override
    public void onRequestToCancelOrder() {
        Map<String, Object> updateMap = new HashMap();
        updateMap.put( "delivery_by_uid", null );
        updateMap.put( "delivery_by_name", null );
        updateMap.put( "delivery_by_mobile", null );
        updateMap.put( "delivery_vehicle_no", null );
        orderQuery.updateOrderOnShop( this, shopID, orderID, updateMap, 3);
    }

    @Override
    public void onCancelledOrder(boolean isCancelByCustomer) {
        if(isCancelByCustomer){ // Cancel By Customer...
            // Delete From Public Delivery ID...
            orderQuery.deleteDeliveryIdQuery(this, deliveryID, true );
            // Set Back Process...
            parentListener.onBackPressed( -1, null );
        }else{ // Cancel By Delivery Boy...
            // Set UI...
            setDefaultLayout( VIEW_ORDER_FROM_NOTIFICATION );
            orderModel.setDeliveryStatus( ORDER_ACCEPTED );

            // Remove from MyOrder Document...
            orderQuery.deleteMyDeliveryID( this, deliveryID, USER_ACCOUNT.getUser_mobile() );

            // Remove from Current Order list...
            int rmIndex =  getIndexOfCurrentListModel( orderID );
            if ( rmIndex >= 0){
                currentOrderListModelList.remove( rmIndex );
                // NotifyDataSetChanged...
                if (shippingOrderAdaptor!= null){
                    shippingOrderAdaptor.notifyDataSetChanged();
                }
            }
            dismissDialog();
        }
    }

    // Set Order Complete Steps...
    @Override
    public void onOrderCompleteNextStep(int step){
        switch (step){
            case 0: // successfully Verified OTP...
                // Request to Update in shop Order List...
                Map<String, Object> updateMap = new HashMap();
                updateMap.put( "delivery_status", ORDER_SUCCESS );
                updateMap.put( "delivery_timestamp", StaticMethods.getCrrTimestamp() );
                orderQuery.updateOrderOnShop( this, shopID, orderID, updateMap, 2);
                break;
            case 1: // successfully Update in shop Order List...
                //  Request to Update into My Delivery document...
                currentOrderListModel.setDelivery_status( ORDER_SUCCESS );
                orderQuery.addOrderInCurrentDelivery( this, currentOrderListModel, deliveryID, null, true );
                break;
            case 2: // Success Update in Delivery List...
                // Request to Delete Delivery Document...
                orderQuery.deleteDeliveryIdQuery(this, deliveryID, false);
                break;
            case 3: // Success delete temp Delivery Document...
                dismissDialog();
                // TODO: Send to User Success Notification...
                // set UI...
                setDefaultLayout( VIEW_ORDER_FROM_COMPLETE_ORDERS );
                int crrListIndex = getIndexOfCurrentListModel( orderID );
                if (crrListIndex >= 0){
                    currentOrderListModelList.get( crrListIndex ).setDelivery_status( ORDER_SUCCESS );
                }
//                else{
//                    currentOrderListModelList.add( currentOrderListModel );
//                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onOrderCompleteFailed(int failedStep){
        switch (failedStep){
            case 1: // Failed to update at shop Order list...
                showToast( "Please Check your Internet Connection !" );
                // Retry...Request to Update in shop Order List...
                Map<String, Object> updateMap = new HashMap();
                updateMap.put( "delivery_status", ORDER_SUCCESS );
                updateMap.put( "delivery_timestamp", StaticMethods.getCrrTimestamp() );
                orderQuery.updateOrderOnShop( this, shopID, orderID, updateMap, 2);
                break;
            case 2: // Failed to update at Delivery list...
                showToast( "your Internet Connection is very slow... !" );
                // retry...Request to Update into My Delivery document...
                orderModel.setDeliveryStatus( ORDER_SUCCESS );
                currentOrderListModel.setDelivery_status( ORDER_SUCCESS );
                orderQuery.addOrderInCurrentDelivery( this, currentOrderListModel, deliveryID, null, true );
                break;
            case 3: // Failed to delete temp Delivery Document..
                dismissDialog();
                // next step...
                // TODO : Send to User Success Notification...

                // set UI...
                setDefaultLayout( VIEW_ORDER_FROM_COMPLETE_ORDERS );
                int crrListIndex = getIndexOfCurrentListModel( orderID );
                if (crrListIndex >= 0){
                    currentOrderListModelList.get( crrListIndex ).setDelivery_status( ORDER_SUCCESS );
                }
//                else{
//                    currentOrderListModelList.add( currentOrderListModel );
//                }
                // Request to Delete Delivery Document...
                orderQuery.deleteDeliveryIdQuery(this, deliveryID, true);
            case 0: // Failed to Verify OTP...
                // Called onCustomerOTPNotVerified() method from...
            default:
                break;
        }
    }

    // Set UI after Accept Order to Delivery...!
    @Override
    public void onAcceptedOrder(String otp) {
        setDefaultLayout( VIEW_ORDER_FROM_CURRENT_ORDERS );
        // Set Data...
        tvOtpTitle.setText( "Shop OTP" );
        tvShopOtp.setText( otp );
        tvOtpGuide.setText( "Show this OTP at Shop" );
        dismissDialog();
    }

    @Override
    public void onCustomerOTPNotVerified(boolean isFailed) {
        dismissDialog();
        if (isFailed){
           showToast( "Verification Failed! Please Check your internet Connection and try again!" );
        }else{
            showToast( "OTP Not matched! Ask correct OTP from customer." );
            etCustomerOtp.setError( "Not Matched!" );
        }
        buttonCompleteDelivery.setEnabled( true );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderID = null;
    }

    @Override
    public void showToast(String msg) {
        parentListener.showToast( msg );
    }

    private int getIndexOfCurrentListModel(String orderID){
//        currentOrderListModelList.add( currentOrderListModel );
        for (CurrentOrderListModel model : currentOrderListModelList){
            if (orderID.equals( model.getOrder_id() )){
                return currentOrderListModelList.indexOf( model );
            }
        }
        return -1;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void onBackPressed(int From, String backTitle) {

    }

    @Override
    public void setNextFragment(Fragment fragment) {

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
        private GetOrderDetailsListener listener;

        public LoadOrderData( String orderID, String shopId, GetOrderDetailsListener listener) {
            this.orderID = orderID;
            this.shopId = shopId;
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