package com.example.vcanteen.Data;

import android.support.annotation.Nullable;

import org.json.JSONObject;

public class Customers {

    private String email;
    @Nullable
    private String first_name;
    @Nullable
    private String last_name;
    private String account_type;
    @Nullable
    private String profile_url;
    @Nullable
    private String password;
    private String firebaseToken;

    public Customers(String email, @Nullable String first_name, @Nullable String last_name, String account_type, @Nullable String profile_url, @Nullable String password, String firebaseToken) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.account_type = account_type;
        this.profile_url = profile_url;
        this.password = password;
        this.firebaseToken = firebaseToken;
    }

    public String getAccount_type() {
        return account_type;
    }

    public String getEmail() {
        return email;
    }

    @Nullable
    public String getFirst_name() {
        return first_name;
    }

    @Nullable
    public String getLast_name() {
        return last_name;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    @Nullable
    public String getProfile_pic() {
        return profile_url;
    }

    @Nullable
    public String getProfile_url() {
        return profile_url;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", account_type='" + account_type + '\'' +
                ", profile_url='" + profile_url + '\'' +
                ", password='" + password + '\'' +
                ", firebaseToken='" + firebaseToken + '\'' +
                '}';
    }
}
