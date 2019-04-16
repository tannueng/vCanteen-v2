package com.example.vcanteen.Data;

public class RecoverPass {
    private String email;

    public RecoverPass(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RecoverPass{" +
                "email='" + email + '\'' +
                '}';
    }
}
