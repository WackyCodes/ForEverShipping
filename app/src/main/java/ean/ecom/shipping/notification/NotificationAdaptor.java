package ean.ecom.shipping.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.GeoPoint;

import ean.ecom.shipping.R;
import ean.ecom.shipping.main.order.CurrentOrderListModel;
import ean.ecom.shipping.main.order.OrderViewFragment;

import static ean.ecom.shipping.database.DBQuery.orderNotificationList;
import static ean.ecom.shipping.other.StaticValues.NOTIFICATION_TYPE_ORDERS;

/**
 * Created by Shailendra (WackyCodes) on 15/10/2020 17:11
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.OrderViewHolder> {

    private OnNotificationUpdater onNotificationUpdater;
    private int notificationType;


    public NotificationAdaptor(OnNotificationUpdater onNotificationUpdater, int notificationType) {
        this.onNotificationUpdater = onNotificationUpdater;
        this.notificationType = notificationType;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (notificationType == NOTIFICATION_TYPE_ORDERS){
            View orderView = LayoutInflater.from( parent.getContext() )
                    .inflate( R.layout.notification_order_layout_item, parent, false );
            return new OrderViewHolder( orderView );
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (notificationType == NOTIFICATION_TYPE_ORDERS)
            holder.setData( position );
    }

    @Override
    public int getItemCount() {
        if (notificationType == NOTIFICATION_TYPE_ORDERS)
            return orderNotificationList.size();
        else
//            return notificationModelList.size();
        return 0;
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

            CurrentOrderListModel orderModel = orderNotificationList.get( position );

            tvShopName.setText( "New Order from " + orderModel.getShop_name() );
            tvShopAddress.setText( orderModel.getShop_address() );
            tvShippingAddress.setText( orderModel.getShipping_address() );

            itemView.setOnClickListener( v-> {
                // on Order Click...
                onNotificationUpdater.onNotificationClick(
                        new OrderViewFragment( orderModel,  null ) );
            } );

            tvGetShopDirection.setOnClickListener( v -> {
                setTvGetShopDirection(orderModel.getShop_geo_point(), orderModel.getShop_name());
            });

            tvGetShippingDirection.setOnClickListener( v -> {
                setTvGetShippingDirection( orderModel.getShipping_geo_point(), orderModel.getShipping_address() );
            } );

        }


        private void setTvGetShopDirection(GeoPoint shopDirection, String title){


        }

        private void setTvGetShippingDirection(GeoPoint shippingDirection, String title){


        }

    }



}
