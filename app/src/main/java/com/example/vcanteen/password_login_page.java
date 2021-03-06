package com.example.vcanteen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.Data.Customers;
import com.example.vcanteen.Data.LoginResponse;
import com.example.vcanteen.Data.RecoverPass;
import com.example.vcanteen.Data.TokenResponse;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class password_login_page extends AppCompatActivity {

    private String email;
    private String passwd;
    private String account_type = "NORMAL";
    private EditText passwdField;
    private Button showBtn;
    private ImageButton next;
    private TextView errorMessage;

    private TextView pwrecoverbtn;
    private Dialog confirmDialog;
    private Dialog recoverWarningDialog;
    private Button ok1btn;
    private Button ok2btn;
    private Button cancelbtn;

    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference dbUsers;
    private String firebaseToken;
    private boolean isHidden = true;
    // vcanteen.herokuapp.com/
    private final String url = "https://vcanteen.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login_page);


        FirebaseApp.initializeApp(password_login_page.this);
        mAuth = FirebaseAuth.getInstance();

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        System.out.println(sharedPref.getString("token", "empty token"));
        System.out.println(sharedPref.getString("email", "empty email"));

        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                email = null;
            } else {
                email = extras.getString("cachedEmail");
            }
        } else {
            email = (String) savedInstanceState.getSerializable("cachedEmail");
        }


        passwdField = findViewById(R.id.passwordBox);
        showBtn = findViewById(R.id.show_pw_btn);
        next = findViewById(R.id.next_button);
        errorMessage = findViewById(R.id.inlineError);

        passwdField.setOnEditorActionListener(editorListener);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isHidden) {
                    showBtn.setText("HIDE");
                    passwdField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isHidden = false;
                } else {
                    showBtn.setText("SHOW");
                    passwdField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isHidden = true;
                }
            }
        });


        final Context context = this;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwd = passwdField.getText().toString();
                if (passwd.equals("")) {
                    errorMessage.setText("Password cannot be blank. Please try again.");
                    errorMessage.setVisibility(View.VISIBLE);
                    return;
                }
                progressDialog = new ProgressDialog(context);
                progressDialog = ProgressDialog.show(context, "",
                        "Loading. Please wait...", true);
                final Intent intent = new Intent(password_login_page.this, homev2Activity.class);


                //////////////hash password/////////////
                passwd = new String(Hex.encodeHex(DigestUtils.sha256(passwdField.getText().toString())));
                System.out.println(passwd);
                account_type = "NORMAL";

                // start firebase login
                System.out.println("Firebase email: " + email);
                System.out.println("Firebase passwd: " + passwd);

                //////////////START mAuth/////////////
                mAuth.signInWithEmailAndPassword(email, passwd)
                        .addOnCompleteListener(password_login_page.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("mAuth SUCCESS");

                                    // Reteive firebase token
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    dbUsers = FirebaseDatabase.getInstance().getReference("users").child(uid);

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

                                    System.out.println("check1");
                                    dbUsers.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot dsUser : dataSnapshot.getChildren())

                                                firebaseToken = dsUser.getValue(String.class);
                                            System.out.println("token: "+firebaseToken);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            System.out.println("================== FIREBASE TOKEN to sharepref : "+firebaseToken+" ==================");
                                            sharedPref.edit().putString("firebaseToken", firebaseToken).commit();
                                            System.out.println("================== Begin Login by NORMAL LOGIN ==================");
                                            System.out.println("Calling HTTP Login request");

                                            Call<LoginResponse> call = jsonPlaceHolderApi.sendLoginV2(email, passwd,  firebaseToken);
                                            call.enqueue(new Callback<LoginResponse>() {
                                                @Override
                                                public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                                                    if (!response.isSuccessful()) {
                                                        errorMessage.setText("An error occurred");
                                                        errorMessage.setVisibility(View.VISIBLE);
                                                        progressDialog.dismiss();
                                                    }
                                                    if (response.code() != 200) {
                                                        errorMessage.setText("Password is incorrect. Please try again.");
                                                        errorMessage.setVisibility(View.VISIBLE);
                                                        progressDialog.dismiss();
                                                    } else {
                                                        System.out.println(response.body().toString());
                                                        sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
                                                        sharedPref.edit().putString("token", response.body().getCustomerSessionToken()).commit();
                                                        sharedPref.edit().putString("email", email).commit();
                                                        sharedPref.edit().putString("account_type", account_type).commit();
                                                        sharedPref.edit().putString("firebaseToken", firebaseToken).commit();
                                                        progressDialog.dismiss();
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<LoginResponse> call, Throwable t) {
                                                    System.out.println("ERROR");
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "HTTP request Error", Toast.LENGTH_SHORT).show();
                                                    t.printStackTrace();
                                                }
                                            });

                                        }
                                    }, 3500);


                                } else {
                                    System.out.println("Firebase login FAIL");
                                    errorMessage.setText("Password is incorrect. Please try again");
                                    errorMessage.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });

        pwrecoverbtn = findViewById(R.id.password_recov_btn);
        pwrecoverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecoverPopup();
            }
        });

        findViewById(R.id.relativeLayout).setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        });
    }

    private void saveToken(String token) {

        System.out.println("entered savetoken");
        String email = mAuth.getCurrentUser().getEmail();
        System.out.println("firebase: "+token);
        Customers customer = new Customers(email, null, null, "CUSTOMER", null, null, token);
        sharedPref.edit().putString("token", token).commit();
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


    private void openRecoverPopup() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        recoverWarningDialog = new Dialog(password_login_page.this);
        recoverWarningDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recoverWarningDialog.setContentView(R.layout.recoverwarningdialog);

        ok1btn = (Button) recoverWarningDialog.findViewById(R.id.ok1_btn);
        cancelbtn = (Button) recoverWarningDialog.findViewById(R.id.cancel_btn);

        ok1btn.setEnabled(true);
        cancelbtn.setEnabled(true);

        final Context context = this;
        ok1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(context);
                progressDialog = ProgressDialog.show(context, "",
                        "Loading. Please wait...", true);
                confirmDialog = new Dialog(password_login_page.this);
                confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                confirmDialog.setContentView(R.layout.confirmdialog);

                ok2btn = (Button) confirmDialog.findViewById(R.id.ok2_btn);

                ok2btn.setEnabled(true);

                ok2btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        confirmDialog.cancel();
                    }
                });

                //code for send request to back-end here
                Call<Void> call = jsonPlaceHolderApi.recoverPass(new RecoverPass(email));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() != 200)
                            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                        else {
                            confirmDialog.show();
                            progressDialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

                recoverWarningDialog.cancel();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverWarningDialog.cancel();
            }
        });

        recoverWarningDialog.show();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(password_login_page.this, emailActivity.class));
    }

    /*@Override
    public void finish() {
         super.finish();
         overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }*/

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if(actionId == EditorInfo.IME_ACTION_DONE){
                Gson gson = new GsonBuilder().serializeNulls().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

                passwd = passwdField.getText().toString();
                if (passwd.equals("")) {
                    errorMessage.setText("Password cannot be blank. Please try again.");
                    errorMessage.setVisibility(View.VISIBLE);
                    return false;
                }
                progressDialog = new ProgressDialog(password_login_page.this);
                progressDialog = ProgressDialog.show(password_login_page.this, "",
                        "Loading. Please wait...", true);
                final Intent intent = new Intent(password_login_page.this, homev2Activity.class);


                //////////////hash password/////////////
                passwd = new String(Hex.encodeHex(DigestUtils.sha256(passwdField.getText().toString())));
                System.out.println(passwd);
                account_type = "NORMAL";

                // start firebase login
                System.out.println("Firebase email: " + email);
                System.out.println("Firebase passwd: " + passwd);

                //////////////START mAuth/////////////
                mAuth.signInWithEmailAndPassword(email, passwd)
                        .addOnCompleteListener(password_login_page.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("mAuth SUCCESS");

                                    // Reteive firebase token
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    dbUsers = FirebaseDatabase.getInstance().getReference("users").child(uid);

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

                                    System.out.println("check1");
                                    dbUsers.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot dsUser : dataSnapshot.getChildren())

                                                firebaseToken = dsUser.getValue(String.class);
                                            System.out.println("token: "+firebaseToken);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            System.out.println("================== FIREBASE TOKEN to sharepref : "+firebaseToken+" ==================");
                                            sharedPref.edit().putString("firebaseToken", firebaseToken).commit();
                                            System.out.println("================== Begin Login by NORMAL LOGIN ==================");
                                            System.out.println("Calling HTTP Login request");

                                            Call<LoginResponse> call = jsonPlaceHolderApi.sendLoginV2(email, passwd,  firebaseToken);
                                            call.enqueue(new Callback<LoginResponse>() {
                                                @Override
                                                public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                                                    if (!response.isSuccessful()) {
                                                        errorMessage.setText("An error occurred");
                                                        errorMessage.setVisibility(View.VISIBLE);
                                                        progressDialog.dismiss();
                                                    }
                                                    if (response.code() != 200) {
                                                        errorMessage.setText("Password is incorrect. Please try again.");
                                                        errorMessage.setVisibility(View.VISIBLE);
                                                        progressDialog.dismiss();
                                                    } else {
                                                        System.out.println(response.body().toString());
                                                        sharedPref.edit().putInt("customerId", response.body().getCustomerId()).commit();
                                                        sharedPref.edit().putString("token", response.body().getCustomerSessionToken()).commit();
                                                        sharedPref.edit().putString("email", email).commit();
                                                        sharedPref.edit().putString("account_type", account_type).commit();
                                                        sharedPref.edit().putString("firebaseToken", firebaseToken).commit();
                                                        progressDialog.dismiss();
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<LoginResponse> call, Throwable t) {
                                                    System.out.println("ERROR");
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "HTTP request Error", Toast.LENGTH_SHORT).show();
                                                    t.printStackTrace();
                                                }
                                            });

                                        }
                                    }, 3500);


                                } else {
                                    System.out.println("Firebase login FAIL");
                                    errorMessage.setText("Password is incorrect. Please try again");
                                    errorMessage.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }

            return false;
        }
    };

}
