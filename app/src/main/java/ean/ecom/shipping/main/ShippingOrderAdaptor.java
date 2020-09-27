package ean.ecom.shipping.main;

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
 * Created by Shailendra (WackyCodes) on 26/09/2020 17:31
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
class ShippingOrderAdaptor extends RecyclerView.Adapter<ShippingOrderAdaptor.ViewHolder> {

    private List<OrderDetailsModel> orderDetailsModelList;

    private MapAction mapActionFragment;

    public ShippingOrderAdaptor(List <OrderDetailsModel> orderDetailsModelList, MapAction mapActionFragment) {
        this.orderDetailsModelList = orderDetailsModelList;
        this.mapActionFragment = mapActionFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.crr_order_list_item, parent, false  );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData( position );
    }

    @Override
    public int getItemCount() {
        return orderDetailsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements MapAction.OnDrawMarker {

        private TextView orderText;
        private TextView shopAddressText;
        private TextView shippingAddressText;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            orderText = itemView.findViewById( R.id.order_id_text );
            shopAddressText = itemView.findViewById( R.id.shop_address_text );
            shippingAddressText = itemView.findViewById( R.id.shipping_address_text );

        }

        private void setData(int position){
            OrderDetailsModel model = orderDetailsModelList.get( position );

            orderText.setText( "Order ID: " + model.getOrderID() );
            shopAddressText.setText( model.getShopAddress() );
            shippingAddressText.setText( model.getShippingAddress() );

        }

        @Override
        public void onDrawingPathLine(MapAction mapAction, GeoPoint fromPoints, GeoPoint toPoints) {
            mapAction.drawPathLine( fromPoints, toPoints );
        }

        @Override
        public void onSetMarker(MapAction mapAction, GeoPoint markPoints) {
            if (markPoints != null)
                mapAction.setMapMarker( markPoints.getLatitude(), markPoints.getLongitude() );
        }
    }



}
