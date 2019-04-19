package com.example.vcanteen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcanteen.POJO.availablePaymentMethod;
import com.example.vcanteen.POJO.paymentMethod;
import com.example.vcanteen.POJO.pickupSlot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditPaymentMethodActivity extends AppCompatActivity {
    ArrayList<availablePaymentMethod> payments = new ArrayList<>();
    TextView sp_cunex_link,sp_scbeasy_link, sp_kplus_link,sp_truemoneywallet_link;
    ImageView sp_cunex_checkmark, sp_scbeasy_checkmark, sp_kplus_checkmark, sp_truemoneywallet_checkmark;
    Dialog dialog;
    TextView serviceProviderName;
    SharedPreferences sharedPref;
    ProgressDialog progressDialog;
    int cunexId, scbEasyId, kplusId, trueMoneyWalletId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_method);
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        sp_cunex_link = findViewById(R.id.sp_cunex_link);
        sp_scbeasy_link = findViewById(R.id.sp_scbeasy_link);
        sp_kplus_link = findViewById(R.id.sp_kplus_link);
        sp_truemoneywallet_link = findViewById(R.id.sp_truemoneywallet_link);

        sp_cunex_checkmark = findViewById(R.id.sp_cunex_checkmark);
        sp_scbeasy_checkmark = findViewById(R.id.sp_scbeasy_checkmark);
        sp_kplus_checkmark = findViewById(R.id.sp_kplus_checkmark);
        sp_truemoneywallet_checkmark = findViewById(R.id.sp_truemoneywallet_checkmark);



        //TODO getExtra from setting page

        payments = getIntent().getParcelableArrayListExtra( "availablePaymentMethodList"); //IMPORTANT
        System.out.println("SP: "+payments.get(0).serviceProvider);
        System.out.println("SP: "+payments.get(1).serviceProvider);

        mapping(payments);
        //successfully intent the object

        sp_cunex_link.setOnClickListener(v ->  {
            if(sp_cunex_link.getText().equals("LINK")) {
                //link cunex
                showLinkPopup("CU_NEX");
            } else if(sp_cunex_link.getText().equals("unlink")) {
                //unlink cunex
                System.out.println("triggered cunex unlink");
                showUnlinkPopup("CUNEX", cunexId);
            }
        });

        sp_scbeasy_link.setOnClickListener(v ->  {
            if(sp_scbeasy_link.getText().equals("LINK")) {
                //link scbeasy
                showLinkPopup("SCB_EASY");
            } else if(sp_scbeasy_link.getText().equals("unlink")) {
                //unlink scbeasy
                showUnlinkPopup("SCB_EASY",scbEasyId);
            }
        });

        sp_kplus_link.setOnClickListener(v ->  {
            if(sp_kplus_link.getText().equals("LINK")) {
                //link kplus
                showLinkPopup("K_PLUS");
            } else if(sp_kplus_link.getText().equals("unlink")) {
                //unlink kplus
                showUnlinkPopup("K_PLUS",kplusId);
            }
        });

        sp_truemoneywallet_link.setOnClickListener(v ->  {
            if(sp_truemoneywallet_link.getText().equals("LINK")) {
                //link truemoneywallet
                showLinkPopup("TRUEMONEY_WALLET");
            } else if(sp_truemoneywallet_link.getText().equals("unlink")) {
                //unlink truemoney wallet
                showUnlinkPopup("TRUEMONEY_WALLET",trueMoneyWalletId);
            }
        });
    }

    private void showUnlinkPopup(String serviceProvider, int customerMoneyAccountId) {
        dialog = new Dialog(EditPaymentMethodActivity.this);
        dialog.setContentView(R.layout.popup_payment_unlink);
        TextView description = dialog.findViewById(R.id.popup_payment_unlink_description);
        TextView dismissBtn = dialog.findViewById(R.id.dismiss_btn);
        Button unlinkBtn = dialog.findViewById(R.id.unlink_btn);
        String shownServiceProviderName = "";
        if(serviceProvider.equals("CU_NEX")) {
            shownServiceProviderName = "CU NEX";
        }
        if(serviceProvider.equals("SCB_EASY")) {
            shownServiceProviderName = "SCB EASY";
        }
        if(serviceProvider.equals("K_PLUS")) {
            shownServiceProviderName = "K PLUS";
        }
        if(serviceProvider.equals("TRUEMONEY_WALLET")) {
            shownServiceProviderName = "TrueMoney Wallet";
        }
        description.setText("Do you wish to unlink "+shownServiceProviderName+"\n from your account?");

        dismissBtn.setOnClickListener(v -> dialog.dismiss());

        unlinkBtn.setOnClickListener(v -> {
            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl("http://vcanteen.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit2.create(JsonPlaceHolderApi.class);
            Call<Void> call =  jsonPlaceHolderApi.deleteUnlinkPayment(sharedPref.getInt("customerId",0),customerMoneyAccountId );
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(EditPaymentMethodActivity.this, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                        System.out.println("onresponse fail");
                        dialog.dismiss(); //might need to delete
                        return;
                    }
                    System.out.println("onResponse done");
                    reload();
                    progressDialog.dismiss();
                    dialog.dismiss();
                    Toast.makeText(EditPaymentMethodActivity.this, "Successfully Unlinked Account", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("onfailure delete");
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    private void showLinkPopup(String serviceProvider) {

        dialog = new Dialog(EditPaymentMethodActivity.this);
        dialog.setContentView(R.layout.popup_payment_link);
        serviceProviderName = dialog.findViewById(R.id.serviceProviderName);
        EditText accountNumber = dialog.findViewById(R.id.accountNumber);
        TextView inline = dialog.findViewById(R.id.inline_error);
        ImageView close_x = dialog.findViewById(R.id.close_x);
        dialog.setCancelable(true);

        String shownServiceProviderName = "";
        if(serviceProvider.equals("CU_NEX")) {
            shownServiceProviderName = "CU NEX";
        }
        if(serviceProvider.equals("SCB_EASY")) {
            shownServiceProviderName = "SCB EASY";
        }
        if(serviceProvider.equals("K_PLUS")) {
            shownServiceProviderName = "K PLUS";
        }
        if(serviceProvider.equals("TRUEMONEY_WALLET")) {
            shownServiceProviderName = "TrueMoney Wallet";
        }

        serviceProviderName.setText(shownServiceProviderName);

        close_x.setOnClickListener(v -> dialog.dismiss());

        (dialog.findViewById(R.id.link_btn))
                .setOnClickListener(v -> {
                    if(accountNumber.getText().toString().trim().equals("")) {
                        inline.setVisibility(View.VISIBLE);
                        return;
                    }
                    progressDialog = new ProgressDialog(EditPaymentMethodActivity.this);
                    progressDialog = ProgressDialog.show(EditPaymentMethodActivity.this
                            , "",
                            "Loading. Please wait...", true);
                    //Retrofit
                    Retrofit retrofit2 = new Retrofit.Builder()
                            .baseUrl("http://vcanteen.herokuapp.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JsonPlaceHolderApi jsonPlaceHolderApi = retrofit2.create(JsonPlaceHolderApi.class);
                    Call<Void> call =  jsonPlaceHolderApi.postLinkPayment(sharedPref.getInt("customerId",0), serviceProvider, accountNumber.getText().toString());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(!response.isSuccessful()) {
                                Toast.makeText(EditPaymentMethodActivity.this, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                dialog.dismiss();
                                reload();
                                return;
                            }
                            Toast.makeText(EditPaymentMethodActivity.this, "Successfully Linked Account ", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            progressDialog.dismiss();
                            dialog.dismiss();
                        }
                    });
                });

        dialog.show();

        accountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0) {
                    inline.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void reload() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<paymentMethod> call =  jsonPlaceHolderApi.getPaymentMethod(sharedPref.getInt("customerId",0));

        call.enqueue(new Callback<paymentMethod>() {
            @Override
            public void onResponse(Call<paymentMethod> call, Response<paymentMethod> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(EditPaymentMethodActivity.this, "CODE: "+response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                paymentMethod methods = response.body(); //IMPORTANT
                ArrayList<availablePaymentMethod> lists = methods.availablePaymentMethod; //IMPORTANT
                for (availablePaymentMethod list :lists){ //just to print
                    System.out.println("payment");
                    System.out.println(list.getCustomerMoneyAccountId()+","+list.getServiceProvider());
                }
                mapping(lists);
                Toast.makeText(EditPaymentMethodActivity.this, "Reloaded",
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<paymentMethod> call, Throwable t) {

            }
        });
    }
    public void mapping(ArrayList<availablePaymentMethod> payments) {
        for (availablePaymentMethod list :payments){
            System.out.println("received payment");
            System.out.println(list.getCustomerMoneyAccountId()+","+list.getServiceProvider());

            if(list.getServiceProvider().equals("CU_NEX")) {
                sp_cunex_link.setText("unlink");
                sp_cunex_link.setPaintFlags(sp_cunex_link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                sp_cunex_checkmark.setVisibility(View.VISIBLE);
                cunexId = list.getCustomerMoneyAccountId();
            }
            if(list.getServiceProvider().equals("SCB_EASY")) {
                sp_scbeasy_link.setText("unlink");
                sp_scbeasy_link.setPaintFlags(sp_scbeasy_link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                sp_scbeasy_checkmark.setVisibility(View.VISIBLE);
                scbEasyId = list.getCustomerMoneyAccountId();
            }
            if(list.getServiceProvider().equals("K_PLUS")) {
                sp_kplus_link.setText("unlink");
                sp_kplus_link.setPaintFlags(sp_kplus_link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                sp_kplus_checkmark.setVisibility(View.VISIBLE);
                kplusId = list.getCustomerMoneyAccountId();
            }
            if(list.getServiceProvider().equals("TRUEMONEY_WALLET")) {
                sp_truemoneywallet_link.setText("unlink");
                sp_truemoneywallet_link.setPaintFlags(sp_truemoneywallet_link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                sp_truemoneywallet_checkmark.setVisibility(View.VISIBLE);
                trueMoneyWalletId = list.getCustomerMoneyAccountId();
            }

        }
    }
}
