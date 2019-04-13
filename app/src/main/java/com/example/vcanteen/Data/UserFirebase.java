package com.example.vcanteen.Data;

import java.io.Serializable;

public class UserFirebase implements Serializable {

    private String email;
    private String token;

    public UserFirebase(String email, String token) {
        this.email = email;
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserFirebase{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
