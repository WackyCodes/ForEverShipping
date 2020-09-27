package ean.ecom.shipping.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import ean.ecom.shipping.R;

public class ShippingOrderFragment extends Fragment {


    public ShippingOrderFragment() {
        // Required empty public constructor
    }
    public static ShippingOrderFragment newInstance(String param1, String param2) {
        ShippingOrderFragment fragment = new ShippingOrderFragment();
        Bundle args = new Bundle();
        args.putString( "ARG_PARAM1", param1 );
        args.putString( "ARG_PARAM2", param2 );
        fragment.setArguments( args );
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
//            mParam1 = getArguments().getString( ARG_PARAM1 );
//            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    // Layout Variables...
    private RecyclerView shippingProductRecycler;
    private ImageView upDownImageBtn;

    private boolean isUp = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_on_delivery_product, container, false );

        shippingProductRecycler = view.findViewById( R.id.shipping_product_recycler );
        upDownImageBtn = view.findViewById( R.id.up_image_view_btn );

//        // Set Layout And Adaptor...
//        LinearLayoutManager layoutManager = new LinearLayoutManager( getContext() );
//        layoutManager.setOrientation( RecyclerView.VERTICAL );
//        shippingProductRecycler.setLayoutManager( layoutManager );
//        // Adaptor...
//        ShippingOrderAdaptor adaptor = new ShippingOrderAdaptor( new ArrayList <OrderDetailsModel>() );
//        shippingProductRecycler.setAdapter( adaptor );
//        // Notify..
//        adaptor.notifyDataSetChanged();

        upDownImageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUp = !isUp;
                upDownImageBtn.setEnabled( false );
                setUpDownImageBtn();
            }
        } );

        return view;
    }

    private void setUpDownImageBtn(){
        if (isUp){
            upDownImageBtn.setImageResource( R.drawable.ic_baseline_keyboard_arrow_down_24 );
        }else{
            upDownImageBtn.setImageResource( R.drawable.ic_outline_keyboard_arrow_up_24 );
        }
        upDownImageBtn.setEnabled( true );
    }




}