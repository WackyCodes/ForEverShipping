package ean.ecom.shipping.main;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by Shailendra (WackyCodes) on 26/09/2020 17:12
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class OrderDetailsModel {

    private String orderID;
    private String deliveryID;
    private String shopAddress;
    private String shippingAddress;

    private GeoPoint shopGeoPoint;
    private GeoPoint shippingGeoPoint;
    private GeoPoint myGeoPoint;

    public OrderDetailsModel() {
    }

    public OrderDetailsModel(String orderID, String deliveryID, String shopAddress, String shippingAddress) {
        this.orderID = orderID;
        this.deliveryID = deliveryID;
        this.shopAddress = shopAddress;
        this.shippingAddress = shippingAddress;
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
