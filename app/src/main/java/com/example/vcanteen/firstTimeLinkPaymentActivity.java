package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import com.example.vcanteen.Data.User;
import com.example.vcanteen.POJO.currentDensity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
    String cachedEmail, cachedPassword, cachedFirstName, cachedLastName, cachedCustomerImage, cachedAccountType;
    SharedPreferences sharedPref;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String firebaseToken;
    int customerId;
    String customerSessionToken;

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
        cachedCustomerImage = getIntent().getStringExtra("cachedCustomerImage");
        cachedAccountType = getIntent().getStringExtra("cachedAccountType");
        firebaseToken = getIntent().getStringExtra("cachedFirebaseToken");

        mAuth = FirebaseAuth.getInstance();
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

                registerFireBase();


//                System.out.println(" ==================== sendEmailToSignUp :: "+cachedEmail+" ==================== ");
//                System.out.println(" ==================== Firebasetoken :: "+cachedFirebaseToken+" ==================== ");
//                progressDialog = new ProgressDialog(firstTimeLinkPaymentActivity.this);
//                progressDialog = ProgressDialog.show(firstTimeLinkPaymentActivity.this, "",
//                        "Verifying. Please wait...", true);



//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://vcanteen.herokuapp.com/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//                Call<LoginResponse> call = jsonPlaceHolderApi.postNewCustomer(cachedEmail,cachedPassword,cachedFirstName,cachedLastName,cachedCustomerImage,cachedAccountType,serviceProvider,accountNumber,cachedFirebaseToken);
//
//                // PUT DATA FOR Email VERIFICATION
//                call.enqueue(new Callback<LoginResponse>() {
//                    @Override
//                    public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
//                        if(response.code() != 200 ){
//                            // SERVER ERROR
//                            if (cachedAccountType.equals("FACEBOOK"))  sharedPref.edit().putString("token", "NO TOKEN").commit();
//
//                            Toast.makeText(getApplicationContext(), "Oops. Something went wrong :( , please try again later...", Toast.LENGTH_SHORT).show();
//                            System.out.println(" ==================== Error Code :: "+response.code()+" ==================== ");
//                            progressDialog.dismiss();
//
//                        }else {
//                            // Code 200 = FOUND IN DB = receive Account Type
//                            System.out.println(" ==================== Result Code :: "+response.code()+" ==================== ");
//
//                            int customerId = response.body().getCustomerId();
//                            String customerSessionToken = response.body().getCustomerSessionToken();
//
//                            System.out.println(" ==================== Response SignUp customerId:: "+customerId+" ======================= ");
//                            System.out.println(" ==================== Response SignUp SessionToken :: "+customerSessionToken+" ======================= ");
//
//                            sharedPref.edit().putString("customerSessionToken", response.body().getCustomerSessionToken()).commit();
//                            sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
//                            System.out.println("before put in sharedPref : "+cachedEmail);
//                            sharedPref.edit().putString("email", cachedEmail).commit();
//                            sharedPref.edit().putString("account_type", cachedAccountType).commit();
//                            progressDialog.dismiss();
//
//                            //TODO callhomepage here
//
//                            startActivity(new Intent(firstTimeLinkPaymentActivity.this,homev2Activity.class));
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginResponse> call, Throwable t) {
//                        System.out.println("\n\n\n\n********************"+ t.getMessage() +"********************\n\n\n\n");
//
//                    }
//                });

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
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        });

    }

    private void registerFireBase(){

        progressDialog = new ProgressDialog(firstTimeLinkPaymentActivity.this);
        progressDialog = ProgressDialog.show(firstTimeLinkPaymentActivity.this, "",
                "Loading. Please wait...", true);


        System.out.println("================== mAuth Start -- signInWithEmailAndPassword(email, passwd) ==================");

        mAuth.createUserWithEmailAndPassword(cachedEmail, cachedPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            registerFireBase2("NORMAL");

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                progressDialog.dismiss();
//                                Toast.makeText(firstTimeLinkPaymentActivity.this, "Email exists in Firebase", Toast.LENGTH_LONG).show();
                                System.out.println("============== Firebase Collision because acctype is "+cachedAccountType+"================");
                                if (cachedAccountType.equals("FACEBOOK")){
                                    sendToSignUp(firebaseToken,"FACEBOOK");
                                }
                            } else {
                                progressDialog.dismiss();
                                //Toast.makeText(FourDigitPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                System.out.println("============== Firebase Check Exist Error "+task.getException().getMessage()+" ================");
                            }
                        }

                    }
                });
    }

    private void registerFireBase2(final String acccountType) {
        progressDialog = new ProgressDialog(firstTimeLinkPaymentActivity.this);
        progressDialog = ProgressDialog.show(firstTimeLinkPaymentActivity.this, "",
                "Loading. Please wait...", true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            firebaseToken = task.getResult().getToken();
                            //String email = mAuth.getCurrentUser().getEmail();
                            User user = new User("CUSTOMER",cachedEmail, firebaseToken);

                            DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

                            dbUsers.child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(FourDigitPage.this, "Firebase Auth Complete", Toast.LENGTH_LONG).show();
                                        System.out.println("============== Firebase Auth Complete================");
                                        sendToSignUp(firebaseToken, acccountType);
                                        progressDialog.dismiss();

                                    }
                                    progressDialog.dismiss();
                                }
                            });


                        } else {
                            progressDialog.dismiss();
                            //Toast.makeText(FourDigitPage.this, "Firebase Auth Fail", Toast.LENGTH_LONG).show();
                            System.out.println("============== Firebase Auth Fail ================");

                        }
                    }
                });

    }

    private void sendToSignUp(String FireBaseToken, String accountType) {

        System.out.println(" ==================== sendEmailToSignUp :: "+cachedEmail+" ==================== ");
        System.out.println(" ==================== Firebasetoken :: "+FireBaseToken+" ==================== ");
        progressDialog = new ProgressDialog(firstTimeLinkPaymentActivity.this);
        progressDialog = ProgressDialog.show(firstTimeLinkPaymentActivity.this, "",
                "Verifying. Please wait...", true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        System.out.println("POST:"+cachedEmail);
        System.out.println("POST:"+cachedPassword);
        System.out.println("POST:"+cachedFirstName);
        System.out.println("POST:"+cachedLastName);
        System.out.println("POST:"+cachedCustomerImage);
        System.out.println("POST:"+cachedAccountType);
        System.out.println("POST:"+serviceProvider);
        System.out.println("POST:"+accountNumber);
        System.out.println("POST:"+firebaseToken);

        Call<LoginResponse> call = jsonPlaceHolderApi.postNewCustomer(cachedEmail,cachedPassword,cachedFirstName,cachedLastName,cachedCustomerImage,cachedAccountType,serviceProvider,accountNumber,firebaseToken);

        // PUT DATA FOR Email VERIFICATION
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                if(response.code() != 200 ){
                    // SERVER ERROR
                    if (cachedAccountType.equals("FACEBOOK"))  sharedPref.edit().putString("token", "NO TOKEN").commit();

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Oops. Something went wrong :( , please try again later...", Toast.LENGTH_SHORT).show();
                    System.out.println(" ==================== Error Code :: "+response.code()+" ==================== ");
                    progressDialog.dismiss();

                }else {
                    // Code 200 = FOUND IN DB = receive Account Type
                    System.out.println(" ==================== Result Code :: "+response.code()+" ==================== ");

                    progressDialog.dismiss();
                    customerId = response.body().getCustomerId();
                    customerSessionToken = response.body().getCustomerSessionToken();

                    System.out.println(" ==================== Response SignUp Customer Id:: "+customerId+" ======================= ");
                    System.out.println(" ==================== Response SignUp token :: "+customerSessionToken+" ======================= ");

                    sharedPref.edit().putString("token", response.body().getCustomerSessionToken()).commit();
                    sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
                    sharedPref.edit().putString("email", cachedEmail).commit();
                    sharedPref.edit().putString("account_type", cachedAccountType).commit();
                    progressDialog.dismiss();

                    startActivity(new Intent(firstTimeLinkPaymentActivity.this,homev2Activity.class));

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                System.out.println("\n\n\n\n********************"+ t.getMessage() +"********************\n\n\n\n");

            }
        });

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
