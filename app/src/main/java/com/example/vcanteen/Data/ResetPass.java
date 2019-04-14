package com.example.vcanteen.Data;

public class ResetPass {

    private String email;
    private String passwordNew;

    public ResetPass(String email, String passwordNew) {
        this.email = email;
        this.passwordNew = passwordNew;
    }

    @Override
    public String toString() {
        return "ResetPass{" +
                "email='" + email + '\'' +
                ", passwordNew='" + passwordNew + '\'' +
                '}';
    }
}
