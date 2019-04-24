package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    String serviceProvider, accountNumber;
    Button linkButton;
    TextView inline;
    String cachedEmail, cachedPassword, cachedFirstName, cachedLastName, cachedCustomerImage, cachedAccountType,cachedFirebaseToken;
    SharedPreferences sharedPref;
    ProgressDialog progressDialog;

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
        cachedAccountType = getIntent().getStringExtra("cachedAccountType");
        cachedFirebaseToken = getIntent().getStringExtra("cachedFirebaseToken");

//        String.valueOf(serviceProviderSpinner.getSelectedItem()).toUpperCase();
//        System.out.println(String.valueOf(serviceProviderSpinner.getSelectedItem()));

        linkButton.setOnClickListener(v -> {
            accountNumber = accountNumberField.getText().toString();
            if(accountNumber.isEmpty()) {
                inline.setText("Please fill in your account number.");
                inline.setVisibility(View.VISIBLE);
                return;
            } else {

                System.out.println("account number not null");
                inline.setVisibility(View.INVISIBLE);
                serviceProvider = renameServiceProvider(String.valueOf(serviceProviderSpinner.getSelectedItem()));

                System.out.println(" ==================== sendEmailToSignUp :: "+cachedEmail+" ==================== ");
                System.out.println(" ==================== Firebasetoken :: "+cachedFirebaseToken+" ==================== ");
                progressDialog = new ProgressDialog(firstTimeLinkPaymentActivity.this);
                progressDialog = ProgressDialog.show(firstTimeLinkPaymentActivity.this, "",
                        "Verifying. Please wait...", true);

                System.out.println("POST:"+cachedEmail);
                System.out.println("POST:"+cachedPassword);
                System.out.println("POST:"+cachedFirstName);
                System.out.println("POST:"+cachedLastName);
                System.out.println("POST:"+cachedCustomerImage);
                System.out.println("POST:"+cachedAccountType);
                System.out.println("POST:"+serviceProvider);
                System.out.println("POST:"+accountNumber);
                System.out.println("POST:"+cachedFirebaseToken);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://vcanteen.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<LoginResponse> call = jsonPlaceHolderApi.postNewCustomer(cachedEmail,cachedPassword,cachedFirstName,cachedLastName,cachedCustomerImage,cachedAccountType,serviceProvider,accountNumber,cachedFirebaseToken);
//                Call<LoginResponse> call = jsonPlaceHolderApi.signUpNewVendor(email,password,acccountType,serviceProvider,accountNumber,FireBaseToken,vendorName,vendorPhoneNumber,fourDigitPin);

                // PUT DATA FOR Email VERIFICATION
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                        if(response.code() != 200 ){
                            // SERVER ERROR
                            if (cachedAccountType.equals("FACEBOOK"))  sharedPref.edit().putString("token", "NO TOKEN JA EDOK").commit();

                            Toast.makeText(getApplicationContext(), "Oops. Something went wrong :( , please try again later...", Toast.LENGTH_SHORT).show();
                            System.out.println(" ==================== Error Code :: "+response.code()+" ==================== ");
                            progressDialog.dismiss();

                        }else {
                            // Code 200 = FOUND IN DB = receive Account Type
                            System.out.println(" ==================== Result Code :: "+response.code()+" ==================== ");

                            int customerId = response.body().getCustomerId();
                            String customerSessionToken = response.body().getCustomerSessionToken();

                            System.out.println(" ==================== Response SignUp customerId:: "+customerId+" ======================= ");
                            System.out.println(" ==================== Response SignUp SessionToken :: "+customerSessionToken+" ======================= ");

                            sharedPref.edit().putString("customerSessionToken", response.body().getCustomerSessionToken()).commit();
                            sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
                            sharedPref.edit().putString("email", cachedEmail).commit();
                            progressDialog.dismiss();

                            //TODO callhomepage here

                            startActivity(new Intent(firstTimeLinkPaymentActivity.this,homev2Activity.class));

                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        System.out.println("\n\n\n\n********************"+ t.getMessage() +"********************\n\n\n\n");

                    }
                });

                ////////////MY retrofit/////
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://vcanteen.herokuapp.com/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//                System.out.println("POST:"+cachedEmail);
//                System.out.println("POST:"+cachedPassword);
//                System.out.println("POST:"+cachedFirstName);
//                System.out.println("POST:"+cachedLastName);
//                System.out.println("POST:"+cachedCustomerImage);
//                System.out.println("POST:"+cachedAccountType);
//                System.out.println("POST:"+serviceProvider);
//                System.out.println("POST:"+accountNumber);
//                System.out.println("POST:"+cachedFirebaseToken);
//                Call<LoginResponse> call = jsonPlaceHolderApi.postNewCustomer(cachedEmail,cachedPassword,cachedFirstName,cachedLastName,cachedCustomerImage,cachedAccountType,serviceProvider,accountNumber,cachedFirebaseToken);
//
//                call.enqueue(new Callback<LoginResponse>() {
//                    @Override
//                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                        if (!response.isSuccessful()) {
//                            Toast.makeText(firstTimeLinkPaymentActivity.this, "CODE: "+response.code(),Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        sharedPref.edit().putString("token", response.body().getCustomerSessionToken()).commit();
//                        sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
//                        sharedPref.edit().putString("email", cachedEmail).commit();
//                        sendToSignUp(cachedFirebaseToken, cachedAccountType);
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginResponse> call, Throwable t) {
//
//                    }
//                });
            }
        });

        findViewById(R.id.relativeLayout).setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            return true;
        });

    }

    private void sendToSignUp(String FireBaseToken, String accountType) {



    }

    private String renameServiceProvider (String serviceProvider) {
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
