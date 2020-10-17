package ean.ecom.shipping.main.order;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import java.util.HashMap;
import java.util.Map;

import ean.ecom.shipping.R;
import ean.ecom.shipping.database.OrderQuery;

import static ean.ecom.shipping.database.DBQuery.currentUser;
import static ean.ecom.shipping.other.StaticValues.USER_ACCOUNT;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_COMPLETE_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_CURRENT_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_NOTIFICATION;

public class OrderViewFragment extends Fragment implements GetOrderDetailsListener {

    public OrderViewFragment(int viewID, String orderID, String shopID, String deliveryID) {
        // Required empty public constructor
        this.deliveryID = deliveryID;
        this.viewID = viewID;
        this.orderID = orderID;
        this.shopID = shopID;
    }

    private int viewID;
    private String orderID;
    private String shopID;
    private String deliveryID;

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
        setDefaultLayout();
        setOnButtonClick(view);
        // Add Listener...
//        OrderQuery.onOrderUpdateListener( deliveryID, this );

        return view;
    }

    private void setDefaultLayout(){
        switch (viewID){
            case VIEW_ORDER_FROM_NOTIFICATION:
                buttonCompleteDelivery.setVisibility( View.INVISIBLE );
                acceptDeliveryBtn.setVisibility( View.VISIBLE );
                buttonCancelDelivery.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.GONE );
                break;
            case VIEW_ORDER_FROM_CURRENT_ORDERS:
                buttonCompleteDelivery.setVisibility( View.VISIBLE );
                buttonCancelDelivery.setVisibility( View.VISIBLE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.VISIBLE );
                break;
            case VIEW_ORDER_FROM_COMPLETE_ORDERS:
                buttonCompleteDelivery.setVisibility( View.INVISIBLE );
                buttonCancelDelivery.setVisibility( View.GONE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.GONE );
                getShippingDirectionBtn.setVisibility( View.INVISIBLE );
                break;
            default:
                break;
        }
    }

    private void setOnButtonClick(View view){
        acceptDeliveryBtn.setOnClickListener( v->{
            acceptDeliveryBtn.setEnabled( false );
            onAcceptButtonClick();
        } );
    }

    // Set Layout///
    private void setLayoutVisibility(){

    }

    /// Set Data...
    private void setOrderData(){

    }

    private void onAcceptButtonClick(){
        showDialog();
        Map<String, Object> updateMap = new HashMap();
        updateMap.put( "delivery_status", "PROCESS" );
        acceptOrderThread = new Thread( new OrderQuery.UpdateOrderStatus(updateMap, orderID, USER_ACCOUNT.getUser_city_code(), this) );
        acceptOrderThread.start();
    }

    @Override
    public void onReceiveDetails() {
        dismissDialog();
    }

    @Override
    public void onReceiveFailed() {
        dismissDialog();
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
                updateMap.put( "delivery_status", "PROCESS" );
                OrderQuery.updateOrderOnShop( this, shopID, orderID, updateMap, 1);

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
    public void onAcceptedOrder(String otp) {
        buttonCompleteDelivery.setVisibility( View.GONE );
        buttonCancelDelivery.setVisibility( View.VISIBLE );
        acceptDeliveryBtn.setVisibility( View.GONE );
        layoutOTP.setVisibility( View.VISIBLE );

        etCustomerOtp.setVisibility( View.GONE );
        // Set Data...
        tvOtpTitle.setText( "Shop OTP" );
        tvShopOtp.setText( otp );
        tvOtpGuide.setText( "Show this OTP at Shop" );
        dismissDialog();
    }

    @Override
    public String getOrderID() {
        if (orderID != null)
            return orderID;
        else return null;
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



}