package ean.ecom.shipping.launching.fragment;

/**
 * Created by Shailendra (WackyCodes) on 21/08/2020 23:47
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */

public class SignInModel {
    private String email;
    private String password;
    private String password1;


    public SignInModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public SignInModel(String email, String password, String password1) {
        this.email = email;
        this.password = password;
        this.password1 = password1;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }
}
