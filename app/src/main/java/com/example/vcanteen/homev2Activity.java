package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.availablePaymentMethod;
import com.example.vcanteen.POJO.currentDensityAll;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vcanteen.Data.Customers;
import com.example.vcanteen.POJO.customerHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homev2Activity extends AppCompatActivity {
    private Button recommendationButton;
    private ImageView recommendationCardImage;
    private TextView recommendationMenu;
    private Button vendorsButton;
    private Button crowdButton;
    private Button ordersButton;
    private RelativeLayout settingsLayout;
    private RelativeLayout profileLayout;

    TextView firstAndLastName;
    ImageView profilePictureButton;
    TextView crowdEstimationOnPicValue;
    customerSingleton customerSingleton;
    TextView crowdEstimationOnPicValue;

    ProgressDialog progressDialog;

    Bitmap bitmap;
    Bitmap bitmap2;

    static SharedPreferences sharedPref;
    RequestOptions option = new RequestOptions().centerCrop();

    int customerId;

    public int randomizedVendorId;
    public String randomizedVendorImage;
    public String randomizedRestaurantName;
    public orderStack orderStack;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homev2);

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

        recommendationButton = findViewById(R.id.recommedationButton);
        recommendationCardImage = findViewById(R.id.recommendationCardImage);
        recommendationMenu = findViewById(R.id.recommendationMenu);
        vendorsButton = findViewById(R.id.vendorsButton);
        crowdButton = findViewById(R.id.crowdButton);
        ordersButton = findViewById(R.id.ordersButton);
        settingsLayout = findViewById(R.id.settingsLayout);
        profileLayout = findViewById(R.id.profileLayout);
        firstAndLastName = findViewById(R.id.firstAndLastName);
        profilePictureButton = findViewById(R.id.profilePictureButton);
        crowdEstimationOnPicValue = findViewById(R.id.crowdEstimationOnPicValue);

        customerSingleton = com.example.vcanteen.customerSingleton.getInstance();

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        customerId = sharedPref.getInt("customerId",0);

        customerSingleton = com.example.vcanteen.customerSingleton.getInstance();

        System.out.println("onCreate Homepage");
        progressDialog = new ProgressDialog(homev2Activity.this);

        callRetrofitOnHomepage(customerId);

        recommendationButton.setOnClickListener(v -> {
            //TODO navigate to vendor_menu_page of the vendor who owns the menu
        });

                     //get result here
                     customerHome info = response.body();
                     firstAndLastName.setText(info.getCustomerInfo().getFirstnamev2()+" "+ info.getCustomerInfo().getLastnamev2().substring(0,1)+".");

                     Glide.with(getApplicationContext()).load(info.getCustomerInfo().getCustomerImagev2()).apply(option).into(profilePictureButton);

                     randomizedVendorId = info.getRecommendationInfo().getRecVendorId();
                     randomizedVendorImage = info.getRecommendationInfo().getRecVendorImage();
                     randomizedRestaurantName = info.getRecommendationInfo().getRecRestaurantName();

                     Glide.with(getApplicationContext()).load(info.getRecommendationInfo().getRecFoodImage()).apply(option).into(recommendationCardImage);
                     recommendationMenu.setText(info.getRecommendationInfo().getRecFoodName());
                     crowdEstimationOnPicValue.setText(info.getPercentDensity()+"%");

                     customerSingleton.setFirstname(info.getCustomerInfo().getFirstnamev2());
                     customerSingleton.setLastname(info.getCustomerInfo().getLastnamev2());
                     customerSingleton.setEmail(info.getCustomerInfo().getEmail());
                     customerSingleton.setCustomerImage(info.getCustomerInfo().getCustomerImagev2());

                     crowdEstimationOnPicValue.setText(info.getPercentDensity()+"%");
                 }

                 @Override
                 public void onFailure(Call<customerHome> call, Throwable t) {
                     System.out.println("Entered Home Fail.....");

                 }
            });

            recommendationButton.setOnClickListener(v -> {
                //TODO navigate to vendor_menu_page of the vendor who owns the menu
                Intent i = new Intent(homev2Activity.this, vendorMenuv2Activity.class);
                String chosenVendor = randomizedRestaurantName;
                String vendorUrl = randomizedVendorImage;
                orderStack.setCustomerId(sharedPref.getInt("customerId", 0));
                orderStack.setVendorId(randomizedVendorId);
                i.putExtra("vendor id", randomizedVendorId);
                i.putExtra("vendor url", vendorUrl);
                i.putExtra("chosenVendor", chosenVendor);
                startActivity(i);
            });

        vendorsButton.setOnClickListener(v -> {
            //TODO create retrofit here
            //TODO spinner
            startActivity(new Intent(homev2Activity.this, vendorListActivity.class));
        });

        crowdButton.setOnClickListener(v -> {
            //TODO create retrofit here
            //TODO spinner
            Intent i = new Intent(homev2Activity.this, crowdEstimationActivity.class);
            preloadCrowdPage(i);

        });

        ordersButton.setOnClickListener(v -> {
            //TODO create retrofit here
            //TODO spinner
            startActivity(new Intent(homev2Activity.this, OrderListActivity.class));
        });

        profileLayout.setOnClickListener(v -> {
            //TODO create retrofit here
            //TODO spinner
            System.out.println("pressed profile");
            startActivity(new Intent(homev2Activity.this, profilePageActivity.class));
        });

        settingsLayout.setOnClickListener(v -> {
            System.out.println("pressed settings");
            startActivity(new Intent(homev2Activity.this, settingActivity.class));
        });


    }

    private void callRetrofitOnHomepage(int customerId) {
        progressDialog = ProgressDialog.show(homev2Activity.this
                , "",
                "Loading. Please wait...", true);
        System.out.println("customerId : "+customerId);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<customerHome> call = jsonPlaceHolderApi.getCustomerHome(customerId);
        call.enqueue(new Callback<customerHome>() {
            @Override
            public void onResponse(Call<customerHome> call, Response<customerHome> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(homev2Activity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                System.out.println("200 Homepage");

                //get result here
                customerHome info = response.body();
                firstAndLastName.setText(info.getCustomerInfo().getFirstname()+" "+ info.getCustomerInfo().getLastname().substring(0,1)+".");

//                     if(info.getCustomerInfo().getCustomerImage().equals(null)) {
//                         info.getCustomerInfo().setCustomerImage("https://firebasestorage.googleapis.com/v0/b/vcanteen-d8ede.appspot.com/o/default%20user%20icon.png?alt=media&token=07b2a90c-2404-4b72-9357-ed40501300b7");
//                     }
                bitmap = getBitmapFromURL(info.getCustomerInfo().getCustomerImage());
                profilePictureButton.setImageBitmap(bitmap);

                customerSingleton.setFirstname(info.getCustomerInfo().getFirstname());
                customerSingleton.setLastname(info.getCustomerInfo().getLastname());
                customerSingleton.setEmail(info.getCustomerInfo().getEmail());
                customerSingleton.setCustomerImage(info.getCustomerInfo().getCustomerImage());

                crowdEstimationOnPicValue.setText(info.getPercentDensity()+"%");
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<customerHome> call, Throwable t) {
                System.out.println("Entered Home Fail.....");

            }
        });
    }

    private void preloadCrowdPage(Intent i) {
        progressDialog = ProgressDialog.show(homev2Activity.this
                , "",
                "Loading. Please wait...", true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<currentDensityAll> call =  jsonPlaceHolderApi.getCurrentDensityAll();

        call.enqueue(new Callback<currentDensityAll>() {
            @Override
            public void onResponse(Call<currentDensityAll> call, Response<currentDensityAll> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(homev2Activity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                currentDensityAll preloadCrowdData = response.body();
                System.out.println("preloaded");
                i.putExtra("preloadCrowdData",preloadCrowdData );
                i.putExtra("hourlyData", preloadCrowdData.hourlyCrowdStat);

//                i.putExtra("8",preloadCrowdData.hourlyCrowdStat.get(0).getCrowdStat());
//                i.putExtra("9",preloadCrowdData.hourlyCrowdStat.get(1).getCrowdStat());
//                i.putExtra("10",preloadCrowdData.hourlyCrowdStat.get(2).getCrowdStat());
//                i.putExtra("11",preloadCrowdData.hourlyCrowdStat.get(3).getCrowdStat());
//                i.putExtra("12",preloadCrowdData.hourlyCrowdStat.get(4).getCrowdStat());
//                i.putExtra("13",preloadCrowdData.hourlyCrowdStat.get(5).getCrowdStat());
//                i.putExtra("14",preloadCrowdData.hourlyCrowdStat.get(6).getCrowdStat());
//                i.putExtra("15",preloadCrowdData.hourlyCrowdStat.get(7).getCrowdStat());
//                i.putExtra("16",preloadCrowdData.hourlyCrowdStat.get(8).getCrowdStat());

                System.out.println("percent1 "+preloadCrowdData.getPercentDensity());
                System.out.println("put preloadCrowdData into intent");
                progressDialog.dismiss();
                startActivity(i);

            }

            @Override
            public void onFailure(Call<currentDensityAll> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void saveToken(String token) {
        String email = mAuth.getCurrentUser().getEmail();
        orderStack = com.example.vcanteen.orderStack.getInstance();
        orderStack.setCustomerId(sharedPref.getInt("customerId",0));
        System.out.println("firebase: "+email);
        Customers customer = new Customers(email, null, null, "CUSTOMER", null, null, token);

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

        dbUsers.child(mAuth.getCurrentUser().getUid()).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("TOKEN SAVED - AUTO LOGIN");

                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callRetrofitOnHomepage(customerId);
    }
}
