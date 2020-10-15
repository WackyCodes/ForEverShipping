package ean.ecom.shipping.profile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shailendra (WackyCodes) on 29/09/2020 19:44
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class User implements Parcelable {

    private String user_email;
    private String user_id;
    private String user_name;
    private String user_image;
    private String user_mobile;

    private String user_driving_licence;
    private String user_vehicle_number;
    private String user_address;
    private String user_city_code;
    private String user_city;


    public User(String user_email, String user_id, String user_name, String user_image, String user_mobile) {
        this.user_email = user_email;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_image = user_image;
        this.user_mobile = user_mobile;
    }

    public User() {

    }

    protected User(Parcel in) {
        user_email = in.readString();
        user_id = in.readString();
        user_name = in.readString();
        user_image = in.readString();
    }

    public static final Creator <User> CREATOR = new Creator <User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User( in );
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( user_email );
        dest.writeString( user_id );
        dest.writeString( user_name );
        dest.writeString( user_image );
    }


    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_driving_licence() {
        return user_driving_licence;
    }

    public void setUser_driving_licence(String user_driving_licence) {
        this.user_driving_licence = user_driving_licence;
    }

    public String getUser_vehicle_number() {
        return user_vehicle_number;
    }

    public void setUser_vehicle_number(String user_vehicle_number) {
        this.user_vehicle_number = user_vehicle_number;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_city_code() {
        return user_city_code;
    }

    public void setUser_city_code(String user_city_code) {
        this.user_city_code = user_city_code;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    @Override
    public String toString() {
        return "User{" + "user_email='" + user_email + '\'' + ", user_id='" + user_id + '\'' + ", user_name='" + user_name + '\'' + ", user_image='" + user_image + '\'' + ", user_mobile='" + user_mobile + '\'' + '}';
    }

}
