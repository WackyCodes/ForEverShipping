package ean.ecom.shipping.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import ean.ecom.shipping.R;
import ean.ecom.shipping.main.order.CurrentOrderListModel;
import ean.ecom.shipping.main.order.CurrentOrderUpdateListener;
import ean.ecom.shipping.other.StaticValues;

import static ean.ecom.shipping.database.DBQuery.getMyCollectionRef;

/**
 * Created by Shailendra (WackyCodes) on 26/09/2020 17:31
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ShippingOrderAdaptor extends RecyclerView.Adapter<ShippingOrderAdaptor.ViewHolder> {

    private List<CurrentOrderListModel> currentOrderListModelList;
    private ListenerRegistration orderUpdateLR;
    private CurrentOrderUpdateListener orderUpdateListener;

    private MapAction mapActionFragment;

    public ShippingOrderAdaptor(List <CurrentOrderListModel> currentOrderListModelList, MapAction mapActionFragment, CurrentOrderUpdateListener orderUpdateListener) {
        this.currentOrderListModelList = currentOrderListModelList;
        this.mapActionFragment = mapActionFragment;
        this.orderUpdateListener = orderUpdateListener;
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
        private ImageButton viewOrderDetailsBtn;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            orderText = itemView.findViewById( R.id.order_id_text );
            shopAddressText = itemView.findViewById( R.id.shop_address_text );
            shippingAddressText = itemView.findViewById( R.id.shipping_address_text );

            getShopDirection = itemView.findViewById( R.id.get_shop_direction );
            viewOrderDetailsBtn = itemView.findViewById( R.id.view_details_image_btn );

        }

        private void setData( final CurrentOrderListModel model, int position){

            orderText.setText( "Order ID: " + model.getOrderID() );
            shopAddressText.setText( model.getShopAddress() );
            shippingAddressText.setText( model.getShippingAddress() );


            // Set Visibility of Shop Direction Button...
            if (model.getOrderStatus() != null && model.getOrderStatus().toUpperCase().equals( "PROCESS" )){
                getShopDirection.setVisibility( View.VISIBLE );
                // If Order Not picked Yet..! ( orderStatus == ACCEPTED )
                onSetMarker( mapActionFragment, model.getShopGeoPoint(), model.getShopAddress() );
            }
            else{
                getShopDirection.setVisibility( View.INVISIBLE );
                // If Order picked and out for delivery..! ( orderStatus == PICKED )
                if (model.getShippingGeoPoint() != null){
                    onSetMarker( mapActionFragment, model.getShippingGeoPoint(), model.getShippingAddress() );
                }
            }

            // Get Shop Direction... From My Location...!
            getShopDirection.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDrawingPathLine( mapActionFragment, StaticValues.MY_GEO_POINTS, model.getShopGeoPoint() );
                }
            } );

            // On Item Click...
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDrawLineClick( model );
                }
            } );

            viewOrderDetailsBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO :
                }
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
            if (model.getOrderStatus().toUpperCase().equals( "PROCESS" )){
                onDrawingPathLine( mapActionFragment, model.getShopGeoPoint(), model.getShippingGeoPoint() );

            }else if (model.getOrderStatus().toUpperCase().equals( "PICKED" )){
                onDrawingPathLine( mapActionFragment, StaticValues.MY_GEO_POINTS, model.getShippingGeoPoint() );

            }else{

            }
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
