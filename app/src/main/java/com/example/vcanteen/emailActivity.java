package com.example.vcanteen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.example.vcanteen.Data.RecoverPass;
import com.example.vcanteen.Data.TokenResponse;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


        //////////////FACEBOOK LOGIN BUTTON//////////////
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                progressDialog = new ProgressDialog(context);
                progressDialog = ProgressDialog.show(context, "",
                        "Loading. Please wait...", true);
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                System.out.println("ON COMPLETED GRAPHREQUEST");
                                final Intent intent = new Intent(emailActivity.this, vendorListActivity.class);
                                email = object.optString("email");
                                System.out.println("DEBUG EMAIL:"+email);
                                first_name = object.optString("first_name");
                                last_name = object.optString("last_name");

                                profile_url = null;
                                try {
                                    profile_url = (String) object.getJSONObject("picture").getJSONObject("data").get("url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(email+", "+ first_name+", "+ last_name+", "+ profile_url);
                                final String account_type = "FACEBOOK";

                                // get firebase token
                                System.out.println("FB: "+email);
                                mAuth.signInWithEmailAndPassword(email, "firebaseOnlyNaja")
                                        .addOnCompleteListener(emailActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    System.out.println("SUCCESS");
                                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    dbUsers = FirebaseDatabase.getInstance().getReference("users").child(uid);

                                                    dbUsers.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot dsUser: dataSnapshot.getChildren())
                                                                firebaseToken = dsUser.getValue(String.class);
                                                            System.out.println(firebaseToken);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                    Customers postCustomer = new Customers(email, first_name, last_name, account_type, profile_url, "firebaseOnlyNaja", firebaseToken);
                                                    System.out.println(postCustomer.toString());
                                                    postCustomer = new Customers(email, first_name, last_name, account_type, profile_url, null, firebaseToken);
                                                    Call<TokenResponse> call = jsonPlaceHolderApi.createCustomer(postCustomer);

                                                    // HTTP POST
                                                    call.enqueue(new Callback<TokenResponse>() {
                                                        @Override
                                                        public void onResponse(Call<TokenResponse> call, final Response<TokenResponse> response) {
                                                            if(!response.isSuccessful())
                                                                Toast.makeText(getApplicationContext(), "Error Occured, please try again.", Toast.LENGTH_SHORT);
//                                        TokenResponse tokenResponse = response.body();
//                                        System.out.println(tokenResponse.statusCode);
//                                        System.out.println(response.body().toString());
                                                            if(response.code() != 200)
                                                                Toast.makeText(getApplicationContext(), "Either email or password is incorrect.", Toast.LENGTH_SHORT).show();
                                                            else {
                                                                System.out.println(response.body().toString());
                                                                sharedPref.edit().putInt("customerId", response.body().getCustID()).commit();
                                                                sharedPref.edit().putString("token", response.body().getToken()).commit();
                                                                sharedPref.edit().putString("email", email).commit();
                                                                sharedPref.edit().putString("account_type", account_type).commit();
                                                                sharedPref.edit().putString("firebaseToken", firebaseToken).commit();
                                                                progressDialog.dismiss();
                                                                startActivity(intent);
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                                                            System.out.println("ERROR ESUS");
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getApplicationContext(), "An error occured. Please try again.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    System.out.println("Firebase login FAIL");
                                                    Toast.makeText(getApplicationContext(), "This account does not appeared on Firebase Databse", Toast.LENGTH_SHORT).show();
                                                    LoginManager.getInstance().logOut();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,id,name,first_name,last_name,link,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();

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
            inline.setVisibility(View.INVISIBLE);
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
        Call<Void> call = jsonPlaceHolderApi.verifyEmail(new RecoverPass(email));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response.code());
                System.out.println(new RecoverPass(email).toString());
                if(response.code()!= 200 && response.code() != 409) {
                    //wrong password
                    inline.setText("Incorrect Password.");
                    inline.setVisibility(View.VISIBLE);
//                    error2.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                } else if (response.code() == 409) {
                    //email only for Facebook
                    inline.setText("This account can only be logged into with Facebook");
//                    inline.setTextSize(10);
                    inline.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                } else if (response.code() == 404){
                    emailField.setInputType(InputType.TYPE_CLASS_TEXT);
                    String customerEmail = null;
                    customerEmail = emailField.getText().toString();

                    pwSignUpIntent.putExtra("cacheEmail", customerEmail);
                    startActivity(pwSignUpIntent);
                }

                else {
                    emailField.setInputType(InputType.TYPE_CLASS_TEXT);
                    String customerEmail = null;
                    customerEmail = emailField.getText().toString();

                    pwloginintent.putExtra("cachedemail", customerEmail);
                    progressDialog.dismiss();
                    startActivity(pwloginintent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    protected void hideKeyboard(View view)    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}

