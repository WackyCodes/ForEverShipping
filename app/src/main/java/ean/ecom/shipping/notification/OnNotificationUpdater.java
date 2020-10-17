package ean.ecom.shipping.notification;

import androidx.fragment.app.Fragment;

/**
 * Created by Shailendra (WackyCodes) on 16/10/2020 10:24
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface OnNotificationUpdater {

    void onNotificationClick();
    void onNotificationClick(Fragment fragment);

}
