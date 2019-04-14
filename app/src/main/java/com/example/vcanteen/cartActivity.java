package com.example.vcanteen;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.availablePaymentMethod;
import com.example.vcanteen.POJO.newOrder;
import com.example.vcanteen.POJO.paymentMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class cartActivity extends AppCompatActivity {

    RadioButton scbEasy, kplus, trueMoney,cunex;

    TextView orderTotalPrice, orderTotalItems, orderTotalPriceTop;

    orderListAdapter orderAdapter;

    String restaurantNameString; //just add for minor fix in order confirmation

    int selectedMoneyAccountId;

    int total=0;

    //dealing with popup
    Dialog showpopup;
    ImageView confirmImgButton;
    TextView cancelButton;
    Button confirmButton;

    orderStack orderStack;
    ArrayList<RadioButton> unavailableService;
    String selectedServiceProvider;
    int customerMoneyAccountId;
    ArrayList<paymentList> paymentList;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        orderStack = com.example.vcanteen.orderStack.getInstance();
        restaurantNameString = getIntent().getStringExtra("sendRestaurantName"); //just add for minor fix in order confirmation
        paymentList = new ArrayList<>(); // need to get from BE

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        System.out.println("Customer ID: "+orderStack.getCustomerId());
        Call<paymentMethod> call = jsonPlaceHolderApi.getPaymentMethod(orderStack.getCustomerId());


        call.enqueue(new Callback<paymentMethod>() {
            @Override
            public void onResponse(Call<paymentMethod> call, Response<paymentMethod> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(cartActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }

                paymentMethod methods = response.body();
                ArrayList<availablePaymentMethod> lists = methods.availablePaymentMethod;

                for (availablePaymentMethod list :lists){
                    System.out.println("payment");
                    System.out.println(list.getCustomerMoneyAccountId()+","+list.getServiceProvider());

                    paymentList.add(new paymentList(list.getCustomerMoneyAccountId(), list.getServiceProvider()));

                }
                scbEasy = findViewById(R.id.scbEasy);
                kplus = findViewById(R.id.kplus);
                trueMoney = findViewById(R.id.trueMoney);
                cunex = findViewById(R.id.cunex);

//// FOR DISABLE UNAVAILABLE SERVICE PROVIDER ////
                unavailableService = new ArrayList<>();
                unavailableService.add(scbEasy);
                unavailableService.add(kplus);
                unavailableService.add(cunex);
                unavailableService.add(trueMoney);

                String a,b;
                for(int i = 0; i<paymentList.size();i++){
                    a = String.valueOf(paymentList.get(i).serviceProvider.charAt(0));
                    for(int j = 0; j < unavailableService.size();j++){
                        b = String.valueOf(unavailableService.get(j).getText().toString().charAt(0));
                        if(a.equalsIgnoreCase(b)){
                            unavailableService.remove(j);
                        }
                    }
                }

                for(int k = 0; k<unavailableService.size();k++){
                    unavailableService.get(k).setEnabled(false);
                    unavailableService.get(k).setTextColor(Color.parseColor("#E0E0E0"));
                }

            }

            @Override
            public void onFailure(Call<paymentMethod> call, Throwable t) {

            }
        });


//// FOR DEALING WITH POP UP ////
        showpopup = new Dialog(this);
        confirmImgButton = (ImageView)findViewById(R.id.confirmImgButton);
        confirmImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total!=0) {

                    showPopUp();
                }

            }
        });

