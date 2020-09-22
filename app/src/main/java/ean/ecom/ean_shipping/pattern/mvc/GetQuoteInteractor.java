package ean.ecom.ean_shipping.pattern.mvc;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 12:56
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface GetQuoteInteractor {


    interface OnFinishedListener {
        void onFinished(String string);
    }

    void getNextQuote(OnFinishedListener listener);

}
