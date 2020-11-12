package ean.ecom.shipping.profile.myorder;

/**
 * Created by Shailendra (WackyCodes) on 25/10/2020 17:40
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface MyOrderListener {

    void showToast(String msg);

    void onLoadProductResponse();


    void showDialog();
    void dismissDialog();


}
