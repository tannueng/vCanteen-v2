package com.example.vcanteen;

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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_menu_v2);

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
        System.out.println("check in coming url: "+restaurantUrl);

        orderStack.setVendorId(restaurantNumber);

        ImageView vendorPic = findViewById(R.id.vendorPic);
        bitmap = getBitmapFromURL(restaurantUrl);
        vendorPic.setImageBitmap(bitmap);


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
                //get result here
                vendorAlacarteMenu menu = response.body();
                System.out.println("Received Restaurant Name: "+menu.getVendorInfo().restaurantName);
                System.out.println("Received Restaurant URL: "+menu.getVendorInfo().vendorImage);
                minCombinationPrice = findViewById(R.id.minCombinationPrice);
                minCombinationPrice.setText("Starting from "+ menu.getMinCombinationPrice() +" ฿");
                addAlacarteToList(menu.availableList, menu.soldOutList);
            }

            @Override
            public void onFailure(Call<vendorAlacarteMenu> call, Throwable t) {
                System.out.println("Entered Menu Fail.....");

            }
        });




///TRY SINGLETON////
//        orderStack.setCustomerId(sharedPref.getInt("customerId",0));

        orderStack.setVendorId(restaurantNumber);
        orderStack.setOrderList(new ArrayList<order>());
        orderStack.setTotalPrice(0);
        orderStack.setCreatedAt(new Date());

        //try date
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Create at "+dateformat.format(orderStack.getCreatedAt()));


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

    private void addAlacarteToList(ArrayList<availableList> inputAvaliableList, ArrayList<soldOutList> inputSoldOutList) {

////////  DEAL WITH A LA CARTE ////////////

        final ArrayList<food> availableList = new ArrayList<>(); //need to get from BE
        ArrayList<food> soldOutList = new ArrayList<>();   //need to get from BE

        for(availableList list : inputAvaliableList) {
            availableList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "A LA CARTE"));
        }
        for(soldOutList list : inputSoldOutList) {
            soldOutList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "A LA CARTE"));
        }

        final ArrayList<food> shownFoodList = new ArrayList<>(availableList);
        shownFoodList.addAll(soldOutList);

        //TextView restaurantName = (TextView)findViewById(R.id.restaurantName);
        //restaurantName.setText(""+);


        ListAdapter testAdapter1 = new menuListAdapterv2(this,shownFoodList,availableList.size());
        final ListView menuList = findViewById(R.id.menuList);
        menuList.setAdapter(testAdapter1);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<availableList.size()) {
                    Intent passALaCarte = new Intent(vendorMenuv2Activity.this, normalOrderActivity.class);
                    passALaCarte.putExtra("chosenFood", shownFoodList.get(position));
                    //passALaCarte.putExtra("orderStack", orderStack);
                    passALaCarte.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
                    startActivity(passALaCarte);
                }else{
                    menuList.getChildAt(position).setEnabled(false);
                }
            }
        });

    }


    private void openCustomizeActivity() {
        Intent intent = new Intent(this, customizeOrderActivity.class);
        intent.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
        startActivity(intent);
    }


    public void openCartActivity(View view) {
        if(orderStack.totalPrice != 0){
            Intent intent = new Intent(this, cartActivity.class);
            intent.putExtra("sendRestaurantName", restaurantNameString); //just add for minor fix in order confirmation
            startActivity(intent);
        }else{
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_price:
                //you can do anything here!!!
                Toast.makeText(this, "Sort by price", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_az:
                Toast.makeText(this, "Sort by A-Z", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_all:
                Toast.makeText(this, "All", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_meat:
                Toast.makeText(this, "Meat category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_noodles:
                Toast.makeText(this, "Moodles category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_soup:
                Toast.makeText(this, "Soup category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_spicy:
                Toast.makeText(this, "Spicy category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_vegan:
                Toast.makeText(this, "Vegan category", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
