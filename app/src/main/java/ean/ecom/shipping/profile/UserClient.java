package ean.ecom.shipping.profile;

import android.app.Application;

/**
 * Created by Shailendra (WackyCodes) on 29/09/2020 19:49
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
