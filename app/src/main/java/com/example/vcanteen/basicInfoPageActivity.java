package com.example.vcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class basicInfoPageActivity extends AppCompatActivity {

    String cachedEmail, cachedPassword, cachedFirstName, cachedLastName, cachedFirebaseToken,cachedAccountType;
    String firstName, lastName, cachedCustomerImage;
    EditText firstNameField, lastNameField;
    Button clearFirstName, clearLastName;
    TextView inline;
    ImageButton nextBtn;

    private static final Pattern NAME_PATTERN =
            Pattern.compile("[a-zA-Z][a-zA-Z ]+[a-zA-Z]$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info_page);

        firstNameField = findViewById(R.id.firstNameBox);
        lastNameField = findViewById(R.id.lastNameBox);
        clearFirstName = findViewById(R.id.firstNameClearBtn);
        clearLastName = findViewById(R.id.lastNameClearTextBtn);
        inline = findViewById(R.id.inline);
        nextBtn = findViewById(R.id.next_button);

        cachedEmail = getIntent().getStringExtra("cachedEmail");
        cachedPassword = getIntent(). getStringExtra("cachedPassword");
        cachedFirstName = getIntent().getStringExtra("cachedFirstName");
        cachedLastName = getIntent().getStringExtra("cachedLastName");
        cachedCustomerImage = getIntent().getStringExtra("cachedCustomerImage");
        cachedAccountType = getIntent().getStringExtra("cachedAccountType");
        cachedFirebaseToken = getIntent().getStringExtra("cachedFirebaseToken");

        System.out.println("into basic info url check : "+cachedCustomerImage);
        firstNameField.setText(cachedFirstName);
        lastNameField.setText(cachedLastName);

        clearFirstName.setOnClickListener(v -> {
            firstNameField.getText().clear();
        });

        clearLastName.setOnClickListener(v -> {
            lastNameField.getText().clear();
        });

        lastNameField.setOnEditorActionListener(editorListener);

        nextBtn.setOnClickListener(v -> {
            firstName = firstNameField.getText().toString();
            lastName = lastNameField.getText().toString();
            if(firstName.isEmpty()||lastName.isEmpty()) {
                inline.setVisibility(View.VISIBLE);
                inline.setText("Please fill in both fields.");
            } else if (!(NAME_PATTERN.matcher(firstName).matches()) || !(NAME_PATTERN.matcher(lastName).matches())) {
                inline.setVisibility(View.VISIBLE);
                inline.setText("Name can only contain a-z, A-Z.");
            } else {
                inline.setVisibility(View.INVISIBLE);
                Intent i = new Intent(this, firstTimeLinkPaymentActivity.class);
                i.putExtra("cachedFirstName", firstName);
                i.putExtra("cachedLastName", lastName);
                i.putExtra("cachedEmail", cachedEmail);
                i.putExtra("cachedPassword", cachedPassword);
                i.putExtra("cachedAccountType", cachedAccountType);
                i.putExtra("cachedCustomerImage", cachedCustomerImage);
                i.putExtra("cachedFirebaseToken", cachedFirebaseToken);
                System.out.println("basin info pw check : " + cachedPassword);
                startActivity(i);
            }
        });

        findViewById(R.id.relativeLayout).setOnTouchListener((v, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        });
    }

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if(actionId == EditorInfo.IME_ACTION_DONE){
                firstName = firstNameField.getText().toString();
                lastName = lastNameField.getText().toString();
                if(firstName.isEmpty()||lastName.isEmpty()) {
                    inline.setVisibility(View.VISIBLE);
                    inline.setText("Please fill in both fields.");
                } else if (!(NAME_PATTERN.matcher(firstName).matches()) || !(NAME_PATTERN.matcher(lastName).matches())) {
                    inline.setVisibility(View.VISIBLE);
                    inline.setText("Name can only contain a-z, A-Z.");
                } else {
                    inline.setVisibility(View.INVISIBLE);
                    Intent i = new Intent(basicInfoPageActivity.this, firstTimeLinkPaymentActivity.class);
                    i.putExtra("cachedFirstName", firstName);
                    i.putExtra("cachedLastName", lastName);
                    i.putExtra("cachedEmail", cachedEmail);
                    i.putExtra("cachedPassword", cachedPassword);
                    i.putExtra("cachedAccountType", cachedAccountType);
                    i.putExtra("cachedCustomerImage", cachedCustomerImage);
                    i.putExtra("cachedFirebaseToken", cachedFirebaseToken);
                    System.out.println("basin info pw check : " + cachedPassword);
                    startActivity(i);
                }
            }

            return false;
        }
    };
}
