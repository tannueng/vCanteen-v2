package com.example.vcanteen;

public class customerSingleton {

    private static customerSingleton instance;

    String firstname;
    String lastname;
    String email;
    String customerImage;

    public customerSingleton() {

    }

    public static customerSingleton getInstance(){
        if(instance == null) instance = new customerSingleton();
        return  instance;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

}
