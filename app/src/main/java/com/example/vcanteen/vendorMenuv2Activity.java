package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import org.w3c.dom.Text;

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
    EditText searchBox;
    Button search_btn;
    TextView no_result1;
    TextView no_result2;
    ImageView search_icon;
    android.support.constraint.ConstraintLayout tappable_customize;

    vendorAlacarteMenu menuVendor;
    vendorAlacarteMenu menuVendorAvailable;
    vendorAlacarteMenu menuVendorSoldOut;

    String searchKeyword;
    String currentCategory = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_menu_v2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("entered page");
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.menuPage);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });
        orderStack = com.example.vcanteen.orderStack.getInstance();

        //orderStack = getIntent().getExtras().getParcelable("orderStack"); // delete if don't want from home activity
        restaurantNameString = getIntent().getStringExtra("chosenVendor"); // delete if don't want from home activity  //just add for minor fix in order confirmation
        restaurantNumber = getIntent().getIntExtra("vendor id",0);
        restaurantUrl = getIntent().getStringExtra("vendor url");

        orderStack.setVendorId(restaurantNumber);

        ImageView vendorPic = findViewById(R.id.vendorPic);
        bitmap = getBitmapFromURL(restaurantUrl);
        vendorPic.setImageBitmap(bitmap);
        searchBox = findViewById(R.id.searchBox);
        search_btn = findViewById(R.id.search_btn);
        no_result1 = findViewById(R.id.no_result1);
        no_result2 = findViewById(R.id.no_result2);
        search_icon = findViewById(R.id.search_icon);
        getMenuList();

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
        tappable_customize = (android.support.constraint.ConstraintLayout)findViewById(R.id.tappable_customize);
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
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_result1.setVisibility(View.INVISIBLE);
                no_result2.setVisibility(View.INVISIBLE);
                search_icon.setVisibility(View.INVISIBLE);
                searchKeyword = searchBox.getText().toString().trim();
                if(searchKeyword.isEmpty()) {
                    setResultListAdapter(availableList);
                } else {
                    resultList = new ArrayList<>();
                    if(currentCategory.equals("ALL")) {
                        for(int i = 0; i < availableList.size(); i++) {
                            String currentMenu = availableList.get(i).getFoodName().toLowerCase();
                            if((currentMenu.indexOf(searchKeyword) != -1)) {
                                resultList.add(availableList.get(i));
                            }
                            hideKeyboard(v);
                            setResultListAdapter(resultList);
                        }
                    } else {
                        for(int i = 0; i < availableList.size(); i++) {
                            String currentMenu = availableList.get(i).getFoodName().toLowerCase();
                            String currentCategory2 = availableList.get(i).getFoodCategory();
                            if((currentMenu.indexOf(searchKeyword) != -1) && (currentCategory.equals(currentCategory2))) {
                                resultList.add(availableList.get(i));
                            }
                            hideKeyboard(v);
                            setResultListAdapter(resultList);
                        }
                    }
                    if(resultList.size() == 0) {
                        hideKeyboard(v);
                        no_result1.setVisibility(View.VISIBLE);
                        no_result2.setVisibility(View.VISIBLE);
                        search_icon.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }
    protected ArrayList<food> shownFoodList;
    protected ArrayList<food> availableList;
    protected ArrayList<food> soldOutList;
    protected ListView menuList;
    protected ListAdapter testAdapter1;
    protected ListAdapter resultAdapter;

    private void addAlacarteToList(ArrayList<availableList> inputAvailableList, ArrayList<soldOutList> inputSoldOutList) {

////////  DEAL WITH A LA CARTE ////////////

        availableList = new ArrayList<>(); //need to get from BE
        soldOutList = new ArrayList<>();   //need to get from BE

        for(availableList list : inputAvailableList) {
            availableList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "A LA CARTE", list.getCategory()));
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
        pd.setMessage("Loading. PLease wait...");
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
//                System.out.println("Received Restaurant Name: "+menu.getVendorInfo().restaurantName);
//                System.out.println("Received Restaurant URL: "+menu.getVendorInfo().vendorImage);
                minCombinationPrice = findViewById(R.id.minCombinationPrice);
                minCombinationPrice.setText("Starting from "+ menu.getMinCombinationPrice() +" ฿");
                if(menu.getMinCombinationPrice()==0){
                    tappable_customize.setVisibility(View.GONE);
                }
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
                currentCategory = "ALL";
                resultList = availableList;
                for(int i = 1; i <= resultList.size()-1; i++) {
                    for(int j = 0; j <= resultList.size()-2; j++) {
                        if(resultList.get(j).getFoodPrice() > resultList.get(j+1).getFoodPrice()) {
                            food temp = resultList.get(j);
                            resultList.set(j, resultList.get(j+1));
                            resultList.set(j+1, temp);
                        }
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Sorted by Price", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_az:
                currentCategory = "ALL";
                resultList = availableList;
                for(int i = 1; i <= resultList.size()-1; i++) {
                    for(int j = 0; j <= resultList.size()-2; j++) {
                        if (resultList.get(j).getFoodName().compareTo(resultList.get(j+1).getFoodName()) > 0) {
                            food temp = resultList.get(j);
                            resultList.set(j, resultList.get(j+1));
                            resultList.set(j+1, temp);
                        }
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Sorted by Name", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_all:
                currentCategory = "ALL";
                no_result1.setVisibility(View.INVISIBLE);
                no_result2.setVisibility(View.INVISIBLE);
                search_icon.setVisibility(View.INVISIBLE);
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
                Toast.makeText(this, "All", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_meat:
                currentCategory = "MEAT";
                resultList = new ArrayList<>();
                for(int i = 0; i < availableList.size(); i++) {
                    if(String.valueOf(availableList.get(i).getFoodCategory()).equals("MEAT")) {
                        resultList.add(availableList.get(i));
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Meat Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_noodles:
                currentCategory = "NOODLES";
                resultList = new ArrayList<>();
                for(int i = 0; i < availableList.size(); i++) {
                    if(String.valueOf(availableList.get(i).getFoodCategory()).equals("NOODLES")) {
                        resultList.add(availableList.get(i));
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Noodles Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_soup:
                currentCategory = "SOUP";
                resultList = new ArrayList<>();
                for(int i = 0; i < availableList.size(); i++) {
                    if(String.valueOf(availableList.get(i).getFoodCategory()).equals("SOUP")) {
                        resultList.add(availableList.get(i));
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Soup Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_spicy:
                currentCategory = "SPICY";
                resultList = new ArrayList<>();
                for(int i = 0; i < availableList.size(); i++) {
                    if(String.valueOf(availableList.get(i).getFoodCategory()).equals("SPICY")) {
                        resultList.add(availableList.get(i));
                    }
                }
                setResultListAdapter(resultList);
                Toast.makeText(this, "Spicy Category", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.category_vegan:
                currentCategory = "VEGAN";
                resultList = new ArrayList<>();
                for(int i = 0; i < availableList.size(); i++) {
                    if(String.valueOf(availableList.get(i).getFoodCategory()).equals("VEGAN")) {
                        resultList.add(availableList.get(i));
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
        no_result1.setVisibility(View.INVISIBLE);
        no_result2.setVisibility(View.INVISIBLE);
        search_icon.setVisibility(View.INVISIBLE);
        resultAdapter = new menuListAdapterv2(this,resultList,resultList.size());
        menuList.setAdapter(resultAdapter);
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
    protected void hideKeyboard(View view)    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