//// FOR DEALING WITH ORDER LIST ////
        //orderStack = getIntent().getExtras().getParcelable("sendOrderStack");


        orderAdapter = new orderListAdapter(this,orderStack);
        final ListView orderList = findViewById(R.id.orderList);
        orderList.setAdapter(orderAdapter);



        orderTotalItems = (TextView) findViewById(R.id.orderTotalItems);
        orderTotalPrice = (TextView) findViewById(R.id.orderTotalPrice);
        orderTotalPriceTop = (TextView) findViewById(R.id.orderTotalPrice1);

        for(int i = 0; i<orderStack.orderList.size(); i++){
            total += orderStack.orderList.get(i).orderPrice;
        }

        orderStack.setTotalPrice(total);
        orderTotalItems.setText("Total "+ orderStack.orderList.size()+" item(s)");
        orderTotalPrice.setText("" + orderStack.totalPrice +" ฿");
        orderTotalPriceTop.setText("" + orderStack.totalPrice +" ฿");



    }

    private void addPayment(int customerMoneyAccountId, String serviceProvider) {
//        paymentList.add(new paymentList(customerMoneyAccountId, serviceProvider));
    }

    public void showPopUp(){
        showpopup.setContentView(R.layout.cart_confirm_popup);
        confirmButton = (Button)showpopup.findViewById(R.id.confirmButtonPassword);
        cancelButton = (TextView)showpopup.findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProcessingPayment();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpopup.dismiss();
            }
        });

        showpopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showpopup.show();
    }

    public void openProcessingPayment() {
        // fill customer money account in orderstack
        // fill timestamp
        //orderStack.setCustomerMoneyAccount(/* fill here*/);

        for(int i=0;i<paymentList.size();i++){
            String x = String.valueOf(selectedServiceProvider.charAt(0));
            if(x.equalsIgnoreCase(String.valueOf(paymentList.get(i).serviceProvider.charAt(0)))){
                customerMoneyAccountId = paymentList.get(i).getCustomerMoneyAccountId();
            }
        }
        orderStack.setCustomerMoneyAccount(customerMoneyAccountId);

        newOrder checkout = new newOrder();
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        int i = sharedPref.getInt("customerId", 0);
        System.out.println("id: "+i);
        checkout.customerId = orderStack.getCustomerId();
        checkout.vendorId = orderStack.getVendorId();
        checkout.order = orderStack.getOrderList();
        checkout.totalPrice = orderStack.getTotalPrice();
//        checkout.createdAt = "2019-04-02 11:00:22";

//        String pattern = "HH:mm:ss";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        Date date = simpleDateFormat.parse("22:00:03");
//        System.out.println(date);

//        System.out.println(d.toString());
        checkout.customerMoneyAccountId = orderStack.getCustomerMoneyAccount();
        orderStack.setCreatedAt(new Date());
        System.out.println("order: "+orderStack.getOrderList().toString());
        System.out.println(checkout.toString());
//        orderStack.setCreatedAt(new Date());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        System.out.println("BB: "+checkout.toString());
        Call<newOrder> call = jsonPlaceHolderApi.postOrder(checkout);

        System.out.println("entered post...");
        call.enqueue(new Callback<newOrder>() {
            @Override
            public void onResponse(Call<newOrder> call, Response<newOrder> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(cartActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                    System.out.println("POST ERROR : "+response.code());
                    return;
                }
                System.out.println("POST SUCCESS");
            }

            @Override
            public void onFailure(Call<newOrder> call, Throwable t) {
                System.out.println("POST ERROR");
            }
        });


        Intent intent = new Intent(this, processingPaymentActivity.class);
        intent.putExtra("orderStack", orderStack);
        intent.putExtra("selectedServiceProvider", selectedServiceProvider);
        intent.putExtra("sendRestaurantName", restaurantNameString);//just add for minor fix in order confirmation

        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.scbEasy:
                if (checked)
                    scbEasy.setTextColor(Color.parseColor("#FF5A5A"));
                unClickRadioButton(kplus,cunex,trueMoney);
                selectedServiceProvider = scbEasy.getText().toString();
                break;
            case R.id.kplus:
                if (checked)
                    kplus.setTextColor(Color.parseColor("#FF5A5A"));
                unClickRadioButton(cunex,scbEasy,trueMoney);
                selectedServiceProvider = kplus.getText().toString();
                break;
            case R.id.cunex:
                if (checked)
                    cunex.setTextColor(Color.parseColor("#FF5A5A"));
                unClickRadioButton(kplus,scbEasy,trueMoney);
                selectedServiceProvider = cunex.getText().toString();
                break;
            case R.id.trueMoney:
                if (checked)
                    trueMoney.setTextColor(Color.parseColor("#FF5A5A"));
                unClickRadioButton(kplus,scbEasy,cunex);
                selectedServiceProvider = trueMoney.getText().toString();
                break;
        }
    }

    public void unClickRadioButton(RadioButton a, RadioButton b, RadioButton c){
        a.setChecked(false);
        b.setChecked(false);
        c.setChecked(false);
        if(!unavailableService.contains(a)){
            a.setTextColor(Color.parseColor("#828282"));
        }
        if(!unavailableService.contains(b)){
            b.setTextColor(Color.parseColor("#828282"));
        }
        if(!unavailableService.contains(c)){
            c.setTextColor(Color.parseColor("#828282"));
        }
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
        Intent goToMenu = new Intent(cartActivity.this,vendorMenuActivity.class);
        goToMenu.putExtra("orderStackFromCart",orderStack);
        //goToMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goToMenu);
        finish();
    }


    public void updateOrder() {

        orderStack.orderList.remove(orderAdapter.passPosition());
        total = 0;
        for(int i = 0; i<orderStack.orderList.size(); i++){
            total += orderStack.orderList.get(i).orderPrice;
        }

        orderStack.setTotalPrice(total);
        orderTotalItems.setText("Total "+ orderStack.orderList.size()+" item(s)");
        orderTotalPrice.setText("" + total +"");
        orderTotalPriceTop.setText("" + orderStack.totalPrice +" ฿");

        if(total == 0){
            scbEasy.setEnabled(false);
            scbEasy.setTextColor(Color.parseColor("#E0E0E0"));
            kplus.setEnabled(false);
            kplus.setTextColor(Color.parseColor("#E0E0E0"));
            trueMoney.setEnabled(false);
            trueMoney.setTextColor(Color.parseColor("#E0E0E0"));
            cunex.setEnabled(false);
            cunex.setTextColor(Color.parseColor("#E0E0E0"));
        }

    }
}
