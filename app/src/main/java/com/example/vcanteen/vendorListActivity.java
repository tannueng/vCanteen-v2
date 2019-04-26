package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.Data.Customers;
import com.example.vcanteen.POJO.vendorListObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class vendorListActivity extends AppCompatActivity {

    private List<vendorList> vendorLists;
    private TextView mTextMessage;
    private ListView listView;
    private ProgressDialog progressDialog;

//    FloatingActionButton profilebtn;
//    FloatingActionButton ordersbtn;
//    FloatingActionButton settingsbtn;
    orderStack orderStack;

    private SharedPreferences sharedPref;
    private FirebaseAuth mAuth;
    vendorAlacarteMenu menuVendorAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);

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
//        profilebtn = findViewById(R.id.buttonprofile);
//        ordersbtn = findViewById(R.id.buttonorders);
//        settingsbtn = findViewById(R.id.buttonsettings);

        //orderStack.setEmpty();
        //orderStack = new orderStack(orderStack.getCustomerId(),orderStack.getVendorId(),orderStack.getOrderList(),orderStack.getTotalPrice(),orderStack.getCustomerMoneyAccount());


//        profilebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(vendorListActivity.this, profile.class);
//                startActivity(intent);
//            }
//        });

//       ordersbtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(vendorListActivity.this, OrderListActivity.class);
////                startActivity(intent);
////            }
////        });
////
////        settingsbtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(vendorListActivity.this, settingActivity.class);
////                startActivity(intent);
////            }
////        });

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

    private void getVendorList() {
        ProgressDialog pd = new ProgressDialog(vendorListActivity.this);
        pd.setMessage("Loading. Please wait...");
        pd.show();
        String url = "https://vcanteen.herokuapp.com/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

//        Call<List<vendorList>> call = jsonPlaceHolderApi.getVendorList();

        Call<vendorListObject> call = jsonPlaceHolderApi.getVendorList();
        call.enqueue(new Callback<vendorListObject>() {
            @Override
            public void onResponse(Call<vendorListObject> call, Response<vendorListObject> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"cannot connect error code: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                vendorListObject vendorLists = response.body();
//                Log.d("TEST", String.valueOf(vendorLists.size()));
                final ArrayList<vendorList> temp = vendorLists.getVendorList();
                //temp.add(new vendorList(vendorLists));
//                temp = vendorLists.getVendorList();
//                for(int i =0; i<vendorLists.size();i++){
//
//                    int vendorId = vendorLists.get(i).getVendorId();
//                    String vendorName = vendorLists.get(i).getRestaurantName();
//                    //int vendorNumber = vendorLists.get(i).getRestaurantNumber();
//                    String vendorImageURL = vendorLists.get(i).getVendorImage();
//                    String vendorStatus = vendorLists.get(i).getVendorStatus();
//                    int queuingTime = vendorLists.get(i).getQueuingTime();
//
//                    //vendorList newVendorList = new vendorList(vendorId,vendorName,vendorNumber,vendorImageURL,vendorStatus,queuingTime);
//                    vendorList newVendorList = new vendorList(vendorId,vendorName,vendorImageURL,vendorStatus,queuingTime);
//                    temp.add(newVendorList);
//
//                }
                vendorListAdapterv2 adapter = new vendorListAdapterv2(vendorListActivity.this,temp);

                listView.setAdapter(adapter);
                pd.dismiss();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                        /*ProgressDialog pd = new ProgressDialog(vendorListActivity.this);
                        pd.setMessage("loading");
                        pd.show();*/
                        Object clickItemObj = adapter.getAdapter().getItem(position);
                        int vendornumber = temp.get(position).getVendorId();
                        String chosenVendor = temp.get(position).getRestaurantName();
                        String vendorUrl = temp.get(position).getVendorImage();
                        String vendorStatus;
                        vendorList item = temp.get(position);
                        vendorStatus = item.getVendorStatus();
                        //int queuingTime = item.getQueuingTime();
                        if (vendorStatus.equals("CLOSED")) {
                            view.setClickable(false);
                            //Toast.makeText(vendorListActivity.this, "This restaurant is closed.", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent i = new Intent(vendorListActivity.this, vendorMenuv2Activity.class);
                            i.putExtra("vendor id", vendornumber);
                            orderStack.setVendorId(vendornumber);
                            orderStack.setCustomerId(sharedPref.getInt("customerId", 0));
                            i.putExtra("vendor url", vendorUrl);
                            i.putExtra("chosenVendor", chosenVendor);
                            /*Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://vcanteen.herokuapp.com/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                            Call<vendorAlacarteMenu> call = jsonPlaceHolderApi.getVendorAlacarte(vendornumber);
                            call.enqueue(new Callback<vendorAlacarteMenu>() {
                                @Override
                                public void onResponse(Call<vendorAlacarteMenu> call, Response<vendorAlacarteMenu> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(vendorListActivity.this, "CODE: " + response.code(), Toast.LENGTH_LONG).show();
                                        return;

                                    }
                                    vendorAlacarteMenu menu = response.body();
                                    i.putExtra("vendorMenu", menu);
                                    //i.putExtra("vendorMenuAvailable", menu.availableList);
                                    i.putExtra("minCombinationPrice", menu.getMinCombinationPrice());
                                    //pd.dismiss();*/
                                    startActivity(i);

                                /*}
                                /*@Override
                                public void onFailure(Call<vendorAlacarteMenu> call, Throwable t) {
                                    System.out.println("Entered Menu Fail.....");

                                }
                                         });*/



                        }
                    }



                });


                            /*On the second activity:
                            Bundle bundle = getIntent().getExtras();
                            int value = bundle.getInt("vendor id");
                            */

                        }

                    /*}
            @Override
            public void onFailure(Call<vendorAlacarteMenu> call, Throwable t) {
                System.out.println("Entered Menu Fail.....");

            }


                });


            }*/

            @Override
            public void onFailure(Call<vendorListObject> call, Throwable t) {
                System.out.println("Didnt ENTER");
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
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
//                    Toast.makeText(vendorListActivity.this, "Token Saved", Toast.LENGTH_LONG).show();
                    System.out.println("TOKEN SAVED - AUTO LOGIN");

                }
            }
        });

    }

    public void onResume() {
        super.onResume();
        getVendorList();
    }

    private void addAlacarteToList(ArrayList<availableList> inputList) {
        final ArrayList<food> availableList = new ArrayList<>();
        ArrayList<food> soldOutList = new ArrayList<>();
        for (availableList list : inputList) {
            availableList.add(new food(list.getFoodId(), list.getFoodName(), list.getFoodPrice(), "A LA CARTE", list.getCategory()));
        }
        final ArrayList<food> shownFoodList = new ArrayList<>(availableList);
        shownFoodList.addAll(soldOutList);
    }
}
