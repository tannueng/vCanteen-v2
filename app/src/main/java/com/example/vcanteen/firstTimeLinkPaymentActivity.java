package com.example.vcanteen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vcanteen.POJO.currentDensity;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class firstTimeLinkPaymentActivity extends AppCompatActivity {

    Spinner serviceProviderSpinner;
    EditText accountNumberField;
    String serviceProvider, accountNumber;
    Button linkButton;
    TextView inline;
    String cachedEmail, cachedPassword, cachedFirstName, cachedLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_link_payment);

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
//                Call<currentDensity> call = jsonPlaceHolderApi.postNewCustomer();
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
