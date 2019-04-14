package com.example.vcanteen.Data;

public class Token {

    private String email;
    private String token;

    public Token(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
