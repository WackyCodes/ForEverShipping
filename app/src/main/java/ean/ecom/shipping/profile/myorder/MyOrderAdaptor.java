package ean.ecom.shipping.profile.myorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ean.ecom.shipping.R;
import ean.ecom.shipping.main.order.CurrentOrderListModel;
import ean.ecom.shipping.main.order.OrderViewActivity;
import ean.ecom.shipping.other.StaticMethods;

import static ean.ecom.shipping.SetFragmentActivity.FRAGMENT_MY_ORDER;
import static ean.ecom.shipping.database.DBQuery.myOrdersList;
import static ean.ecom.shipping.other.StaticValues.ORDER_ACCEPTED;
import static ean.ecom.shipping.other.StaticValues.ORDER_PICKED;
import static ean.ecom.shipping.other.StaticValues.ORDER_SUCCESS;

/**
 * Created by Shailendra (WackyCodes) on 25/10/2020 17:48
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class MyOrderAdaptor extends RecyclerView.Adapter<MyOrderAdaptor.ViewHolder> {

    public MyOrderAdaptor() {
    }

    @NonNull
    @Override
    public MyOrderAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.my_order_list_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdaptor.ViewHolder holder, int position) {
        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconOrderStatus; // order_image
        private TextView textViewStatusText; // order_status_text_view
        private TextView textViewOrderID; // order_id_text_view
        private TextView textViewShopName; // order_shop_name_text
        private TextView textViewShopID; // order_shop_id_text_view
        private TextView textViewShopAddress; // order_shop_address_text_view
        private TextView textViewTime; // order_time_tv

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            iconOrderStatus = itemView.findViewById( R.id.order_image );
            textViewStatusText = itemView.findViewById( R.id.order_status_text_view );
            textViewOrderID = itemView.findViewById( R.id.order_id_text_view );
            textViewShopName = itemView.findViewById( R.id.order_shop_name_text );
            textViewShopID = itemView.findViewById( R.id.order_shop_id_text_view );
            textViewShopAddress = itemView.findViewById( R.id.order_shop_address_text_view );
            textViewTime = itemView.findViewById( R.id.order_time_tv );
        }

        private void setData(int position ){
            // Get Model Item...
            textViewTime.setText( "Few sec ago" );

            CurrentOrderListModel model = myOrdersList.get( position );
            String orderStatus = model.getDelivery_status();
            setIconOrderStatus( orderStatus );
            // set Data...
            textViewOrderID.setText( model.getOrder_id() );
            textViewShopName.setText( model.getShop_name() );
            textViewShopID.setText( model.getShop_id() );
            textViewShopAddress.setText( model.getShop_address() );

            itemView.setOnClickListener( v -> {
                Intent intent = new Intent(itemView.getContext(), OrderViewActivity.class );
                intent.putExtra( "MODEL_INDEX", position );
                intent.putExtra( "MODEL_TYPE", FRAGMENT_MY_ORDER );
                itemView.getContext().startActivity( intent );
            } );

            // Set Time for success...
            if (orderStatus.toUpperCase().equals( ORDER_SUCCESS )){
                if (model.getDelivery_timestamp() != null)
                    textViewTime.setText( StaticMethods.getTimeFromNow( model.getDelivery_timestamp().toDate() ) );
            }else
//                if(orderStatus.toUpperCase().equals( ORDER_PICKED )){
                if (model.getAccepted_pickup_timestamp() != null){
                    textViewTime.setText( StaticMethods.getTimeFromNow( model.getAccepted_pickup_timestamp().toDate() ) );
            }else{
                textViewTime.setText( "Few sec ago" );
            }

        }

        @SuppressLint("UseCompatLoadingForColorStateLists")
        private void setIconOrderStatus(String orderStatus){
            if (orderStatus.toUpperCase().equals( ORDER_SUCCESS )){
                iconOrderStatus.setImageResource( R.drawable.ic_baseline_check_circle_24 );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    iconOrderStatus.setImageTintList( itemView.getResources().getColorStateList( R.color.colorGreen ) );
                    textViewStatusText.setTextColor( itemView.getResources().getColor( R.color.colorGreen ) );
                }
                textViewStatusText.setText( "Successful" );
            }
            else if( orderStatus.toUpperCase().equals( ORDER_PICKED ) ){
                iconOrderStatus.setImageResource( R.drawable.ic_baseline_directions_bike_24 );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    iconOrderStatus.setImageTintList( itemView.getResources().getColorStateList( R.color.colorPrimary ) );
                    textViewStatusText.setTextColor( itemView.getResources().getColor( R.color.colorPrimary ) );
                }
                textViewStatusText.setText( "On Way" );
            }
            else if( orderStatus.toUpperCase().equals( ORDER_ACCEPTED ) ){
                iconOrderStatus.setImageResource( R.drawable.ic_baseline_schedule_24 );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    iconOrderStatus.setImageTintList( itemView.getResources().getColorStateList( R.color.colorBlue ) );
                    textViewStatusText.setTextColor( itemView.getResources().getColor( R.color.colorBlue ) );
                }
                textViewStatusText.setText( "Ready to Delivery" );
            }
        }



    }



}
