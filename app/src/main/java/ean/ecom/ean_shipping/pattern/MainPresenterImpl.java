package ean.ecom.ean_shipping.pattern;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 13:01
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class MainPresenterImpl implements MainContract.Presenter, MainContract.GetQuoteInteractor.OnFinishedListener {

    private MainContract.MainView mainView;
    private MainContract.GetQuoteInteractor getQuoteInteractor;

    public MainPresenterImpl(MainContract.MainView mainView, MainContract.GetQuoteInteractor getQuoteInteractor) {
        this.mainView = mainView;
        this.getQuoteInteractor = getQuoteInteractor;
    }

    @Override
    public void onButtonClick() {
        if (mainView != null) {
            mainView.showProgress();
        }

        getQuoteInteractor.getNextQuote(this);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onFinished(String string) {
        if (mainView != null) {
            mainView.setQuote(string);
            mainView.hideProgress();
        }
    }


}
