package com.example.vcanteen;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class processingPaymentActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    orderStack orderStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing_payment);

        String restaurantNameString = getIntent().getStringExtra("sendRestaurantName"); //just add for minor fix in order confirmation

        String name = getIntent().getStringExtra("selectedServiceProvider");

        TextView selectedServiceProvider = (TextView)findViewById(R.id.selectedServiceProvider);
        selectedServiceProvider.setText("Redirecting to "+name+" .....");

        orderStack = getIntent().getExtras().getParcelable("orderStack");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(processingPaymentActivity.this,paymentSplash.class);
                intent.putExtra("selectedServiceProvider", name);
                intent.putExtra("orderStack", orderStack);
                intent.putExtra("sendRestaurantName", restaurantNameString);//just add for minor fix in order confirmation
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
