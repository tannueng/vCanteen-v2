package com.example.vcanteen;

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

import com.example.vcanteen.POJO.customerHome;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    customerSingleton customerSingleton;

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

        getSharedPreferences("myPref", MODE_PRIVATE);
        customerId = sharedPref.getInt("customerId",0);

        customerSingleton = com.example.vcanteen.customerSingleton.getInstance();



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

                     //get result here
                     customerHome info = response.body();
                     firstAndLastName.setText(info.getCustomerInfo().getFirstname()+" "+ info.getCustomerInfo().getLastname().substring(0,1)+".");

                     bitmap = getBitmapFromURL(info.getCustomerInfo().getCustomerImage());
                     profilePictureButton.setImageBitmap(bitmap);

                     customerSingleton.setFirstname(info.getCustomerInfo().getFirstname());
                     customerSingleton.setLastname(info.getCustomerInfo().getLastname());
                     customerSingleton.setEmail(info.getCustomerInfo().getEmail());
                     customerSingleton.setCustomerImage(info.getCustomerInfo().getCustomerImage());
                 }

                 @Override
                 public void onFailure(Call<customerHome> call, Throwable t) {
                     System.out.println("Entered Home Fail.....");

                 }
            });


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
            startActivity(new Intent(homev2Activity.this, crowdEstimationActivity.class));
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
}
