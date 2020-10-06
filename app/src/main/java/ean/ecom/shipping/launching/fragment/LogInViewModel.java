package ean.ecom.shipping.launching.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
;
import androidx.databinding.Bindable;

import java.net.ContentHandler;

import ean.ecom.shipping.BR;
import ean.ecom.shipping.launching.CheckUserPermission;
import ean.ecom.shipping.launching.UserPermissionM;


/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 23:48
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class LogInViewModel extends BaseObservable {
    private SignInModel user;
    private Context context;
    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Password not valid";

    @Bindable
    private String toastMessage = null;

    private CheckUserPermission.CheckIsUserRegistered onAuthActionListener;
    private UserPermissionM authActionClass;

    //  Constructor....
    public LogInViewModel(Context context, CheckUserPermission.CheckIsUserRegistered onAuthActionListener ) {
        user = new SignInModel("","");
        authActionClass = new UserPermissionM();
        this.context = context;
        this.onAuthActionListener = onAuthActionListener;
    }

    // ----------- Action Button Click -------------------------------------------------------------

    public void onLoginClicked() {
        if (isValidSignInData()){
//            setToastMessage(successMessage);
            if (authActionClass!=null){
                onAuthActionListener.showDialog();
                authActionClass.onSignInListener( onAuthActionListener, getUserMobile(), getUserEmail(), getUserPassword() );
            }
        }
        else {
            setToastMessage(errorMessage);
        }
    }

    // on Forget Password Clicked...!
    public void onForgetPasswordClick(){

    }

    // On Sign Up Clicked..!
    public void onSignUpClick(){
        if (isValidUserSignUpData()){
//            setToastMessage(successMessage);
            if (authActionClass!=null){
                onAuthActionListener.showDialog();
                authActionClass.onSignUpListener( onAuthActionListener, getUserMobile() , getUserEmail(), getUserPassword() );
            }
        }
        else {
            setToastMessage(errorMessage);
        }
    }


    // on Apply Button Clicked..!
    public void onApplyBtnClicked(){


    }

    // ----------- Action Button Click -------------------------------------------------------------

    // Validation...

    public boolean isValidSignInData() {
        return isValidEmail() && getUserPassword() != null;
    }

    public  boolean isValidUserSignUpData(){
        return isValidEmail() && isMatchedPassword();
    }

    // Checking Validation.....
    private boolean isValidEmail(){
        return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches();
    }

    private boolean isMatchedPassword(){
        return !TextUtils.isEmpty(getUserPassword()) && !TextUtils.isEmpty(getUserPassword1()) && getUserPassword1().equals( getUserPassword() );
    }


    // ----------- Binding Data --------------------------------------------------------------------
    @Bindable
    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged( ean.ecom.shipping.BR.toastMessage);
    }

    @Bindable
    public String getUserEmail() {
        return user.getEmail();
    }

    public void setUserEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged( ean.ecom.shipping.BR.userEmail );
    }

    @Bindable
    public String getUserMobile() {
        return user.getMobile();
    }

    public void setUserMobile(String mobile) {
        user.setMobile(mobile);
        notifyPropertyChanged( BR.userMobile );
    }

    @Bindable
    public String getUserPassword() {
        return user.getPassword();
    }

    public void setUserPassword(String password) {
        user.setPassword(password);
        notifyPropertyChanged( ean.ecom.shipping.BR.userPassword );
    }
    @Bindable
    public String getUserPassword1() {
        return user.getPassword();
    }

    public void setUserPassword1(String password) {
        user.setPassword(password);
        notifyPropertyChanged( ean.ecom.shipping.BR.userPassword1 );
    }

    @Bindable
    public String getUserPassword2() {
        return user.getPassword1();
    }

    public void setUserPassword2(String password) {
        user.setPassword1(password);
        notifyPropertyChanged( ean.ecom.shipping.BR.userPassword1 );
    }

    // ----------- Binding Data --------------------------------------------------------------------


}
