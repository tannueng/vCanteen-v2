package com.example.vcanteen;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.Data.LoginResponse;
import com.example.vcanteen.POJO.currentDensity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class firstTimeLinkPaymentActivity extends AppCompatActivity {

    Spinner serviceProviderSpinner;
    EditText accountNumberField;
    String serviceProvider, accountNumber, firebaseToken;
    Button linkButton;
    TextView inline;
    String cachedEmail, cachedPassword, cachedFirstName, cachedLastName, cachedCustomerImage, cachedAccountType;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_link_payment);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        accountNumberField = findViewById(R.id.accountNumberBox);
        linkButton = findViewById(R.id.linkButton);
        inline = findViewById(R.id.inline);

        serviceProviderSpinner = findViewById(R.id.paymentSpinner);
        serviceProviderSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        cachedEmail = getIntent().getStringExtra("cachedEmail");
        cachedPassword = getIntent().getStringExtra("cachedPassword");
        cachedFirstName = getIntent().getStringExtra("cachedFirstName");
        cachedLastName = getIntent().getStringExtra("cachedLastName");

//        String.valueOf(serviceProviderSpinner.getSelectedItem()).toUpperCase();
        System.out.println(String.valueOf(serviceProviderSpinner.getSelectedItem()));

        linkButton.setOnClickListener(v -> {
            accountNumber = accountNumberField.getText().toString();
            if(accountNumber.isEmpty()) {
                inline.setText("Please fill in your account number.");
                return;
            } else {
                serviceProvider = renameServiceProvider(String.valueOf(serviceProviderSpinner.getSelectedItem()));
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://vcanteen.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<LoginResponse> call = jsonPlaceHolderApi.postNewCustomer(cachedEmail,cachedPassword,cachedFirstName,cachedLastName,cachedCustomerImage,cachedAccountType,serviceProvider,accountNumber,firebaseToken);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(firstTimeLinkPaymentActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
                            return;
                        }
                        sharedPref.edit().putString("token", response.body().getCustomerSessionToken()).commit();
                        sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
                        sharedPref.edit().putString("email", cachedEmail).commit();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
            }
        });

    }

    private String renameServiceProvider(String serviceProvider) {
        if(serviceProvider.equals("CU NEX")) {
            return "CU_NEX";
        } else if (serviceProvider.equals("SCB EASY")) {
            return "SCB_EASY";
        } else if (serviceProvider.equals("K PLUS")) {
            return "K_PLUS";
        } else if (serviceProvider.equals("TrueMoney Wallet")) {
            return "TRUEMONEY_WALLET";
        }
        return null;
    }
}
