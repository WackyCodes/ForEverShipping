package ean.ecom.shipping.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import ean.ecom.shipping.OnFragmentSetListener;
import ean.ecom.shipping.R;
import ean.ecom.shipping.main.order.CurrentOrderListModel;
import ean.ecom.shipping.main.order.CurrentOrderUpdateListener;
import ean.ecom.shipping.main.order.OrderViewActivity;
import ean.ecom.shipping.other.StaticValues;

import static ean.ecom.shipping.SetFragmentActivity.FRAGMENT_ORDER_VIEW;
import static ean.ecom.shipping.database.DBQuery.getMyCollectionRef;

/**
 * Created by Shailendra (WackyCodes) on 26/09/2020 17:31
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ShippingOrderAdaptor extends RecyclerView.Adapter<ShippingOrderAdaptor.ViewHolder> {

    private List<CurrentOrderListModel> currentOrderListModelList;
    private ListenerRegistration orderUpdateLR;
    private OnFragmentSetListener parentListener;
    private CurrentOrderUpdateListener orderUpdateListener;

    private MapAction mapActionFragment;

    public ShippingOrderAdaptor( OnFragmentSetListener parentListener, CurrentOrderUpdateListener orderUpdateListener, List <CurrentOrderListModel> currentOrderListModelList, MapAction mapActionFragment ) {
        this.parentListener = parentListener;
        this.orderUpdateListener = orderUpdateListener;
        this.currentOrderListModelList = currentOrderListModelList;
        this.mapActionFragment = mapActionFragment;
        OrderUpdateListener();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.crr_order_list_item, parent, false  );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CurrentOrderListModel model = currentOrderListModelList.get( position );
        holder.setData( model, position );
    }

    @Override
    public int getItemCount() {
        return currentOrderListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements MapAction.OnDrawMarker {

        private TextView orderText;
        private TextView shopAddressText;
        private TextView shippingAddressText;

        private TextView getShopDirection;
        private ImageView viewOrderDetailsBtn;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            orderText = itemView.findViewById( R.id.order_id_text );
            shopAddressText = itemView.findViewById( R.id.shop_address_text );
            shippingAddressText = itemView.findViewById( R.id.shipping_address_text );

            getShopDirection = itemView.findViewById( R.id.get_shop_direction );
            viewOrderDetailsBtn = itemView.findViewById( R.id.view_details_image_btn );

        }

        private void setData( final CurrentOrderListModel model, int position){

            orderText.setText( "Order ID: " + model.getDelivery_id() );
            shopAddressText.setText( model.getShop_address() );
            shippingAddressText.setText( model.getShipping_address() );


            // Set Visibility of Shop Direction Button...
            if (model.getDelivery_status() != null && model.getDelivery_status().toUpperCase().equals( "PROCESS" )){
                getShopDirection.setVisibility( View.VISIBLE );
                // If Order Not picked Yet..! ( orderStatus == ACCEPTED )
                onSetMarker( mapActionFragment, model.getShop_geo_point(), model.getShop_address() );
            }
            else{
                getShopDirection.setVisibility( View.INVISIBLE );
                // If Order picked and out for delivery..! ( orderStatus == PICKED )
                if (model.getShipping_geo_point() != null){
                    onSetMarker( mapActionFragment, model.getShipping_geo_point(), model.getShipping_address() );
                }
            }

            // Get Shop Direction... From My Location...!
            getShopDirection.setOnClickListener( v -> onDrawingPathLine( mapActionFragment, StaticValues.MY_GEO_POINTS, model.getShop_geo_point() ) );
            // Order Details..
            viewOrderDetailsBtn.setOnClickListener( v -> {
//                parentListener.setNextFragment( new OrderViewFragment( parentListener, model,  null ));
//                parentListener.setTitle(  "Order View" );
                onOrderViewClick( position );
            });
            // On Item Click...
            itemView.setOnClickListener( v -> {
//                    onDrawLineClick( model );

                onOrderViewClick( position );
            } );

        }
        // Drawing Line Between... Two points...!
        private void onDrawLineClick( CurrentOrderListModel model ){
              /*
                *  3. PACKED - ( Waiting for Delivery ) READY_TO_DELIVERY
                *  4. PROCESS  - When Any Delivery Boy Accept to Delivering...
                *  5. PICKED - ( On Delivery ) OUT_FOR_DELIVERY...
            Draw Line :
                if User tap on any order to see from where to where going to shipping...
                Calculate Distance = 9 KM ( Ex.)
                Show the path between Shop and Shipping address...
                and if any Order has been Picked then show the path between user Location and Shipping Location...!
             */
            if (model.getDelivery_status().toUpperCase().equals( "PROCESS" )){
                onDrawingPathLine( mapActionFragment, model.getShop_geo_point(), model.getShipping_geo_point() );

            }else if (model.getDelivery_status().toUpperCase().equals( "PICKED" )){
                onDrawingPathLine( mapActionFragment, StaticValues.MY_GEO_POINTS, model.getShipping_geo_point() );

            }else{
                onDrawingPathLine( mapActionFragment, new GeoPoint( 23.29900000,76.00023100 ), model.getShipping_geo_point() );
            }
        }

        private void onOrderViewClick(int position){
            Intent intent = new Intent(itemView.getContext(), OrderViewActivity.class );
            intent.putExtra( "MODEL_INDEX", position );
            intent.putExtra( "MODEL_TYPE", FRAGMENT_ORDER_VIEW );
            itemView.getContext().startActivity( intent );
        }

        @Override
        public void onDrawingPathLine(MapAction mapAction, GeoPoint fromPoints, GeoPoint toPoints) {
            mapAction.drawPathLine( fromPoints, toPoints );
        }

        @Override
        public void onSetMarker(MapAction mapAction, GeoPoint markPoints, String title) {
            if (markPoints != null)
                mapAction.setMapMarker( markPoints.getLatitude(), markPoints.getLongitude(), title );
        }
    }

    // Start RealTime Data Update Checking...
    public void OrderUpdateListener(){
        orderUpdateLR = getMyCollectionRef( "MY_CURRENT_SHIPPING" )
                .addSnapshotListener( new EventListener <QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                String deliveryID = documentSnapshot.getId();
                                if (documentSnapshot.get( "delivery_status" ) != null){
                                    String updateStatus = documentSnapshot.get( "delivery_status" ).toString();
                                    orderUpdateListener.onCurrentOrderUpdates( deliveryID, updateStatus);
                                }
                            }
                        }
                    }
                } );
    }


}
