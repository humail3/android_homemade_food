package uo.nexton.homemadefood.models;

public class Users {
    String userName, emailAddress, userPassword, uid, phoneNumber, userAddress, userCity;

    public Users() {
    }

    public Users(String userName, String emailAddress, String userPassword, String uid) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.userPassword = userPassword;
        this.uid = uid;
    }

    public Users(String userName, String emailAddress, String uid) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.uid = uid;
    }

    public Users(String userName, String emailAddress, String phoneNumber, String userAddress, String userCity) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.userAddress = userAddress;
        this.userCity = userCity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }
}
