package com.example.vcanteen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.widget.Toast;

import com.example.vcanteen.POJO.orderProgress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class vendorMenuv2Activity extends AppCompatActivity {

    orderStack orderStack;
    order order;
    TextView minCombinationPrice;
    ArrayList<order> orderList;
    String restaurantNameString; //just add for minor fix in order confirmation
    int restaurantNumber;
    String restaurantUrl;
    Bitmap bitmap;


    vendorAlacarteMenu menuVendor;
    vendorAlacarteMenu menuVendorAvailable;
    vendorAlacarteMenu menuVendorSoldOut;

    String searchKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_menu_v2);

        System.out.println("entered vendor menu");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        orderStack = com.example.vcanteen.orderStack.getInstance();

        //orderStack = getIntent().getExtras().getParcelable("orderStack"); // delete if don't want from home activity
        restaurantNameString = getIntent().getStringExtra("chosenVendor"); // delete if don't want from home activity  //just add for minor fix in order confirmation
        restaurantNumber = getIntent().getIntExtra("vendor id",0);
        restaurantUrl = getIntent().getStringExtra("vendor url");

        orderStack.setVendorId(restaurantNumber);

        ImageView vendorPic = findViewById(R.id.vendorPic);
        bitmap = getBitmapFromURL(restaurantUrl);
        vendorPic.setImageBitmap(bitmap);
        getMenuList();
        EditText searchBox = findViewById(R.id.searchBox);
        Button search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchKeyword = searchBox.getText().toString().trim();
            }
        });

        //menuVendor = new vendorAlacarteMenu(getIntent().getExtras().getParcelable("vendorMenu"));
        //menuVendorAvailable = getIntent().getExtras().getParcelable("vendorMenuAvailable");
        //menuVendorSoldOut = getIntent().getExtras().getParcelable("vendorMenuSoldOut");
        //minCombinationPrice = findViewById(R.id.minCombinationPrice);
        //minCombinationPrice.setText("Starting from "+ getIntent().getExtras().getInt("minCombinationPrice") +" ฿");
        //addAlacarteToList(menuVendor.availableList, menuVendor.soldOutList);
        //plan b: intent as arrayList call addAlacarteToList at vendorlist and change to static method

///TRY SINGLETON////
//        orderStack.setCustomerId(sharedPref.getInt("customerId",0));

        orderStack.setVendorId(restaurantNumber);
        orderStack.setOrderList(new ArrayList<order>());
        orderStack.setTotalPrice(0);
        orderStack.setCreatedAt(new Date());

        //try date
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Created at "+dateformat.format(orderStack.getCreatedAt()));


        TextView restaurantName = (TextView)findViewById(R.id.restaurantName);// delete if don't want from home activity
        restaurantName.setText(""+restaurantNameString);// delete if don't want from home activity   //just add for minor fix in order confirmation

        // to open cutomize order activity
        android.support.constraint.ConstraintLayout tappable_customize = (android.support.constraint.ConstraintLayout)findViewById(R.id.tappable_customize);
        tappable_customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomizeActivity();
            }
        });
        // wait to connect with BE
