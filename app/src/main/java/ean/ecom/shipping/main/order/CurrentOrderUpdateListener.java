package ean.ecom.shipping.main.order;

/**
 * Created by Shailendra (WackyCodes) on 02/10/2020 15:08
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface CurrentOrderUpdateListener {

    void onCurrentOrderUpdates(String deliveryID, String updateStatus);

}
