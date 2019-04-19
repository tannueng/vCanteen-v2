package com.example.vcanteen.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class availablePaymentMethod implements Parcelable {
    protected availablePaymentMethod(Parcel in) {
        customerMoneyAccountId = in.readInt();
        serviceProvider = in.readString();
    }

    public static final Creator<availablePaymentMethod> CREATOR = new Creator<availablePaymentMethod>() {
        @Override
        public availablePaymentMethod createFromParcel(Parcel in) {
            return new availablePaymentMethod(in);
        }

        @Override
        public availablePaymentMethod[] newArray(int size) {
            return new availablePaymentMethod[size];
        }
    };

    public int getCustomerMoneyAccountId() {
        return customerMoneyAccountId;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public int customerMoneyAccountId;
    public String serviceProvider;

    public availablePaymentMethod(int customerMoneyAccountId, String serviceProvider){
        this.customerMoneyAccountId = customerMoneyAccountId;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(customerMoneyAccountId);
        dest.writeString(serviceProvider);
    }
}
