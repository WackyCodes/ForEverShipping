package ean.ecom.shipping.main.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shailendra (WackyCodes) on 26/09/2020 17:12
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class CurrentOrderListModel implements Parcelable {

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

    private String order_id;
    private String delivery_id;
    private String shop_id;
    private String shop_name;
    private String shop_address;
    private String shipping_address;
    private String order_time;
    private String delivery_status;

    private GeoPoint shop_geo_point;
    private GeoPoint shipping_geo_point;
    private GeoPoint my_geo_point;

    public CurrentOrderListModel() {
    }

    public CurrentOrderListModel(String order_id, String delivery_id, String shop_id, String shop_address, String shipping_address
            , String order_time, GeoPoint shop_geo_point, GeoPoint shipping_geo_point, GeoPoint my_geo_point) {
        this.order_id = order_id;
        this.delivery_id = delivery_id;
        this.shop_id = shop_id;
        this.shop_address = shop_address;
        this.shipping_address = shipping_address;
        this.order_time = order_time;
        this.shop_geo_point = shop_geo_point;
        this.shipping_geo_point = shipping_geo_point;
        this.my_geo_point = my_geo_point;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public GeoPoint getShop_geo_point() {
        return shop_geo_point;
    }

    public void setShop_geo_point(GeoPoint shop_geo_point) {
        this.shop_geo_point = shop_geo_point;
    }

    public GeoPoint getShipping_geo_point() {
        return shipping_geo_point;
    }

    public void setShipping_geo_point(GeoPoint shipping_geo_point) {
        this.shipping_geo_point = shipping_geo_point;
    }

    public GeoPoint getMy_geo_point() {
        return my_geo_point;
    }

    public void setMy_geo_point(GeoPoint my_geo_point) {
        this.my_geo_point = my_geo_point;
    }


    public Map<String, Object> toMap(){
        Map<String, Object> map  = new HashMap <>();
        map.put( "order_id", order_id );
        map.put( "delivery_id", delivery_id );
        map.put( "shop_id", shop_id );
        map.put( "shop_name", shop_name );
        map.put( "shop_address", shop_address );
        map.put( "shipping_address", shipping_address );
        map.put( "order_time", order_time );
        map.put( "delivery_status", delivery_status );
        map.put( "shop_geo_point", shop_geo_point );
        map.put( "shipping_geo_point", shipping_geo_point );
        map.put( "my_geo_point", my_geo_point );
        return map;
    }

    protected CurrentOrderListModel(Parcel in) {
        order_id = in.readString();
        delivery_id = in.readString();
        shop_id = in.readString();
        shop_name = in.readString();
        shop_address = in.readString();
        shipping_address = in.readString();
        order_time = in.readString();
        delivery_status = in.readString();
    }

    public static final Creator <CurrentOrderListModel> CREATOR = new Creator <CurrentOrderListModel>() {
        @Override
        public CurrentOrderListModel createFromParcel(Parcel in) {
            return new CurrentOrderListModel( in );
        }

        @Override
        public CurrentOrderListModel[] newArray(int size) {
            return new CurrentOrderListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( order_id );
        dest.writeString( delivery_id );
        dest.writeString( shop_id );
        dest.writeString( shop_name );
        dest.writeString( shop_address );
        dest.writeString( shipping_address );
        dest.writeString( order_time );
        dest.writeString( delivery_status );
    }



}
