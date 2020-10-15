package ean.ecom.shipping.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import ean.ecom.shipping.R;

/**
 * Created by Shailendra (WackyCodes) on 15/10/2020 17:11
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.OrderViewHolder> {

    List<NotificationModel> notificationModelList;

//    public NotificationAdaptor(List <NotificationModel> notificationModelList) {
//        this.notificationModelList = notificationModelList;
//    }

    public NotificationAdaptor() {
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.notification_order_layout_item, parent, false );
        return new OrderViewHolder( orderView );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName;
        private TextView tvShopAddress;
        private TextView tvShippingAddress;
        private TextView tvGetShopDirection;
        private TextView tvGetShippingDirection;

        public OrderViewHolder(@NonNull View itemView) {
            super( itemView );

            tvShopName = itemView.findViewById( R.id.order_id_name_text );
            tvShopAddress = itemView.findViewById( R.id.shop_address_text );
            tvShippingAddress = itemView.findViewById( R.id.shipping_address_text );
            tvGetShopDirection = itemView.findViewById( R.id.get_shop_direction );
            tvGetShippingDirection = itemView.findViewById( R.id.get_shipping_direction );

        }


        private void setData(int position){

            NotificationModel.NotificationOrderModel orderModel = notificationModelList.get( position ).getNotificationOrderModel();

            tvShopName.setText( "New Order from " + orderModel.getOrderShopName() );
            tvShopAddress.setText( orderModel.getOrderShopAddress() );
            tvShippingAddress.setText( orderModel.getOrderShippingAddress() );

            itemView.setOnClickListener( v-> {
                // OnClick...

            } );

            tvGetShopDirection.setOnClickListener( v -> {
                setTvGetShopDirection(orderModel.getOrderShopGeoPoint(), orderModel.getOrderShopName());
            });

            tvGetShippingDirection.setOnClickListener( v -> {
                setTvGetShippingDirection( orderModel.getOrderShippingGeoPoint(), orderModel.getOrderShippingAddress() );
            } );

        }


        private void setTvGetShopDirection(GeoPoint shopDirection, String title){


        }

        private void setTvGetShippingDirection(GeoPoint shippingDirection, String title){


        }

    }



}
