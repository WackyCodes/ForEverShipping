package ean.ecom.shipping.launching.fragment;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
;
import androidx.databinding.Bindable;


/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 23:48
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class LogInViewModel extends BaseObservable {
    private SignInModel user;

    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Password not valid";

    @Bindable
    private String toastMessage = null;

    //  Constructor....
    public LogInViewModel() {
        user = new SignInModel("","");
    }

    @Bindable
    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged( ean.ecom.shipping.BR.toastMessage);
    }

    public void setUserEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged( ean.ecom.shipping.BR.userEmail );
    }

    @Bindable
    public String getUserEmail() {
        return user.getEmail();
    }

    @Bindable
    public String getUserPassword() {
        return user.getPassword();
    }

    public void setUserPassword(String password) {
        user.setPassword(password);
        notifyPropertyChanged( ean.ecom.shipping.BR.userPassword);
    }

    public void onLoginClicked() {
        if (isInputDataValid())
            setToastMessage(successMessage);
        else
            setToastMessage(errorMessage);
    }

    public boolean isInputDataValid() {
        return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches() && getUserPassword().length() > 5;
    }

    // on Forget Password Clicked...!
    public void onForgetPasswordClick(){

    }

    // On Sign Up Clicked..!
    public void onSignUpClick(){

    }


    // on Apply Button Clicked..!
    public void onApplyBtnClicked(){

    }


}
