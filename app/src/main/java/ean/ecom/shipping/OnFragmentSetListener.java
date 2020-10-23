package ean.ecom.shipping;

import androidx.fragment.app.Fragment;

/**
 * Created by Shailendra (WackyCodes) on 16/10/2020 01:48
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface OnFragmentSetListener {

    void setTitle(String title);
    void showToast(String msg);
    void onBackPressed(int From, String backTitle);
    void setNextFragment(Fragment fragment);

}
