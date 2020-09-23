package ean.ecom.shipping.pattern;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 13:27
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public interface MainContract {

    interface MainView {
        void showProgress();

        void hideProgress();

        void setQuote(String string);
    }

    interface GetQuoteInteractor {
        interface OnFinishedListener {
            void onFinished(String string);
        }

        void getNextQuote(OnFinishedListener onFinishedListener);
    }

    interface Presenter {
        void onButtonClick();

        void onDestroy();
    }
}

