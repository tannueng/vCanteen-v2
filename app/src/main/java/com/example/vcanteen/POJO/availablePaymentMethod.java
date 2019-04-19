package com.example.vcanteen.POJO;

public class availablePaymentMethod {
    public int getCustomerMoneyAccountId() {
        return customerMoneyAccountId;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public int customerMoneyAccountId;
    public String serviceProvider;

    availablePaymentMethod(int customerMoneyAccount, String serviceProvider ){
        this.customerMoneyAccountId = customerMoneyAccount;
        this.serviceProvider = serviceProvider;
    }
}