//        boolean hasCombination = false; < just an assumption for testing
//        if(hasCombination==false){
//            tappable_customize.setVisibility(View.GONE);
//        }

    }
    protected ArrayList<food> shownFoodList;
    protected ArrayList<food> availableList;
    protected ArrayList<food> soldOutList;
    protected ListView menuList;
    protected ListAdapter testAdapter1;

    private void addAlacarteToList(ArrayList<availableList> inputAvailableList, ArrayList<soldOutList> inputSoldOutList) {

////////  DEAL WITH A LA CARTE ////////////

        availableList = new ArrayList<>(); //need to get from BE
        soldOutList = new ArrayList<>();   //need to get from BE

        for(availableList list : inputAvailableList) {
            availableList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "A LA CARTE", list.getFoodCategory()));
        }
        for(soldOutList list : inputSoldOutList) {
            soldOutList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "A LA CARTE", null));
        }
        shownFoodList = new ArrayList<>(availableList);
        shownFoodList.addAll(soldOutList);
        testAdapter1 = new menuListAdapterv2(this,shownFoodList,availableList.size());
        menuList = findViewById(R.id.menuList);
        menuList.setAdapter(testAdapter1);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < availableList.size()) {
                    Intent passALaCarte = new Intent(vendorMenuv2Activity.this, normalOrderActivity.class);
                    passALaCarte.putExtra("chosenFood", shownFoodList.get(position));
                    //passALaCarte.putExtra("orderStack", orderStack);
                    passALaCarte.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
                    startActivity(passALaCarte);
                }else{
                    menuList.getChildAt(position).setClickable(false);
                }
            }
        });

    }
    public void getMenuList() {
        ProgressDialog pd = new ProgressDialog(vendorMenuv2Activity.this);
        pd.setMessage("loading");
        pd.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<vendorAlacarteMenu> call = jsonPlaceHolderApi.getVendorAlacarte(restaurantNumber);
        call.enqueue(new Callback<vendorAlacarteMenu>() {
            @Override
            public void onResponse(Call<vendorAlacarteMenu> call, Response<vendorAlacarteMenu> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(vendorMenuv2Activity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                vendorAlacarteMenu menu = response.body();
                System.out.println("Received Restaurant Name: "+menu.getVendorInfo().restaurantName);
                System.out.println("Received Restaurant URL: "+menu.getVendorInfo().vendorImage);
                minCombinationPrice = findViewById(R.id.minCombinationPrice);
                minCombinationPrice.setText("Starting from "+ menu.getMinCombinationPrice() +" ฿");
                addAlacarteToList(menu.availableList, menu.soldOutList);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<vendorAlacarteMenu> call, Throwable t) {
                System.out.println("Entered Menu Fail.....");

            }
        });
    }

    private void openCustomizeActivity() {
        Intent intent = new Intent(this, customizeOrderActivity.class);
        intent.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
        startActivity(intent);
    }


    public void openCartActivity(View view) {
        if (orderStack.totalPrice != 0) {
            Intent intent = new Intent(this, cartActivity.class);
            intent.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
            startActivity(intent);
        } else {
            Toast.makeText(vendorMenuv2Activity.this, "No Order in the Cart!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        orderStack o = intent.getExtras().getParcelable("orderStackFromCart");
    }

    public Bitmap getBitmapFromURL(String src){
        try{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.droplist, menu);
        return true;
    }
    protected ArrayList<food> resultList;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_price:
                /*for(resultList list : availableList) {
                    availableList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "A LA CARTE"));
                }*/
                /*List<String> result = Arrays.asList(availableList.list.getFoodName());
                ArrayList<food> cloneAvailable = availableList;
                String name = cloneAvailable.get();
                Collections.sort(result);
                System.out.println(result);*/
                //availableList.toString();
                //resultList = Collections.sort(availableList);
                for(int i = 0; i < availableList.size(); i++) {
                    if (availableList.get(i).getFoodPrice() <= (availableList.get(i+1).getFoodPrice())) {
                        //food temp = new food(availableList.get(i).getFoodId(), availableList.get(i).getFoodName(), availableList.get(i).getFoodPrice(), availableList.get(i).getFoodType(), availableList.get(i).getFoodCategory());
                        System.out.println("sort price: i = " + availableList.get(i));
                        resultList.add(availableList.get(i));
                    } else {
                        //food temp = new food(availableList.get(i).getFoodId(), availableList.get(i).getFoodName(), availableList.get(i).getFoodPrice(), availableList.get(i).getFoodType(), availableList.get(i).getFoodCategory());
                        //System.out.println("i+1 = " + availableList.get(i));
                        resultList.add(availableList.get(i+1));
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Sorted by Price", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_az:
                for(int i = 0; i < availableList.size(); i++) {
                    if (availableList.get(i).getFoodName().compareTo(availableList.get(i+1).getFoodName()) > 0) {
                        food temp = availableList.get(i);
                        System.out.println("sort name" + temp.toString());
                        resultList.add(temp);
                    } else {
                        food temp = availableList.get(i+1);
                        resultList.add(temp);
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Sorted by Name", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_all:
                testAdapter1 = new menuListAdapterv2(this,shownFoodList,availableList.size());
                menuList.setAdapter(testAdapter1);
                menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position < availableList.size()) {
                            Intent passALaCarte = new Intent(vendorMenuv2Activity.this, normalOrderActivity.class);
                            passALaCarte.putExtra("chosenFood", shownFoodList.get(position));
                            passALaCarte.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
                            startActivity(passALaCarte);
                        }else{
                            menuList.getChildAt(position).setEnabled(false);
                        }
                    }
                });
                Toast.makeText(this, "All", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_meat:
                for(int i = 0; i < availableList.size(); i++) {
                    if (availableList.get(i).getFoodCategory().equals("MEAT")) {
                        food temp = availableList.get(i);
                        //System.out.println(availableList.get(i));
                        resultList.add(temp);
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Meat Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_noodles:
                for(int i = 0; i < availableList.size(); i++) {
                    if (availableList.get(i).getFoodCategory().equals("NOODLE")) {
                        food temp = availableList.get(i);
                        //System.out.println(availableList.get(i));
                        resultList.add(temp);
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Noodles Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_soup:
                System.out.println("SOUP test " + availableList.get(1).getFoodCategory().equals("SOUP"));
                for(int i = 0; i < availableList.size(); i++) {
                    if (availableList.get(i).getFoodCategory().equals("SOUP")) {
                        food temp = availableList.get(i);
                        //System.out.println(availableList.get(i));
                        resultList.add(temp);
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Soup Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_spicy:
                for(int i = 0; i < availableList.size(); i++) {
                    if (availableList.get(i).getFoodCategory().equals("SPICY")) {
                        food temp = availableList.get(i);
                        //System.out.println(availableList.get(i));
                        resultList.add(temp);
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Spicy Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_vegan:
                for(int i = 0; i < availableList.size(); i++) {
                    if (availableList.get(i).getFoodCategory().equals("VEGAN")) {
                        food temp = availableList.get(i);
                        //System.out.println(availableList.get(i));
                        resultList.add(temp);
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Vegan Category", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onResume() {
        super.onResume();
        getMenuList();
    }
    public void setResultListAdapter(ArrayList<food> resultList) {
        testAdapter1 = new menuListAdapterv2(this,resultList,resultList.size());
        menuList.setAdapter(testAdapter1);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < resultList.size()) {
                    Intent passALaCarte = new Intent(vendorMenuv2Activity.this, normalOrderActivity.class);
                    passALaCarte.putExtra("chosenFood", resultList.get(position));
                    passALaCarte.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
                    startActivity(passALaCarte);
                }else{
                    menuList.getChildAt(position).setEnabled(false);
                }
            }
        });
    }
}
