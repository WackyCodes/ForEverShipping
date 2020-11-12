package ean.ecom.shipping.main.order;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

/**
 * Created by Shailendra (WackyCodes) on 27/09/2020 11:05
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderListModel implements Parcelable {

    private String order_id; //  orderID
    // Order Time...
    private Timestamp order_timestamp; //  orderTimestamp

    // Order List Sub Item...
    private ArrayList <OrderProductsModel> products_list;  // cartOrderSubItemModelList

    // Billing ...
    private String delivery_charge; // deliveryCharge
    private String billing_amounts; // billing_amounts
    private String product_amounts; //  productAmounts
    private String total_amounts; // total product's amounts...  totalAmounts
    private String saving_amounts; // savingAmounts

    private String pay_mode; //  payMode
    private String payment_id; //  payment_id

    // Shipping Name And Address...
    private String order_accepted_by; // shippingName
    private String order_delivery_address; //  shippingAddress
    private String order_delivery_pin; //  shippingPinCode
    private GeoPoint shipping_geo_point;
    private String order_by_auth_id;
    private String order_by_mobile;
    private String order_by_name;

    private String order_accepted_otp; //  || acceptOrderOTP

    // Delivery Status...
    private String delivery_status; // orderStatus
//    private String delivery_schedule_time;

    // Delivery Info and Address...
    // Load Delivery Boy information.. | Load When User View Details..
    private String delivery_id; //  delivery_id
    private String delivery_by_uid; //  deliveredByAuthID
    private String delivery_by_name; // deliveredByName
    private String delivery_by_mobile; // deliveredByMobile
    private String delivery_vehicle_no;
    private Timestamp delivery_timestamp;

    private boolean is_rated_to_delivery_boy = false;
    private String delivery_boy_rate_star;
    private String delivery_boy_rate_text;


    public OrderListModel() {
    }


    protected OrderListModel(Parcel in) {
        order_id = in.readString();
        order_timestamp = in.readParcelable( Timestamp.class.getClassLoader() );
        products_list = in.createTypedArrayList( OrderProductsModel.CREATOR );
        delivery_charge = in.readString();
        billing_amounts = in.readString();
        product_amounts = in.readString();
        total_amounts = in.readString();
        saving_amounts = in.readString();
        pay_mode = in.readString();
        payment_id = in.readString();
        order_accepted_by = in.readString();
        order_delivery_address = in.readString();
        order_delivery_pin = in.readString();
        order_by_auth_id = in.readString();
        order_by_mobile = in.readString();
        order_by_name = in.readString();
        order_accepted_otp = in.readString();
        delivery_status = in.readString();
        delivery_id = in.readString();
        delivery_by_uid = in.readString();
        delivery_by_name = in.readString();
        delivery_by_mobile = in.readString();
        delivery_vehicle_no = in.readString();
        delivery_timestamp = in.readParcelable( Timestamp.class.getClassLoader() );
        is_rated_to_delivery_boy = in.readByte() != 0;
        delivery_boy_rate_star = in.readString();
        delivery_boy_rate_text = in.readString();
    }

    public static final Creator <OrderListModel> CREATOR = new Creator <OrderListModel>() {
        @Override
        public OrderListModel createFromParcel(Parcel in) {
            return new OrderListModel( in );
        }

        @Override
        public OrderListModel[] newArray(int size) {
            return new OrderListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( order_id );
        dest.writeParcelable( order_timestamp, flags );
        dest.writeTypedList( products_list );
        dest.writeString( delivery_charge );
        dest.writeString( billing_amounts );
        dest.writeString( product_amounts );
        dest.writeString( total_amounts );
        dest.writeString( saving_amounts );
        dest.writeString( pay_mode );
        dest.writeString( payment_id );
        dest.writeString( order_accepted_by );
        dest.writeString( order_delivery_address );
        dest.writeString( order_delivery_pin );
        dest.writeString( order_by_auth_id );
        dest.writeString( order_by_mobile );
        dest.writeString( order_by_name );
        dest.writeString( order_accepted_otp );
        dest.writeString( delivery_status );
        dest.writeString( delivery_id );
        dest.writeString( delivery_by_uid );
        dest.writeString( delivery_by_name );
        dest.writeString( delivery_by_mobile );
        dest.writeString( delivery_vehicle_no );
        dest.writeParcelable( delivery_timestamp, flags );
        dest.writeByte( (byte) (is_rated_to_delivery_boy ? 1 : 0) );
        dest.writeString( delivery_boy_rate_star );
        dest.writeString( delivery_boy_rate_text );
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Timestamp getOrder_timestamp() {
        return order_timestamp;
    }

    public void setOrder_timestamp(Timestamp order_timestamp) {
        this.order_timestamp = order_timestamp;
    }

    public ArrayList <OrderProductsModel> getProducts_list() {
        return products_list;
    }

    public void setProducts_list(ArrayList <OrderProductsModel> products_list) {
        this.products_list = products_list;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getBilling_amounts() {
        return billing_amounts;
    }

    public void setBilling_amounts(String billing_amounts) {
        this.billing_amounts = billing_amounts;
    }

    public String getProduct_amounts() {
        return product_amounts;
    }

    public void setProduct_amounts(String product_amounts) {
        this.product_amounts = product_amounts;
    }

    public String getTotal_amounts() {
        return total_amounts;
    }

    public void setTotal_amounts(String total_amounts) {
        this.total_amounts = total_amounts;
    }

    public String getSaving_amounts() {
        return saving_amounts;
    }

    public void setSaving_amounts(String saving_amounts) {
        this.saving_amounts = saving_amounts;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getOrder_accepted_by() {
        return order_accepted_by;
    }

    public void setOrder_accepted_by(String order_accepted_by) {
        this.order_accepted_by = order_accepted_by;
    }

    public String getOrder_delivery_address() {
        return order_delivery_address;
    }

    public void setOrder_delivery_address(String order_delivery_address) {
        this.order_delivery_address = order_delivery_address;
    }

    public String getOrder_delivery_pin() {
        return order_delivery_pin;
    }

    public void setOrder_delivery_pin(String order_delivery_pin) {
        this.order_delivery_pin = order_delivery_pin;
    }

    public GeoPoint getShipping_geo_point() {
        return shipping_geo_point;
    }

    public void setShipping_geo_point(GeoPoint shipping_geo_point) {
        this.shipping_geo_point = shipping_geo_point;
    }

    public String getOrder_by_auth_id() {
        return order_by_auth_id;
    }

    public void setOrder_by_auth_id(String order_by_auth_id) {
        this.order_by_auth_id = order_by_auth_id;
    }

    public String getOrder_by_mobile() {
        return order_by_mobile;
    }

    public void setOrder_by_mobile(String order_by_mobile) {
        this.order_by_mobile = order_by_mobile;
    }

    public String getOrder_by_name() {
        return order_by_name;
    }

    public void setOrder_by_name(String order_by_name) {
        this.order_by_name = order_by_name;
    }

    public String getOrder_accepted_otp() {
        return order_accepted_otp;
    }

    public void setOrder_accepted_otp(String order_accepted_otp) {
        this.order_accepted_otp = order_accepted_otp;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getDelivery_by_uid() {
        return delivery_by_uid;
    }

    public void setDelivery_by_uid(String delivery_by_uid) {
        this.delivery_by_uid = delivery_by_uid;
    }

    public String getDelivery_by_name() {
        return delivery_by_name;
    }

    public void setDelivery_by_name(String delivery_by_name) {
        this.delivery_by_name = delivery_by_name;
    }

    public String getDelivery_by_mobile() {
        return delivery_by_mobile;
    }

    public void setDelivery_by_mobile(String delivery_by_mobile) {
        this.delivery_by_mobile = delivery_by_mobile;
    }

    public String getDelivery_vehicle_no() {
        return delivery_vehicle_no;
    }

    public void setDelivery_vehicle_no(String delivery_vehicle_no) {
        this.delivery_vehicle_no = delivery_vehicle_no;
    }

    public Timestamp getDelivery_timestamp() {
        return delivery_timestamp;
    }

    public void setDelivery_timestamp(Timestamp delivery_timestamp) {
        this.delivery_timestamp = delivery_timestamp;
    }

    public boolean isIs_rated_to_delivery_boy() {
        return is_rated_to_delivery_boy;
    }

    public void setIs_rated_to_delivery_boy(boolean is_rated_to_delivery_boy) {
        this.is_rated_to_delivery_boy = is_rated_to_delivery_boy;
    }

    public String getDelivery_boy_rate_star() {
        return delivery_boy_rate_star;
    }

    public void setDelivery_boy_rate_star(String delivery_boy_rate_star) {
        this.delivery_boy_rate_star = delivery_boy_rate_star;
    }

    public String getDelivery_boy_rate_text() {
        return delivery_boy_rate_text;
    }

    public void setDelivery_boy_rate_text(String delivery_boy_rate_text) {
        this.delivery_boy_rate_text = delivery_boy_rate_text;
    }

    public static Creator <OrderListModel> getCREATOR() {
        return CREATOR;
    }
}
