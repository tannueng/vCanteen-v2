package com.example.vcanteen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.Data.Customers;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.facebook.internal.LockOnGetVariable;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class homev1Activity extends AppCompatActivity {

    private List<vendorList> vendorLists;
    private TextView mTextMessage;
    private ListView listView;

    FloatingActionButton profilebtn;
    FloatingActionButton ordersbtn;
    FloatingActionButton settingsbtn;
    orderStack orderStack;

    private SharedPreferences sharedPref;
    private FirebaseAuth mAuth;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_profile:
//                    return true;
//                case R.id.navigation_orders:
////                    mTextMessage.setText("ORDERS");
//                    startActivity(new Intent(homev1Activity.this, OrderListActivity.class));
//                    return true;
//                case R.id.navigation_settings:
////                    mTextMessage.setText("SETTINGS");
//                    startActivity(new Intent(homev1Activity.this, settingActivity.class));
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v1);

        mAuth = FirebaseAuth.getInstance();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            System.out.println(token);
                            saveToken(token);
                        } else {

                        }
                    }
                });

        mTextMessage = (TextView) findViewById(R.id.message);
        listView = findViewById(R.id.vendorlist);
        profilebtn = findViewById(R.id.buttonprofile);
        ordersbtn = findViewById(R.id.buttonorders);
        settingsbtn = findViewById(R.id.buttonsettings);

        //orderStack.setEmpty();
        //orderStack = new orderStack(orderStack.getCustomerId(),orderStack.getVendorId(),orderStack.getOrderList(),orderStack.getTotalPrice(),orderStack.getCustomerMoneyAccount());


//        profilebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(homev1Activity.this, profile.class);
//                startActivity(intent);
//            }
//        });

       ordersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homev1Activity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });

        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homev1Activity.this, settingActivity.class);
                startActivity(intent);
            }
        });

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        System.out.println(sharedPref.getString("token", "empty token"));
        System.out.println(sharedPref.getString("email", "empty email"));
        System.out.println(sharedPref.getInt("customerId", 0));
        System.out.println("sharedPref Set.");

//        final String[] test = {"ESAN food","Fried Chicken with Sticky Rice","Food3","Food4","Fried Chicken with Sticky RiceFried Chicken with Sticky RiceFried Chicken with Sticky RiceFried Chicken with Sticky Rice","Food6", "Food 77"};
//        ListAdapter testAdapter = new vendorListAdapter(this, test);
//        ListView vendorList = findViewById(R.id.vendorlist);
//        vendorList.setAdapter(testAdapter);
        getVendorList();

    }

    private void getVendorList(){

        String url = "https://vcanteen.herokuapp.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        vendorListApi jsonPlaceHolderApi = retrofit.create(vendorListApi.class);




        Call<List<vendorList>> call = jsonPlaceHolderApi.getVendorList();

        call.enqueue(new Callback<List<vendorList>>() {
            @Override
            public void onResponse(Call<List<vendorList>> call, Response<List<vendorList>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"cannot connect error code: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }

                List<vendorList> vendorLists = response.body();
                Log.d("TEST", String.valueOf(vendorLists.size()));
                ArrayList<vendorList> temp = new ArrayList<vendorList>();
                for(int i =0; i<vendorLists.size();i++){

                    int vendorId = vendorLists.get(i).getVendorId();
                    String vendorName = vendorLists.get(i).getRestaurantName();
                    int vendorNumber = vendorLists.get(i).getRestaurantNumber();
                    String vendorImageURL = vendorLists.get(i).getVendorImage();
                    String vendorStatus = vendorLists.get(i).getVendorStatus();

                    vendorList newVendorList = new vendorList(vendorId,vendorName,vendorNumber,vendorImageURL,vendorStatus);
                    temp.add(newVendorList);

                }
                vendorListAdapter adapter = new vendorListAdapter(homev1Activity.this,temp);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                        Object clickItemObj = adapter.getAdapter().getItem(position);
                        int vendornumber = vendorLists.get(position).getVendorId();
                        String chosenVendor = vendorLists.get(position).getRestaurantName();
                        String vendorUrl = vendorLists.get(position).getVendorImage();
                        String vendorStatus;
                        vendorList item = vendorLists.get(position);
                        vendorStatus = item.getVendorStatus();
                        if(vendorStatus.equals("CLOSED")){
                            view.setClickable(false);
                            //Toast.makeText(homev1Activity.this, "This restaurant is closed.", Toast.LENGTH_SHORT).show();
                        } else{
                            Intent i = new Intent(homev1Activity.this, vendorMenuActivity.class);
                            i.putExtra("vendor id", vendornumber);
                            orderStack.setVendorId(vendornumber);
                            orderStack.setCustomerId(sharedPref.getInt("customerId",0));
                            System.out.println("added vendor id in intent/singleton: "+vendornumber);
                            System.out.println("customer id in orderstack: "+orderStack.getCustomerId());
                            i.putExtra("vendor url", vendorUrl);
                            i.putExtra("chosenVendor",chosenVendor);
                            startActivity(i);
                            /*On the second activity:
                            Bundle bundle = getIntent().getExtras();
                            int value = bundle.getInt("vendor id");
                            */

                        }

                    }



                });


            }

            @Override
            public void onFailure(Call<List<vendorList>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        System.out.println("entered savetoken");
        String email = mAuth.getCurrentUser().getEmail();
        orderStack = com.example.vcanteen.orderStack.getInstance();
        orderStack.setCustomerId(sharedPref.getInt("customerId",0));
        System.out.println("firebase: "+email);
        Customers customer = new Customers(email, null, null, "CUSTOMER", null, null, token);

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(homev1Activity.this, "Token Saved", Toast.LENGTH_LONG).show();
                    System.out.println("TOKEN SAVED - AUTO LOGIN");

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
