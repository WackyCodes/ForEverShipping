package ean.ecom.shipping.launching;

import static ean.ecom.shipping.launching.WelcomeActivity.CHECK_STORAGE_PERMISSION;
import static ean.ecom.shipping.launching.WelcomeActivity.GO_TO_MAIN_ACTIVITY;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 18:19
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class CheckPermissionP implements CheckUserPermission.AppUsePermissionPresenter, CheckUserPermission.CheckAppUsePermission.OnCheckFinisher
        , CheckUserPermission.AdminPermissionPresenter, CheckUserPermission.CheckAdminPermission.OnAdminCheckFinisher
{

    private CheckUserPermission mainView;

    // App Use Permission Start....
    private CheckUserPermission.CheckAppUsePermission checkAppUsePermission;
    private String verCode;

    public CheckPermissionP(CheckUserPermission mainView, CheckUserPermission.CheckAppUsePermission checkAppUsePermission, String verCode) {
        this.mainView = mainView;
        this.checkAppUsePermission = checkAppUsePermission;
        this.verCode = verCode;
    }

    @Override
    public void onCheckedStart() {
        checkAppUsePermission.checkAppPermission( this, verCode );
    }

    @Override
    public void onCheckedFinish(boolean permission) {
        if (permission){
            // Go to next step...
            mainView.continueToNext( CHECK_STORAGE_PERMISSION );
        }else{
            // show Alert Dialog...
            mainView.showAlertDialog( "Permission denied!" );
        }
    }

    @Override
    public void onQueryFailed() {
        mainView.showAlertDialog( "Sorry, Request Failed! Please Check Your Internet Connection!" );
    }

    @Override
    public void onCheckedDestroyed() {

    }

    // Admin Permission..
    private CheckUserPermission.CheckAdminPermission checkAdminPermission;
    private String mobile;
    private String email;

    public CheckPermissionP(CheckUserPermission mainView, CheckUserPermission.CheckAdminPermission checkAdminPermission, String mobile, String email) {
        this.mainView = mainView;
        this.checkAdminPermission = checkAdminPermission;
        this.mobile = mobile;
        this.email = email;
    }

    @Override
    public void onAdminPermissionCheckStart() {
    }

    @Override
    public void onAdminPermissionCheckFinish(boolean permission) {
        if (permission){
            // Go to next step...
            mainView.continueToNext( GO_TO_MAIN_ACTIVITY );
        }else{
            // show Alert Dialog...
            mainView.showAlertDialog( "Admin Permission denied!" );
        }
    }



    ///

}


