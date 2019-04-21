package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.vcanteen.POJO.customerHome;

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
    private Button vendorsButton;
    private Button crowdButton;
    private Button ordersButton;
    private RelativeLayout settingsLayout;
    private RelativeLayout profileLayout;

    TextView firstAndLastName;
    ImageView profilePictureButton;
    TextView crowdEstimationOnPicValue;
    customerSingleton customerSingleton;

    ProgressDialog progressDialog;

    Bitmap bitmap;

    static SharedPreferences sharedPref;

    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homev2);

        recommendationButton = findViewById(R.id.recommedationButton);
        vendorsButton = findViewById(R.id.vendorsButton);
        crowdButton = findViewById(R.id.crowdButton);
        ordersButton = findViewById(R.id.ordersButton);
        settingsLayout = findViewById(R.id.settingsLayout);
        profileLayout = findViewById(R.id.profileLayout);
        firstAndLastName = findViewById(R.id.firstAndLastName);
        profilePictureButton = findViewById(R.id.profilePictureButton);
        crowdEstimationOnPicValue = findViewById(R.id.crowdEstimationOnPicValue);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        customerId = sharedPref.getInt("customerId",0);

        customerSingleton = com.example.vcanteen.customerSingleton.getInstance();

        System.out.println("onCreate Homepage");
        progressDialog = new ProgressDialog(homev2Activity.this);

        callRetrofitOnHomepage(customerId);

        recommendationButton.setOnClickListener(v -> {
            //TODO navigate to vendor_menu_page of the vendor who owns the menu
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

    public Bitmap getBitmapFromURL(String src){
        try{
            System.out.println("check1 : "+src);
                if(src == null) {
                    src = "http://firebasestorage.googleapis.com/v0/b/vcanteen-d8ede.appspot.com/o/default%20user%20icon.png?alt=media&token=07b2a90c-2404-4b72-9357-ed40501300b7";
                }
            System.out.println("check2 : "+src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e){
            System.out.println("null image url..");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callRetrofitOnHomepage(customerId);
    }
}
