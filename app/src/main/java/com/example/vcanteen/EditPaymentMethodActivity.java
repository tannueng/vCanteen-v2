package com.example.vcanteen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.vcanteen.POJO.availablePaymentMethod;

import java.util.ArrayList;

public class EditPaymentMethodActivity extends AppCompatActivity {
    ArrayList<availablePaymentMethod> payments = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_method);
        //TODO getExtra from setting page

        payments = getIntent().getParcelableArrayListExtra( "availablePaymentMethodList"); //IMPORTANT
        System.out.println("SP: "+payments.get(0).serviceProvider);
        System.out.println("SP: "+payments.get(1).serviceProvider);

        for (availablePaymentMethod list :payments){
            System.out.println("received payment");
            System.out.println(list.getCustomerMoneyAccountId()+","+list.getServiceProvider());

        }
        //successfully intent the object

    }
}
