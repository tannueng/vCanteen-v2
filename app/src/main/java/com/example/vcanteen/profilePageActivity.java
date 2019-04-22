package com.example.vcanteen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;

public class profilePageActivity extends AppCompatActivity {

    private Button editProfileButton;
    private ImageView profilePictureImage;
    private TextView name, email;
    customerSingleton customerSingleton;
    RequestOptions option = new RequestOptions().centerCrop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        customerSingleton = com.example.vcanteen.customerSingleton.getInstance();

        profilePictureImage = findViewById(R.id.profilePictureImage);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.Email);

        name.setText("" + customerSingleton.getFirstname() + " " + customerSingleton.getLastname());
        email.setText(""+customerSingleton.getEmail());

        Glide.with(profilePageActivity.this).load(customerSingleton.getCustomerImage()).apply(option).into(profilePictureImage);

        editProfileButton = findViewById(R.id.editProfileButton);

        editProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(profilePageActivity.this, editProfilePageActivity.class));
        });

    }
}
