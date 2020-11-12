package ean.ecom.shipping.main.order;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shailendra (WackyCodes) on 02/10/2020 15:08
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface CurrentOrderUpdateListener {

    void onCurrentOrderUpdates(String deliveryID, String updateStatus);

    void onLoadingOrderResponse( boolean isComplete );

}
