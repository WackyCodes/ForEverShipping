package ean.ecom.shipping.main.order;

import java.util.Map;

/**
 * Created by Shailendra (WackyCodes) on 16/10/2020 00:56
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface GetOrderDetailsListener {

    void onReceiveDetails( OrderListModel orderListModel );

    void onReceiveFailed();

    void showDialog();
    void dismissDialog();

    void onAcceptedOrder(String otp);

    void onUpdateStatusFailed();

    void onUpdateDeliveryStatus( String updateResultID, Map<String, Object> result );

    void showToast( String msg );

}
