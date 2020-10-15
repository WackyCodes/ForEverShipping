package ean.ecom.shipping.notification;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by Shailendra (WackyCodes) on 15/10/2020 17:12
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class NotificationModel {


    private String notifyID;
    private String notifyClickID;
    private String notifyTitle;
    private String notifyMessage;
    private int notifyType;

    private NotificationOrderModel notificationOrderModel;

    public NotificationModel(String notifyID, String notifyClickID, String notifyTitle, String notifyMessage, int notifyType, NotificationOrderModel notificationOrderModel) {
        this.notifyID = notifyID;
        this.notifyClickID = notifyClickID;
        this.notifyTitle = notifyTitle;
        this.notifyMessage = notifyMessage;
        this.notifyType = notifyType;
        this.notificationOrderModel = notificationOrderModel;
    }

    public String getNotifyID() {
        return notifyID;
    }

    public void setNotifyID(String notifyID) {
        this.notifyID = notifyID;
    }

    public String getNotifyClickID() {
        return notifyClickID;
    }

    public void setNotifyClickID(String notifyClickID) {
        this.notifyClickID = notifyClickID;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

    public NotificationOrderModel getNotificationOrderModel() {
        return notificationOrderModel;
    }

    public void setNotificationOrderModel(NotificationOrderModel notificationOrderModel) {
        this.notificationOrderModel = notificationOrderModel;
    }

    //---------------------------------------------------------------------------------------------
    public class NotificationOrderModel{

        private String orderID;
        private String orderShopID;
        private String orderShopName;
        private String orderShopAddress;
        private String orderShippingAddress;
        private GeoPoint orderShopGeoPoint;
        private GeoPoint orderShippingGeoPoint;

        public NotificationOrderModel() {
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getOrderShopID() {
            return orderShopID;
        }

        public void setOrderShopID(String orderShopID) {
            this.orderShopID = orderShopID;
        }

        public String getOrderShopName() {
            return orderShopName;
        }

        public void setOrderShopName(String orderShopName) {
            this.orderShopName = orderShopName;
        }

        public String getOrderShopAddress() {
            return orderShopAddress;
        }

        public void setOrderShopAddress(String orderShopAddress) {
            this.orderShopAddress = orderShopAddress;
        }

        public String getOrderShippingAddress() {
            return orderShippingAddress;
        }

        public void setOrderShippingAddress(String orderShippingAddress) {
            this.orderShippingAddress = orderShippingAddress;
        }

        public GeoPoint getOrderShopGeoPoint() {
            return orderShopGeoPoint;
        }

        public void setOrderShopGeoPoint(GeoPoint orderShopGeoPoint) {
            this.orderShopGeoPoint = orderShopGeoPoint;
        }

        public GeoPoint getOrderShippingGeoPoint() {
            return orderShippingGeoPoint;
        }

        public void setOrderShippingGeoPoint(GeoPoint orderShippingGeoPoint) {
            this.orderShippingGeoPoint = orderShippingGeoPoint;
        }
    }



}
