package com.example.vcanteen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.Data.Customers;
import com.example.vcanteen.Data.ResetPass;
import com.example.vcanteen.Data.TokenResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class changePasswordActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9@!#$%^&+-=](?=\\S+$).*$");  // Password Constraint
    // (?=\S+$) = no space is allowed.
    // special characters that are allowed @ ! # $ % ^ & + = -

    private Button changePasswordButton;
    private EditText currentPassword, newPassword, confirmNewPassword;
    private TextView showHide1, showHide2, showHide3; //show and hide password button
    private TextView checkCurrentPasswordText, checkNewPasswordText;
    private String email;
    private String account_type;
    private SharedPreferences sharedPref;
    private ProgressDialog progressDialog;

    private Dialog confirmChangePassDialog;
    private Button confirmChangePass;
    private Button cancelChangePass;

    private String firebaseToken;

    private String x;
    private String y;
    private String z;

    private final String url = "https://vcanteen.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        checkCurrentPasswordText = (TextView) findViewById(R.id.checkCurrentPasswordText);
        checkNewPasswordText = (TextView) findViewById(R.id.checkNewPasswordText);

        currentPassword = (EditText) findViewById(R.id.currentPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmNewPassword = (EditText) findViewById(R.id.confirmNewPassword);

        showHide1 = (TextView) findViewById(R.id.showHide1);
        showHide2 = (TextView) findViewById(R.id.showHide2);
        showHide3 = (TextView) findViewById(R.id.showHide3);



        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        email = sharedPref.getString("email", "empty email");
        account_type = sharedPref.getString("account_type", "UNKONWN");
        firebaseToken = sharedPref.getString("firebaseToken", "NO TOKEN");


        // show and hide password for current password field
        showHide1.setVisibility(View.GONE);
        currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        currentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (currentPassword.getText().length() > 0) {
                    showHide1.setVisibility(View.VISIBLE);
                } else {
                    showHide1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showHide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHide1.getText() == "SHOW") {
                    showHide1.setText("HIDE");
                    currentPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    currentPassword.setSelection(currentPassword.length());
                } else {
                    showHide1.setText("SHOW");
                    currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    currentPassword.setSelection(currentPassword.length());
                }
            }
        });

        // show and hide password for new password field
        showHide2.setVisibility(View.GONE);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (newPassword.getText().length() > 0) {
                    showHide2.setVisibility(View.VISIBLE);
                } else {
                    showHide2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        showHide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHide2.getText() == "SHOW") {
                    showHide2.setText("HIDE");
                    newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    newPassword.setSelection(newPassword.length());
                } else {
                    showHide2.setText("SHOW");
                    newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    newPassword.setSelection(newPassword.length());
                }
            }
        });

        // show and hide password for confirm new password field
        showHide3.setVisibility(View.GONE);
        confirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (confirmNewPassword.getText().length() > 0) {
                    showHide3.setVisibility(View.VISIBLE);
                } else {
                    showHide3.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        showHide3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHide3.getText() == "SHOW") {
                    showHide3.setText("HIDE");
                    confirmNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmNewPassword.setSelection(confirmNewPassword.length());
                } else {
                    showHide3.setText("SHOW");
                    confirmNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmNewPassword.setSelection(confirmNewPassword.length());
                }
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changePassword();

            }
        });

        findViewById(R.id.constraintLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

    }

    private void changePassword() {
        x = currentPassword.getText().toString();
        y = newPassword.getText().toString();
        z = confirmNewPassword.getText().toString();
        final boolean[] check1 = {true};
        boolean check2 = false;

        if(x.matches("")||y.matches("")||z.matches("")){ //pin just add
            checkNewPasswordText.setText("You must fill out all the fields.");
            currentPassword.setText("");
            newPassword.setText("");
            confirmNewPassword.setText("");
        } else if(!(PASSWORD_PATTERN.matcher(y).matches())||!(PASSWORD_PATTERN.matcher(z).matches())||!(PASSWORD_PATTERN.matcher(x).matches())){
            checkNewPasswordText.setText("Must be letter, number or these characters _ - * ' \" # & () @");
            //currentPassword.setText("");
            newPassword.setText("");
            confirmNewPassword.setText("");
        } else if(!(y.equals(z))){
            checkNewPasswordText.setText("Password doesn't match. Please try again.");
            //currentPassword.setText("");
            newPassword.setText("");
            confirmNewPassword.setText("");
        } else if (y.length() < 8 || y.length() > 20) {
            checkNewPasswordText.setText("Invalid Password. Please try again.");
            //currentPassword.setText("");
            newPassword.setText("");
            confirmNewPassword.setText("");
        } else if(x.equals(y)){
            checkNewPasswordText.setText("Your new password can't be the same as your current passaword.");
            currentPassword.setText("");
            newPassword.setText("");
            confirmNewPassword.setText("");
        } else {
            confirmChangePassDialog = new Dialog(changePasswordActivity.this);
            confirmChangePassDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            confirmChangePassDialog.setContentView(R.layout.dialog_confirm_changepass);

            confirmChangePass = confirmChangePassDialog.findViewById(R.id.confirm_changepass);
            cancelChangePass = confirmChangePassDialog.findViewById(R.id.cancel_confirm_changepass);

            confirmChangePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmChangePassDialog.cancel();
                    progressDialog = new ProgressDialog(changePasswordActivity.this);
                    progressDialog = ProgressDialog.show(changePasswordActivity.this
                            , "",
                            "Loading. Please wait...", true);
                    //check if match with current database password
                    // replace "asdfg" with data from database
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                    final JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


                    Customers postCustomer = new Customers(email, null, null, account_type, null, new String(Hex.encodeHex(DigestUtils.sha256(x))), firebaseToken);
                    Call<TokenResponse> call = jsonPlaceHolderApi.createCustomer(postCustomer);
                    System.out.println("Curr pass: "+new String(Hex.encodeHex(DigestUtils.sha256(x))));
                    System.out.println("New pass: "+new String(Hex.encodeHex(DigestUtils.sha256(y))));
                    System.out.println(postCustomer.toString());
                    call.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            System.out.println(response.code());
                            if (response.code() != 200) {
                                checkCurrentPasswordText.setText("Current password is incorrect. Please try again.");
                                checkNewPasswordText.setText("");
                                currentPassword.setText("");
                                newPassword.setText("");
                                confirmNewPassword.setText("");
                                progressDialog.dismiss();
                                check1[0] = false;
                            } else {

                                Call<Void> resetCall = jsonPlaceHolderApi.resetPass(new ResetPass(email, new String(Hex.encodeHex(DigestUtils.sha256(y)))));

                                resetCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.code() != 200)
                                            Toast.makeText(getApplicationContext(), "Could not reset your password. Please try again later.", Toast.LENGTH_LONG).show();
                                        else {
                                            checkCurrentPasswordText.setText("");
                                            check1[0] = true;
                                            currentPassword.setText("");
                                            newPassword.setText("");
                                            confirmNewPassword.setText("");
                                            checkNewPasswordText.setText("");
                                            Toast.makeText(changePasswordActivity.this, "Password successfully changed.", Toast.LENGTH_LONG).show();
                                        }
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {

                        }
                    });
                }
            });

            cancelChangePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmChangePassDialog.cancel();
                }
            });

            confirmChangePassDialog.show();

        }
    }
}
