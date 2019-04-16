package com.example.vcanteen;

public class paymentList {

    int customerMoneyAccountId;
    String serviceProvider;

    public paymentList(int customerMoneyAccountId, String serviceProvider) {
        this.customerMoneyAccountId = customerMoneyAccountId;
        this.serviceProvider = serviceProvider;
    }

    public int getCustomerMoneyAccountId() {
        return customerMoneyAccountId;
    }

    public void setCustomerMoneyAccountId(int customerMoneyAccountId) {
        this.customerMoneyAccountId = customerMoneyAccountId;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
