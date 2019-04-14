package com.example.vcanteen;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import java.lang.Integer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vcanteen.POJO.extraItemList;
import com.example.vcanteen.POJO.extraList;
import com.example.vcanteen.POJO.menuExtra;
import com.example.vcanteen.POJO.paymentMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class normalOrderActivity extends AppCompatActivity {

    int eop,op;
    TextView orderQuantity, orderPrice, foodPrice, foodName, addFoodName, addFoodPrice;
    ImageView addToCartImg;
    foodListAdapter adapter ;
    ArrayList<Boolean> foodchk = new ArrayList<Boolean>();

    food chosenALaCarte;
    orderStack orderStack;
    order order;
    int oPrice;
    int mainAmount;

    ArrayList<food> foodList;
    food[] sendfoodList;
    SparseBooleanArray mCheckStates;
    ArrayList<food> shownFoodList;

    String restaurantNameString; //just add for minor fix in order confirmation

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_order);

        restaurantNameString = getIntent().getStringExtra("sendRestaurantName"); //just add for minor fix in order confirmation




        orderPrice = (TextView) findViewById(R.id.orderPrice);
        foodPrice = (TextView) findViewById(R.id.baseComPrice);
        foodName = (TextView) findViewById(R.id.baseComName);

        //Get Selected A La Carte Info
        chosenALaCarte = getIntent().getExtras().getParcelable("chosenFood");
        //orderStack = getIntent().getExtras().getParcelable("orderStack");
        orderStack = com.example.vcanteen.orderStack.getInstance();

        foodName.setText(chosenALaCarte.foodName);
        foodPrice.setText(""+chosenALaCarte.foodPrice);
        orderPrice.setText(""+chosenALaCarte.foodPrice);
        oPrice = chosenALaCarte.foodPrice;
        mainAmount = 1;
        eop = 0;
        op = chosenALaCarte.foodPrice;

        orderQuantity = (TextView) findViewById(R.id.orderQuantity);
        TextView subtractSign = findViewById(R.id.subtractSign);
        TextView addSign = findViewById(R.id.addSign);


        subtractSign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                decreaseQuntity();
            }
        });
        addSign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                increaseQuntity();
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<menuExtra> call = jsonPlaceHolderApi.getMenuExtra(1,chosenALaCarte.foodId);

        call.enqueue(new Callback<menuExtra>() {
            @Override
            public void onResponse(Call<menuExtra> call, Response<menuExtra> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(normalOrderActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                System.out.println("menu Extra success");

                menuExtra menuExtra = response.body();

                ArrayList<food> availableExtraList = new ArrayList<>(); //need to get from BE
                ArrayList<food> soldOutExtraList = new ArrayList<>();   //need to get from BE

                ArrayList<extraList> lists = menuExtra.extraList;

                for (extraList list : lists) {
                    availableExtraList.add(new food(list.foodId,list.foodName,list.foodPrice,"EXTRA"));
                }

                shownFoodList = new ArrayList<>(availableExtraList);
                shownFoodList.addAll(soldOutExtraList);

                adapter = new foodListAdapter(normalOrderActivity.this,shownFoodList,availableExtraList.size());
                final ListView extraListShow = findViewById(R.id.extraList);
                extraListShow.setAdapter(adapter);

                foodList = new ArrayList<>();

                addToCartImg = (ImageView)findViewById(R.id.addToCartImg);
                addToCartImg.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        openCart();
                    }
                });
            }

            @Override
            public void onFailure(Call<menuExtra> call, Throwable t) {
                System.out.println("menu Extra fail");
            }
        });



        //int numCheckBox = ... //how many checkbox are checked
//        foodList = new food[2]; //[numCheckBox+1];
//        foodList[0] = chosenALaCarte;
//        foodList[1] = shownFoodList.get(1);

        //adapter.mCheckStates.o

    }

    private void addMenuExtraToList(food inputFood, ArrayList<extraList> inputExtraList) {


    }

    public void notifyExtraChange(){
        int tempex = 0;
        for (int i = 0; i < shownFoodList.size(); i++) {
            if (adapter.isChecked(i) == true) {
                tempex += shownFoodList.get(i).getFoodPrice();
            }
        }
        eop = tempex;
        orderPrice.setText("" + (eop + op)*mainAmount );
    }


    private void increaseQuntity() {
        if(mainAmount<9){
            mainAmount = mainAmount+1;
            //Toast.makeText(normalOrderActivity.this, "Now "+amount+"!", Toast.LENGTH_LONG).show();
            orderQuantity.setText(""+mainAmount+"");
            orderPrice.setText("" + (eop + op)*mainAmount);
        }
    }

    public void decreaseQuntity(){
        if(mainAmount>1){
            mainAmount = mainAmount-1;
            //Toast.makeText(normalOrderActivity.this, "Now "+amount+"!", Toast.LENGTH_LONG).show();
            orderQuantity.setText(""+mainAmount+"");
            orderPrice.setText("" + (eop + op)*mainAmount);
        }
    }

    public void openCart(){

        foodList.add(chosenALaCarte);

        String extraName = "";

        int n = 0;
        for(int i=0;i<shownFoodList.size();i++)
        {
            if(adapter.isChecked(i)==true)
            {
                foodList.add(shownFoodList.get(i));
                extraName = extraName + adapter.foodList.get(i).getFoodName()+ ", " ;
            }
        }
        if(!extraName.equals("")) {
            System.out.println("BEFORE: "+extraName);
            extraName = extraName.substring(0, extraName.length() - 2);
            System.out.println("AFTER: "+extraName);

        }

        order = new order(chosenALaCarte.foodName,extraName, (op + eop) ,foodList);

        // when quantity more than 1
        for(int i = 0; i<mainAmount; i++){
            orderStack.orderList.add(order);
        }

        //to check can be delete
        for(int j = 0; j<orderStack.orderList.size();j++){
            System.out.println(orderStack.orderList.get(j).getOrderName());
            System.out.println(orderStack.orderList.get(j).getOrderNameExtra());
            System.out.println("EXTRA : "+orderStack.orderList.get(j).getOrderNameExtra());
            System.out.println("order price = "+orderStack.orderList.get(j).getOrderPrice());
            System.out.println("Food List");
            for(int k = 0; k<orderStack.orderList.get(j).foodList.size();k++){
                System.out.println(orderStack.orderList.get(j).foodList.get(k).getFoodName());
                System.out.println(orderStack.orderList.get(j).foodList.get(k).getFoodPrice());
                System.out.println(orderStack.orderList.get(j).foodList.get(k).getFoodType());
            }
        }



        Log.d("1",orderStack.orderList.get(0).orderName);
        Intent intent = new Intent(this, cartActivity.class);
        intent.putExtra("sendRestaurantName", restaurantNameString);//just add for minor fix in order confirmation
        //intent.putExtra("sendOrderStack", orderStack);

        startActivity(intent);
    }
}
