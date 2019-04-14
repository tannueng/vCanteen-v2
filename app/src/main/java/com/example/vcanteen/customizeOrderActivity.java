package com.example.vcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.baseList;
import com.example.vcanteen.POJO.extraList;
import com.example.vcanteen.POJO.mainList;
import com.example.vcanteen.POJO.vendorCombinationMenu;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class customizeOrderActivity extends AppCompatActivity {

    String[] items1 = { "Base1", "Base2" };
    String[] items2 = { "Main1", "Main2", "Main3", "Main4", "Main5" ,"Main1", "Main2", "Main3", "Main4", "Main5"};
    String[] items3 = { "Extra1", "Extra2", "Extra3" ,"Extra4","Extra5"};
    int[] items3Price = {10,5,3,6,2};

    int mainP, extraP, baseP;

    orderStack orderStack;
    order order;
    ArrayList<food> mainList, extraList, baseList, foodList;
    //ListAdapter testMainAdapter;

    foodListAdapterForCombi testMainAdapter, testExtraAdapter;

    combinationBaseListAdapter testBaseAdapter; // pin add
    food b; //pin add

    TextView orderPriceCombi;
    ImageView addToCartImgFromCustomize;

    String restaurantNameString; //just add for minor fix in order confirmation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_order);

        restaurantNameString = getIntent().getStringExtra("sendRestaurantName"); //just add for minor fix in order confirmation

        orderStack = com.example.vcanteen.orderStack.getInstance();
        orderPriceCombi = (TextView)findViewById(R.id.orderPriceCombi);

        foodList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<vendorCombinationMenu> call = jsonPlaceHolderApi.getVendorCombination(orderStack.getVendorId());

        call.enqueue(new Callback<vendorCombinationMenu>() {
            @Override
            public void onResponse(Call<vendorCombinationMenu> call, Response<vendorCombinationMenu> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(customizeOrderActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                vendorCombinationMenu menu = response.body();
                addCombinationToList(menu.baseList, menu.mainList,menu.extraList);
            }

            @Override
            public void onFailure(Call<vendorCombinationMenu> call, Throwable t) {
                System.out.println("Entered Menu Fail.....");
            }
        });




    }

    private void addCombinationToList(ArrayList<baseList> inputBaseList, ArrayList<mainList> inputMainList, ArrayList<extraList> inputExtraList) {

///// FOR COMBINATION BASE //////
        baseList = new ArrayList<>(); //need to get from BE

        for(baseList list : inputBaseList) {
            baseList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "COMBINATION_BASE"));
        }
        // for testing
//        baseList.add(new food(3, "Jasmine Rice", 5, "COMBINATION_BASE"));
//        baseList.add(new food(1, "Sticky Rice", 10, "COMBINATION_BASE"));
//        baseList.add(new food(2, "Fried Rice", 15, "COMBINATION_BASE"));

        testBaseAdapter = new combinationBaseListAdapter(this, baseList);
        final ListView baseListShow = findViewById(R.id.list1);

        baseListShow.setAdapter(testBaseAdapter);

        setListViewHeightBasedOnChildren(baseListShow);

///// FOR COMBINATION MAIN //////
        final ArrayList<food> availableMainList = new ArrayList<>(); //need to get from BE
        ArrayList<food> soldOutMainList = new ArrayList<>();   //need to get from BE

        for(mainList list : inputMainList) {
            if(list.getFoodStatus().equals("AVAILABLE")) {
                availableMainList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "COMBINATION_BASE"));
            } else {
                soldOutMainList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "COMBINATION_BASE"));
            }

        }

        // for testing
//        availableMainList.add(new food(3, "Pork", 15, "COMBINATION_MAIN"));
//        availableMainList.add(new food(1, "Beef", 10, "COMBINATION_MAIN"));
//        availableMainList.add(new food(2, "Fried Chicken", 15, "COMBINATION_MAIN"));
//        availableMainList.add(new food(3, "Curry", 15, "COMBINATION_MAIN"));
//        soldOutMainList.add(new food(32, "Salad", 25, "COMBINATION_MAIN"));
//        soldOutMainList.add(new food(13, "Curry Beef", 35, "COMBINATION_MAIN"));

        mainList = new ArrayList<>(availableMainList);
        mainList.addAll(soldOutMainList);

        testMainAdapter = new foodListAdapterForCombi(this, mainList, availableMainList.size());
        final ListView mainListShow = findViewById(R.id.list2);
        mainListShow.setAdapter(testMainAdapter);

        setListViewHeightBasedOnChildren(mainListShow);

///// FOR EXTRA //////
        extraList = new ArrayList<>();

        for(extraList list : inputExtraList) {
            extraList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "EXTRA"));
        }
//        extraList.add(new food(11, "More Rice", 5, "EXTRA"));
//        extraList.add(new food(12, "Extra Large", 10, "EXTRA"));
//        extraList.add(new food(13, "No Spicy", 0, "EXTRA"));
//        extraList.add(new food(14, "Extra Spicy", 0, "EXTRA"));
//        extraList.add(new food(15, "No Vegetable", 0, "EXTRA"));

        testExtraAdapter = new foodListAdapterForCombi(this,extraList,extraList.size());
        final ListView extraListShow = findViewById(R.id.list3);
        extraListShow.setAdapter(testExtraAdapter);

        setListViewHeightBasedOnChildren(extraListShow);

        addToCartImgFromCustomize = findViewById(R.id.addToCartImgFromCustomize);
        addToCartImgFromCustomize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("Try open cart");
                openCart();
            }
        });
    }

    public void notifyExtraChangeBase() {
        System.out.println("Enter Notify Change Extra Base Has Enter");
        b=testBaseAdapter.getSelectedBase();
        baseP = b.foodPrice;
        orderPriceCombi.setText(""+(mainP+extraP+baseP));

    }

    public void notifyExtraChange() {

        int tempex = 0;
        for (int i = 0; i < mainList.size(); i++) {
            if (testMainAdapter.isChecked(i)==true) {
                tempex += mainList.get(i).getFoodPrice();
            }
        }
        mainP = tempex;

        int tempex2 = 0;
        for (int i = 0; i < extraList.size(); i++) {
            if (testExtraAdapter.isChecked(i)==true) {
                tempex2 += extraList.get(i).getFoodPrice();
            }
        }
        extraP = tempex2;
        orderPriceCombi.setText(""+(mainP+extraP+baseP));

    }

    public void openCart(){

        foodList.add(b); // add base

        //add Main to orderStack
        String mainName = ""+ b.getFoodName();
        for(int i=0;i<mainList.size();i++)
        {
            if(testMainAdapter.isChecked(i)==true)
            {
                foodList.add(mainList.get(i));
                mainName = mainName + ", " + testMainAdapter.foodList.get(i).getFoodName();
            }
        }

        String extraName = "";
        for(int i=0;i<extraList.size();i++)
        {
            if(testExtraAdapter.isChecked(i)==true)
            {
                foodList.add(extraList.get(i));
                extraName = extraName + testExtraAdapter.foodList.get(i).getFoodName()+ ", " ;
            }
        }
        if(!extraName.equals("")) {
            System.out.println("BEFORE: "+extraName);
            extraName = extraName.substring(0, extraName.length() - 2);
            System.out.println("AFTER: "+extraName);

        }


        order = new order(mainName,extraName,Integer.parseInt(orderPriceCombi.getText().toString()),foodList);
        orderStack.orderList.add(order);

        Intent intent = new Intent(this, cartActivity.class);

        intent.putExtra("sendRestaurantName", restaurantNameString);//just add for minor fix in order confirmation


        startActivity(intent);
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



}

