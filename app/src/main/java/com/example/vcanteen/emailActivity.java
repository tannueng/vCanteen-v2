package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vcanteen.Data.Customers;
import com.example.vcanteen.Data.LoginCustomer;
import com.example.vcanteen.Data.LoginResponse;
import com.example.vcanteen.Data.RecoverPass;
import com.example.vcanteen.Data.TokenResponse;
import com.example.vcanteen.Data.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class emailActivity extends AppCompatActivity {

    private ImageButton next_button;
    private static final String EMAIL = "email";
    private LoginButton loginButton;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;
    private String email;
    private TextView inline;
//    private TextView error2;
    private EditText emailField;

    private final String url = "https://vcanteen.herokuapp.com/";
    private boolean exit = false;

    private FirebaseAuth mAuth;
    private DatabaseReference dbUsers;
    private String firebaseToken;

    private String first_name;
    private String last_name;

    private String profile_url;
    private String passwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_enter_page);

        RelativeLayout layout = findViewById(R.id.emailRelative);
        layout.setOnTouchListener((view, ev) -> {
            hideKeyboard(view);
            return false;
        });

        FirebaseApp.initializeApp(emailActivity.this);
        mAuth = FirebaseAuth.getInstance();

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        LoginManager.getInstance().logInWithReadPermissions(emailActivity.this, Arrays.asList("public_profile", "email"));

        next_button = findViewById(R.id.next_button /*xml next_button */);
        loginButton = findViewById(R.id.login_button); //facebook login button
        emailField = findViewById(R.id.editEmail);

        final Context context = this;
        final Button cleartxtbtn = findViewById(R.id.clear_text_btn);
        final EditText emailbox = findViewById(R.id.editEmail);
        inline = findViewById(R.id.inlineError);
//        error2 = findViewById(R.id.error2);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        System.out.println(sharedPref.getString("token", "empty token"));
        System.out.println(sharedPref.getString("email", "empty email"));

        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //////////////CLEAR BUTTON//////////////////
        cleartxtbtn.setOnClickListener(v -> {
            emailbox.getText().clear();
        });


        ////////////////NEXT BUTTON TAPPED/////////////
        next_button.setOnClickListener(v -> {
            openpassword_login_page();
        });



        loginButton.setOnClickListener(v -> {
            System.out.println("pressed facebook button");
            Fblogin();
        });






        //////////////FACEBOOK LOGIN BUTTON//////////////
