package ean.ecom.shipping.main.order;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by Shailendra (WackyCodes) on 26/09/2020 17:12
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class CurrentOrderListModel {

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

    private String orderID;
    private String deliveryID;
    private String shopID;
    private String shopAddress;
    private String shippingAddress;
    private String orderTime;
    private String orderStatus;

    private GeoPoint shopGeoPoint;
    private GeoPoint shippingGeoPoint;
    private GeoPoint myGeoPoint;

    public CurrentOrderListModel() {
    }

    public CurrentOrderListModel(String orderID, String deliveryID, String shopID, String shopAddress, String shippingAddress
            , String orderTime, GeoPoint shopGeoPoint, GeoPoint shippingGeoPoint, GeoPoint myGeoPoint) {
        this.orderID = orderID;
        this.deliveryID = deliveryID;
        this.shopID = shopID;
        this.shopAddress = shopAddress;
        this.shippingAddress = shippingAddress;
        this.orderTime = orderTime;
        this.shopGeoPoint = shopGeoPoint;
        this.shippingGeoPoint = shippingGeoPoint;
        this.myGeoPoint = myGeoPoint;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public GeoPoint getShopGeoPoint() {
        return shopGeoPoint;
    }

    public void setShopGeoPoint(GeoPoint shopGeoPoint) {
        this.shopGeoPoint = shopGeoPoint;
    }

    public GeoPoint getShippingGeoPoint() {
        return shippingGeoPoint;
    }

    public void setShippingGeoPoint(GeoPoint shippingGeoPoint) {
        this.shippingGeoPoint = shippingGeoPoint;
    }

    public GeoPoint getMyGeoPoint() {
        return myGeoPoint;
    }

    public void setMyGeoPoint(GeoPoint myGeoPoint) {
        this.myGeoPoint = myGeoPoint;
    }
}
