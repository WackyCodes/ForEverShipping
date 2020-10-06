package ean.ecom.shipping.profile;

/**
 * Created by Shailendra (WackyCodes) on 02/10/2020 11:44
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
class UserProfileInfo {

    private String userAuthID;
    private String userName;
    private String userEmail;
    private String userMobile;
    private String userImage;
    private String userAddress;

    private String userVehicleNumber;
    private String userDrivingLicence;

    private UserAccount userAccount;

    public UserProfileInfo(String userAuthID, String userName, String userEmail, String userMobile, String userImage, String userAddress, String userVehicleNumber, String userDrivingLicence, UserAccount userAccount) {
        this.userAuthID = userAuthID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.userImage = userImage;
        this.userAddress = userAddress;
        this.userVehicleNumber = userVehicleNumber;
        this.userDrivingLicence = userDrivingLicence;
        this.userAccount = userAccount;
    }

    public String getUserAuthID() {
        return userAuthID;
    }

    public void setUserAuthID(String userAuthID) {
        this.userAuthID = userAuthID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserVehicleNumber() {
        return userVehicleNumber;
    }

    public void setUserVehicleNumber(String userVehicleNumber) {
        this.userVehicleNumber = userVehicleNumber;
    }

    public String getUserDrivingLicence() {
        return userDrivingLicence;
    }

    public void setUserDrivingLicence(String userDrivingLicence) {
        this.userDrivingLicence = userDrivingLicence;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    // User Account...
    public static class UserAccount {
        private String accountNumber;
        private String accountType;
        private String accountIFSC;
        private String accountUPI;
        private String accountOwner;

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountIFSC() {
            return accountIFSC;
        }

        public void setAccountIFSC(String accountIFSC) {
            this.accountIFSC = accountIFSC;
        }

        public String getAccountUPI() {
            return accountUPI;
        }

        public void setAccountUPI(String accountUPI) {
            this.accountUPI = accountUPI;
        }

        public String getAccountOwner() {
            return accountOwner;
        }

        public void setAccountOwner(String accountOwner) {
            this.accountOwner = accountOwner;
        }
    }



}
