package com.example.vcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class passwordSignUpPage extends AppCompatActivity {

    String email;
    String password;
    String confirmPassword;
    EditText passwordField, confirmPasswordField;
    Button showPasswordBtn, showConfirmedPasswordBtn;
    TextView inline;
    ImageButton nextBtn;
    private boolean isHidden = true;
    private boolean isHidden2 = true;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9@!#$%^&+-=](?=\\S+$).*$");  // Password Constraint

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_sign_up_page);

        email = getIntent().getStringExtra("cachedEmail");

        passwordField = findViewById(R.id.passwordBox);
        confirmPasswordField = findViewById(R.id.passwordConfirmBox);
        showPasswordBtn = findViewById(R.id.show_pw_btn_signup);
        showConfirmedPasswordBtn = findViewById(R.id.show_pw_confirmation_btn_signup);
        inline = findViewById(R.id.inline);
        nextBtn = findViewById(R.id.next_button);

        showPasswordBtn.setOnClickListener(v -> {
            if (isHidden) {
                showPasswordBtn.setText("HIDE");
                passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isHidden = false;
            } else {
                showPasswordBtn.setText("SHOW");
                passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isHidden = true;
            }
        });

        showConfirmedPasswordBtn.setOnClickListener(v -> {
            if (isHidden2) {
                showConfirmedPasswordBtn.setText("HIDE");
                confirmPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isHidden2 = false;
            } else {
                showConfirmedPasswordBtn.setText("SHOW");
                confirmPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isHidden2 = true;
            }
        });

        confirmPasswordField.setOnEditorActionListener(editorListener);

        nextBtn.setOnClickListener(v -> {
            password = passwordField.getText().toString();

            confirmPassword = confirmPasswordField.getText().toString();

            if (passwordField.getText().toString().isEmpty() || confirmPasswordField.getText().toString().isEmpty()) {
                inline.setText("Password cannot be empty. Please try again.");
                inline.setVisibility(View.VISIBLE);
                return;
            } else if (!password.equals(confirmPassword)) {
                //password doesn't match
                inline.setText("Passwords do not match. Please try again.");
                inline.setVisibility(View.VISIBLE);
            } else if (password.length() < 8) {
                inline.setText("Password must be longer than 8 characters.");
                inline.setVisibility(View.VISIBLE);
            } else if (!(PASSWORD_PATTERN.matcher(password).matches()) || !(PASSWORD_PATTERN.matcher(confirmPassword).matches())) {
                inline.setText("Only a-z A-Z 0-9 _ - * ‘ “ # & () @ are allowed.");
                inline.setVisibility(View.VISIBLE);
            }  else {
                inline.setVisibility(View.INVISIBLE);
                String passwordHash = new String(Hex.encodeHex(DigestUtils.sha256(passwordField.getText().toString())));
                Intent i = new Intent(this, basicInfoPageActivity.class);
                i.putExtra("cachedEmail", email);
                i.putExtra("cachedPassword",passwordHash);
                i.putExtra("cachedAccountType", "NORMAL");
                System.out.println("From PW Sign Up : "+email+", "+", pw: "+password);
                startActivity(i);
            }
        });

        findViewById(R.id.relativeLayout).setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            return true;
        });
    }

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if(actionId == EditorInfo.IME_ACTION_DONE){
                password = passwordField.getText().toString();

                confirmPassword = confirmPasswordField.getText().toString();

                if (passwordField.getText().toString().isEmpty() || confirmPasswordField.getText().toString().isEmpty()) {
                    inline.setText("Password cannot be empty. Please try again.");
                    inline.setVisibility(View.VISIBLE);
                    return false;
                } else if (!password.equals(confirmPassword)) {
                    //password doesn't match
                    inline.setText("Passwords do not match. Please try again.");
                    inline.setVisibility(View.VISIBLE);
                } else if (password.length() < 8) {
                    inline.setText("Password must be longer than 8 characters.");
                    inline.setVisibility(View.VISIBLE);
                } else if (!(PASSWORD_PATTERN.matcher(password).matches()) || !(PASSWORD_PATTERN.matcher(confirmPassword).matches())) {
                    inline.setText("Only a-z A-Z 0-9 _ - * ‘ “ # & () @ are allowed.");
                    inline.setVisibility(View.VISIBLE);
                }  else {
                    inline.setVisibility(View.INVISIBLE);
                    String passwordHash = new String(Hex.encodeHex(DigestUtils.sha256(passwordField.getText().toString())));
                    Intent i = new Intent(passwordSignUpPage.this, basicInfoPageActivity.class);
                    i.putExtra("cachedEmail", email);
                    i.putExtra("cachedPassword",passwordHash);
                    i.putExtra("cachedAccountType", "NORMAL");
                    System.out.println("From PW Sign Up : "+email+", "+", pw: "+password);
                    startActivity(i);
                }
            }

            return false;
        }
    };
}
