package ean.ecom.shipping.main.order;

import android.app.Dialog;
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

import ean.ecom.shipping.R;

import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_COMPLETE_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_CURRENT_ORDERS;
import static ean.ecom.shipping.other.StaticValues.VIEW_ORDER_FROM_NOTIFICATION;

public class OrderViewFragment extends Fragment implements GetOrderDetailsListener {

    public OrderViewFragment(int viewID, String orderID, String shopID) {
        // Required empty public constructor
        this.viewID = viewID;
        this.orderID = orderID;
        this.shopID = shopID;
    }

    private int viewID;
    private String orderID;
    private String shopID;

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

    private Button buttonCompleteOrder; // order_complete_button
    private TextView acceptDeliveryBtn; /// accept_delivery_text

    private ProgressDialog dialog;

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

        buttonCompleteOrder = view.findViewById( R.id.order_complete_button );
        acceptDeliveryBtn = view.findViewById( R.id.accept_delivery_text );

        // set Default View....
        setDefaultLayout();

        return view;
    }

    private void setDefaultLayout(){
        switch (viewID){
            case VIEW_ORDER_FROM_NOTIFICATION:
                buttonCompleteOrder.setVisibility( View.INVISIBLE );
                acceptDeliveryBtn.setVisibility( View.VISIBLE );
                layoutOTP.setVisibility( View.GONE );
                break;
            case VIEW_ORDER_FROM_CURRENT_ORDERS:
                buttonCompleteOrder.setVisibility( View.VISIBLE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.VISIBLE );
                break;
            case VIEW_ORDER_FROM_COMPLETE_ORDERS:
                buttonCompleteOrder.setVisibility( View.INVISIBLE );
                acceptDeliveryBtn.setVisibility( View.GONE );
                layoutOTP.setVisibility( View.GONE );
                getShippingDirectionBtn.setVisibility( View.INVISIBLE );
                break;
            default:
                break;
        }
    }

    // Set Layout///
    private void setLayoutVisibility(){

    }

    /// Set Data...
    private void setOrderData(){

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


}