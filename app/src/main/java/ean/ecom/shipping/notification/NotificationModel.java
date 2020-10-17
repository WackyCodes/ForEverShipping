package ean.ecom.shipping.notification;

import com.google.firebase.firestore.GeoPoint;

import ean.ecom.shipping.main.order.CurrentOrderListModel;

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

    private CurrentOrderListModel notificationOrderModel;

    public NotificationModel(String notifyID, String notifyClickID, String notifyTitle, String notifyMessage, int notifyType, CurrentOrderListModel notificationOrderModel) {
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

    public CurrentOrderListModel getNotificationOrderModel() {
        return notificationOrderModel;
    }

    public void setNotificationOrderModel(CurrentOrderListModel notificationOrderModel) {
        this.notificationOrderModel = notificationOrderModel;
    }

    //---------------------------------------------------------------------------------------------



}