//        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                progressDialog = new ProgressDialog(context);
//                progressDialog = ProgressDialog.show(context, "",
//                        "Loading. Please wait...", true);
//                GraphRequest request = GraphRequest.newMeRequest(
//                        AccessToken.getCurrentAccessToken(),
//                        (object, response) -> {
//                            System.out.println("ON COMPLETED GRAPHREQUEST");
//                            final Intent intent = new Intent(emailActivity.this, homev2Activity.class);
//                            email = object.optString("email");
//                            System.out.println("DEBUG EMAIL:"+email);
//                            first_name = object.optString("first_name");
//                            last_name = object.optString("last_name");
//
//                            profile_url = null;
//                            try {
//                                profile_url = (String) object.getJSONObject("picture").getJSONObject("data").get("url");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println(email+", "+ first_name+", "+ last_name+", "+ profile_url);
//                            final String account_type = "FACEBOOK";
//
//
//                            //////
//                            Retrofit retrofit = new Retrofit.Builder()
//                                    .baseUrl("https://vcanteen.herokuapp.com/")
//                                    .addConverterFactory(GsonConverterFactory.create())
//                                    .build();
//
//                            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//                            Call<LoginResponse> call = jsonPlaceHolderApi.checkEmail(email);
//
//                            call.enqueue(new Callback<LoginResponse>() {
//                                @Override
//                                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                                    if(response.code() != 200 && response.code() != 404){
//                                        // SERVER ERROR
//                                        progressDialog.dismiss();
//                                        Toast.makeText(getApplicationContext(), "Oops. Something went wrong :( , please try again later...", Toast.LENGTH_SHORT).show();
//                                        System.out.println(" ==================== Error Code :: "+response.code()+" ==================== ");
//
//
//                                    }else if(response.code()==404) {
//                                        //Email is not in the Database go to register
//
//                                    }
//                                    LoginResponse response1 = response.body();
//                                    if(response1.getAccountType().equals("FACEBOOK")) {
//
//
//                                        Call<LoginResponse> call2 = jsonPlaceHolderApi.sendLoginFacebook(email, firebaseToken);
//
//                                    } else if (response1.getAccountType().equals("NORMAL")) {
//
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<LoginResponse> call, Throwable t) {
//
//                                }
//                            });
//
//
//
//
//
//                            // get firebase token
//                            System.out.println("FB: "+email);
//                            mAuth.signInWithEmailAndPassword(email, "firebaseOnlyNaja")
//                                    .addOnCompleteListener(emailActivity.this, new OnCompleteListener<AuthResult>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                            if (task.isSuccessful()) {
//                                                System.out.println("SUCCESS");
//                                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                                dbUsers = FirebaseDatabase.getInstance().getReference("users").child(uid);
//
//                                                dbUsers.addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                        for(DataSnapshot dsUser: dataSnapshot.getChildren())
//                                                            firebaseToken = dsUser.getValue(String.class);
//                                                        System.out.println("Firebase Token : "+firebaseToken);
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                                Customers postCustomer = new Customers(email, first_name, last_name, account_type, profile_url, "firebaseOnlyNaja", firebaseToken);
//                                                System.out.println(postCustomer.toString());
//                                                postCustomer = new Customers(email, first_name, last_name, account_type, profile_url, null, firebaseToken);
//                                                Call<TokenResponse> call = jsonPlaceHolderApi.createCustomer(postCustomer);
//
//                                                // HTTP POST
//                                                call.enqueue(new Callback<TokenResponse>() {
//                                                    @Override
//                                                    public void onResponse(Call<TokenResponse> call, final Response<TokenResponse> response) {
//                                                        if(!response.isSuccessful())
//                                                            Toast.makeText(getApplicationContext(), "Error Occurred, please try again.", Toast.LENGTH_SHORT);
////                                        TokenResponse tokenResponse = response.body();
////                                        System.out.println(tokenResponse.statusCode);
////                                        System.out.println(response.body().toString());
//                                                        if(response.code() != 200)
//                                                            Toast.makeText(getApplicationContext(), "Either email or password is incorrect.", Toast.LENGTH_SHORT).show();
//                                                        else {
//                                                            //successfully logged in with facebook
//                                                            System.out.println(response.body().toString());
//                                                            sharedPref.edit().putInt("customerId", response.body().getCustID()).commit();
//                                                            sharedPref.edit().putString("token", response.body().getToken()).commit();
//                                                            sharedPref.edit().putString("email", email).commit();
//                                                            sharedPref.edit().putString("account_type", account_type).commit();
//                                                            sharedPref.edit().putString("firebaseToken", firebaseToken).commit();
//                                                            progressDialog.dismiss();
//
//                                                            startActivity(intent);
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<TokenResponse> call, Throwable t) {
//                                                        System.out.println("ERROR ONRESPONSE");
//                                                        progressDialog.dismiss();
//                                                        Toast.makeText(getApplicationContext(), "An error occured. Please try again.", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                            } else {
//                                                System.out.println("Firebase login FAIL");
//                                                Toast.makeText(getApplicationContext(), "This account does not appeared on Firebase Database", Toast.LENGTH_SHORT).show();
//                                                LoginManager.getInstance().logOut();
//                                                progressDialog.dismiss();
//                                            }
//                                        }
//                                    });
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "email,id,name,first_name,last_name,link,picture.type(large)");
//                request.setParameters(parameters);
//                request.executeAsync();
//
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//                exception.printStackTrace();
//
//            }
//        });

    }

    private void Fblogin() {
        //////////////FACEBOOK LOGIN BUTTON////////////// DAVE
        callbackManager = CallbackManager.Factory.create();

        System.out.println("went in fblogin");
        LoginManager.getInstance().logOut();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");

                                            String jsonresult = String.valueOf(json);
                                            System.out.println("JSON Result" + jsonresult);

                                            email = json.optString("email");
                                            String str_id = json.optString("id");
                                            first_name = json.optString("first_name");
                                            last_name = json.optString("last_name");

                                            profile_url = null;
                                            try {
                                                profile_url = (String) json.getJSONObject("picture").getJSONObject("data").get("url");
                                            } catch (JSONException e) {
                                                System.out.println("error with fb profile pic");
                                                e.printStackTrace();
                                            }
                                            passwd = "firebaseOnlyNaja";

                                            progressDialog = new ProgressDialog(emailActivity.this);
                                            progressDialog = ProgressDialog.show(emailActivity.this, "",
                                                    "Loading. Please wait...", true);

                                            mAuth.createUserWithEmailAndPassword(email, passwd)
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                registerToFirebase(email);
                                                                System.out.println("========= Email not found in Firebase, register new to firebase::::"+email+"===Pass::: "+passwd);

                                                            } else {
                                                                progressDialog.dismiss();
                                                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                                    System.out.println("========= Email FOUND in Firebase, login to firebase to get token::::"+email+"===Pass::: "+passwd);
                                                                    firebaseLogin(email,passwd);

                                                                } else {
                                                                    //Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                    System.out.println("============== Firebase Check Exist Error "+task.getException().getMessage()+" ================");
                                                                }
                                                            }

                                                        }
                                                    });
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name, last_name, email,link, picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

    }

    private void firebaseLogin(String email, String password) {
        progressDialog = new ProgressDialog(emailActivity.this);
        progressDialog = ProgressDialog.show(emailActivity.this, "",
                "Loading. Please wait...", true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(emailActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("firebaseLogin SUCCESS");
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            dbUsers = FirebaseDatabase.getInstance().getReference("users").child(uid);

                            dbUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dsUser: dataSnapshot.getChildren())
                                        firebaseToken = dsUser.getValue(String.class);
                                    System.out.println("==============firebaseLogin receive firebase token : "+firebaseToken);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    sharedPref.edit().putString("firebaseToken", firebaseToken).commit();
                                    passwd = null;
                                    sendJSONFacebook(email, firebaseToken);
                                    progressDialog.dismiss();

                                }
                            }, 3500);


                        } else {
                            System.out.println("FIREBASE LOGIN FAIL");
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void sendJSONFacebook(String email, String firebaseToken) {
        progressDialog = new ProgressDialog(emailActivity.this);
        progressDialog = ProgressDialog.show(emailActivity.this, "",
                "Loading. Please wait...", true);

        LoginCustomer loginCustomer = new LoginCustomer(email, firebaseToken);
        System.out.println("======================sent FACEBOOK loginCustomer to string::: "+loginCustomer.toString() + " ==sendLoginFacebook=============");

        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<LoginResponse> call = jsonPlaceHolderApi.sendLoginFacebook(email, firebaseToken);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("==================Code :::: "+response.code()+" ==sendLoginFacebook()================");

                if (response.code() != 200 && response.code() != 404 ) {
                    // Error
                    /*if (account_type.equals("FACEBOOK"))
                        sharedPref.edit().putString("token", "NO TOKEN JA EDOK").commit();*/
                    sharedPref.edit().putString("token", "NO TOKEN JA EDOK").commit();
                    progressDialog.dismiss();
//                    errorMessage.setText("Email or Password is Incorrect");
//                    errorMessage.setVisibility(View.VISIBLE);
                    LoginManager.getInstance().logOut();
                    /*if (account_type.equals("FACEBOOK"))
                        LoginManager.getInstance().logOut();
                    progressDialog.dismiss();*/
                } else if(response.code() == 404){
                    //Email not found in DB, go to sign up
                    System.out.println("================== FACEBOOK EMAIL NOT IN DB, GO SIGN UP ================");
                    progressDialog.dismiss();
                    Intent i = new Intent(emailActivity.this,basicInfoPageActivity.class);
                    // passing data to the next activity
                    i.putExtra("cachedEmail",email);
                    i.putExtra("cachedPassword", "firebaseOnlyNaja");
                    i.putExtra("cachedFirstName", first_name);
                    i.putExtra("cachedLastName", last_name);
                    i.putExtra("cachedAccountType","FACEBOOK");
                    i.putExtra("cachedFirebaseToken",firebaseToken);
                    startActivity(i);

                /*} else if(response.code()==409) {
                    progressDialog.dismiss();
                    errorMessage.setText("This account can only be logged into with Facebook");
                    errorMessage.setVisibility(View.VISIBLE);*/

                } else {
                    // Success
                    System.out.println("================== sendLoginFacebook() = SUCCESS! ================");
                    String type = response.body().getAccountType();

                    if(type.equals("FACEBOOK")){
                        // save customerId, token
                        sharedPref.edit().putString("token", response.body().getCustomerSessionToken()).commit();
                        sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
                        sharedPref.edit().putString("email", email).commit();
                        sharedPref.edit().putString("account_type",response.body().getAccountType()).commit();
                        //sharedPref.edit().putString("account_type", account_type).commit();

                        System.out.println("==================Account Type :::: "+response.body().getAccountType()+" ==================");
                        System.out.println("==================CUSTOMER ID :::: "+response.body().getCustomerId()+" ==================");
                        System.out.println("==================JWT Token :::: "+response.body().getCustomerSessionToken()+" ==================");
                        progressDialog.dismiss();
                        startActivity(new Intent(emailActivity.this, homev2Activity.class));


                    }else{

                        System.out.println("==================Account Type :::: "+response.body().getAccountType()+" ==================");
                        System.out.println("==================CUSTOMER ID :::: "+response.body().getCustomerId()+" ==================");
                        System.out.println("==================JWT Token :::: "+response.body().getCustomerSessionToken()+" ==================");
                        progressDialog.dismiss();
                        emailField.setText(email);

                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("==================send Login Fail :::: "+t.getMessage()+" ==================");

            }
        });
    }

    private void registerToFirebase(String email) {
        progressDialog = new ProgressDialog(emailActivity.this);
        progressDialog = ProgressDialog.show(emailActivity.this, "",
                "Loading. Please wait...", true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            firebaseToken = task.getResult().getToken();
                            String emailFromFirebase = mAuth.getCurrentUser().getEmail();
                            User user = new User("CUSTOMER",email, firebaseToken);

                            DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");

                            dbUsers.child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(LoginActivity.this, "Firebase Auth Complete", Toast.LENGTH_LONG).show();
                                        System.out.println("============== Firebase Auth Complete================");
                                        sendJSONFacebook(email, firebaseToken);
                                        progressDialog.dismiss();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            //Toast.makeText(LoginActivity.this, "Firebase Auth Fail", Toast.LENGTH_LONG).show();
                            System.out.println("============== Firebase Auth Fail ================");
                        }
                    }
                });
    }

    ///////AFTER PRESSED NEXT BUTTON//////////
    public void openpassword_login_page() {
        if (TextUtils.isEmpty(emailField.getText().toString())) {
//            error2.setVisibility(View.INVISIBLE);
            inline.setText("Please enter your email.");
//            inline.setTextSize(18);
            inline.setVisibility(View.VISIBLE);
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches()) {
            inline.setText("Invalid email address.");
            inline.setVisibility(View.VISIBLE);
            return;
        }
        progressDialog = new ProgressDialog(emailActivity.this);
        progressDialog = ProgressDialog.show(emailActivity.this, "",
                "Loading. Please wait...", true);

        final Intent pwloginintent = new Intent(this, password_login_page.class);
        final Intent pwSignUpIntent = new Intent(this, passwordSignUpPage.class);

        ///////////check for email validation here///////////
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        email = emailField.getText().toString();
//        Call<Void> call = jsonPlaceHolderApi.verifyEmail(new RecoverPass(email));
        Call<LoginResponse> call = jsonPlaceHolderApi.checkEmail(email);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("Verify Email: "+response.code());
//                System.out.println(new RecoverPass(email).toString());

                if (response.code() == 404){
                    //TO Password Sign Up
                    emailField.setInputType(InputType.TYPE_CLASS_TEXT);
                    String customerEmail = null;
                    customerEmail = emailField.getText().toString();
                    System.out.println("email from email page (putExtra) : "+customerEmail);
                    pwSignUpIntent.putExtra("cachedEmail", customerEmail);
                    startActivity(pwSignUpIntent);
                    return;
                }

                if (response.code() == 200&&response.body().accountType.equals("FACEBOOK")) { //error may be null
                    //email only for Facebook
                    inline.setText("This account can only be logged into with Facebook");
//                    inline.setTextSize(10);
                    inline.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                } else {
                    //200 NORMAL
                    emailField.setInputType(InputType.TYPE_CLASS_TEXT);
                    String customerEmail = null;
                    customerEmail = emailField.getText().toString();

                    pwloginintent.putExtra("cachedEmail", customerEmail);
                    progressDialog.dismiss();
                    startActivity(pwloginintent);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

//    private static String encodeTobase64(Bitmap image) {
//        Bitmap immagex = image;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
//
//        Log.e("LOOK", imageEncoded);
//        return imageEncoded;
//    }
    protected void hideKeyboard(View view)    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
        sharedPref.edit().putString("token", "NO TOKEN JA EDOK").apply();
        sharedPref.edit().putInt("customerId", 0).apply();
        sharedPref.edit().putString("email","").apply();
        sharedPref.edit().putString("account_type", "").apply();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

