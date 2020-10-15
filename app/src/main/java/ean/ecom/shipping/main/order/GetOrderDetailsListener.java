package ean.ecom.shipping.main.order;

/**
 * Created by Shailendra (WackyCodes) on 16/10/2020 00:56
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface GetOrderDetailsListener {

    void onReceiveDetails();

    void onReceiveFailed();

    void showDialog();
    void dismissDialog();

}
