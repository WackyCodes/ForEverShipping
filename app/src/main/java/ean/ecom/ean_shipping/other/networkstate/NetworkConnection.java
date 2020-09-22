package ean.ecom.ean_shipping.other.networkstate;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 19:59
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface NetworkConnection {



    // On Check Start...
    interface OnStateCheck{

        interface OnStateChecker{
            void onConnected();
        }


        int onStateChanged(OnStateChecker onStateChecker);

    }

    interface CheckConnection{
        void onCheckStart();
        void onCheckFinished();
    }



}
