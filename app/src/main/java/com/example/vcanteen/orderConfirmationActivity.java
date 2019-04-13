package com.example.vcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

public class orderConfirmationActivity extends AppCompatActivity {

    orderStack orderStack;
    Button orderMoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        String restaurantNameString = getIntent().getStringExtra("sendRestaurantName"); //just add for minor fix in order confirmation

        orderMoreButton = (Button) findViewById(R.id.orderMoreButton);
        orderMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderMoreBackToHome();
            }
        });

        orderStack = com.example.vcanteen.orderStack.getInstance();//getIntent().getExtras().getParcelable("orderStack");

        ListAdapter testAdapter3 = new confirmedOrderListAdapter(this,orderStack, restaurantNameString);  //just add for minor fix in order confirmation
        final ListView confirmedList = findViewById(R.id.confirmedList);
        confirmedList.setAdapter(testAdapter3);

        String selectedServiceProvider = getIntent().getStringExtra("selectedServiceProvider");

        TextView service = (TextView)findViewById(R.id.showServiceProvider);
        service.setText("Paid through "+ selectedServiceProvider);

        setListViewHeightBasedOnChildren(confirmedList);

        //to check can be delete
        System.out.println("From order confirmation page");
        for(int j = 0; j<orderStack.orderList.size();j++){
            System.out.println(orderStack.orderList.get(j).getOrderName());
            System.out.println(orderStack.orderList.get(j).getOrderNameExtra());
            System.out.println("order price = "+orderStack.orderList.get(j).getOrderPrice());
            System.out.println("Food List");
            for(int k = 0; k<orderStack.orderList.get(j).foodList.size();k++){
                System.out.println(orderStack.orderList.get(j).foodList.get(k).getFoodName());
                System.out.println(orderStack.orderList.get(j).foodList.get(k).getFoodPrice());
                System.out.println(orderStack.orderList.get(j).foodList.get(k).getFoodType());
            }
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ArrayAdapter listAdapter = (ArrayAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void orderMoreBackToHome(){
        orderStack.setEmpty();
        Intent intent = new Intent(this,homev1Activity.class);
        startActivity(intent);
    }
}
