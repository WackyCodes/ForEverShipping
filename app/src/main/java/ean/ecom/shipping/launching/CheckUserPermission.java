package ean.ecom.shipping.launching;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 04:06
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public interface CheckUserPermission {

    //To Show Only Alert... Single Btn
    void showAlertDialog(String msg);

    // To Take User Action .. Double Btn
    void showActionDialog(String msg);

    // Go to Next Step...
    void continueToNext(int stepNo);


    // Interface to Check App Use
    interface CheckAppUsePermission{
        interface OnCheckFinisher{
            void onCheckedFinish(boolean permission);
            void onQueryFailed();
        }
        void checkAppPermission(OnCheckFinisher onCheckFinisher, String verCode);
    }
    interface AppUsePermissionPresenter{

        void onCheckedStart();

        void onCheckedDestroyed();

    }

    // Interface to Check UserPermissionM...
    interface CheckAdminPermission{
        interface OnAdminCheckFinisher{
            void onAdminPermissionCheckFinish(boolean permission);
        }
        void checkAdminPermission(OnAdminCheckFinisher onCheckFinisher, String mobile, String email);
    }
    interface AdminPermissionPresenter{
        void onAdminPermissionCheckStart();

    }


}
