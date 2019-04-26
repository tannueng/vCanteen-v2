package com.example.vcanteen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class editProfilePageActivity extends AppCompatActivity {

    private static final Pattern NAME_PATTERN =
            Pattern.compile("[a-zA-Z][a-zA-Z ]+[a-zA-Z]$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^\\w.+@\\w+\\..{2,3}(.{1,})?$");

    private static final Pattern EMAIL_CHARACTER_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.]+\\..{2,3}(.{2,3})?$");

    private EditText firstNameBox, lastNameBox, emailBox;
    private TextView firstNameError, lastNameError, emailError;
    private Button saveAndExitButton, editProfileImageButton;
    private ImageView profilePicture;

    customerSingleton customerSingleton;

    Dialog confirmProfileChangeDialog;
    private Button cancelButton, confirmButton;

    RequestOptions option = new RequestOptions().centerCrop();

    static SharedPreferences sharedPref;
    int customerId;

    private static final int PICK_IMAGE_REQUEST = 1; //Can be any number
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    private String imageUrl;
    private Uri downloadUri;
    private ProgressDialog progressDialog;

    Context c;

    private String originalImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        customerId = sharedPref.getInt("customerId",0);

        customerSingleton = com.example.vcanteen.customerSingleton.getInstance();

        firstNameBox = findViewById(R.id.firstNameBox);
        lastNameBox = findViewById(R.id.lastNameBox);
        emailBox = findViewById(R.id.emailBox);
        firstNameError = findViewById(R.id.firstNameError);
        lastNameError = findViewById(R.id.lastNameError);
        emailError = findViewById(R.id.emailError);

        saveAndExitButton = findViewById(R.id.saveAndExitButton);
        editProfileImageButton = findViewById(R.id.editProfileImageButton);

        profilePicture = findViewById(R.id.profilePicture);

        firstNameBox.setText(""+customerSingleton.getFirstname());
        lastNameBox.setText(""+customerSingleton.getLastname());
        emailBox.setText(""+customerSingleton.getEmail());

        saveAndExitButton.setAlpha(0.4f);
        saveAndExitButton.setEnabled(false);

        if(customerSingleton.getCustomerImage()!=null){
            Glide.with(editProfilePageActivity.this)
                    .load(customerSingleton.getCustomerImage())
                    .apply(RequestOptions.circleCropTransform())//.apply(option)
                    .into(profilePicture);
            originalImageUrl = ""+customerSingleton.getCustomerImage();
            imageUrl = originalImageUrl;
        }

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        //แก้จาก upload เป็นอย่างอื่นเพื่อจะได้เก็บไว้คนละ folder ทั้ง 2 อันเลย และให้ชื่อเหมือนกัน

        firstNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(firstNameBox.getText().toString().equals(customerSingleton.getFirstname()))){
                    saveAndExitButton.setEnabled(true);
                    saveAndExitButton.setAlpha(1f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(lastNameBox.getText().toString().equals(customerSingleton.getLastname()))){
                    saveAndExitButton.setEnabled(true);
                    saveAndExitButton.setAlpha(1f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(emailBox.getText().toString().equals(customerSingleton.getEmail()))){
                    saveAndExitButton.setEnabled(true);
                    saveAndExitButton.setAlpha(1f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveAndExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Case 1 : If blank
                if(firstNameBox.getText().toString().length()==0 || lastNameBox.getText().toString().length()==0 || emailBox.getText().toString().length()==0){
                    if(firstNameBox.getText().toString().length()==0){
                        firstNameError.setVisibility(View.VISIBLE);
                        firstNameError.setText("This field cannot be blank.");
                    }else {
                        firstNameError.setVisibility(View.INVISIBLE);
                    }
                    if(lastNameBox.getText().toString().length()==0){
                        lastNameError.setVisibility(View.VISIBLE);
                        lastNameError.setText("This field cannot be blank.");
                    }else{
                        lastNameError.setVisibility(View.INVISIBLE);
                    }
                    if(emailBox.getText().toString().length()==0){
                        emailError.setVisibility(View.VISIBLE);
                        emailError.setText("This field cannot be blank.");
                    }else{
                        emailError.setVisibility(View.INVISIBLE);
                    }
                } // Case 2 : Wrong name format a-z, A-Z
                else if(!(NAME_PATTERN.matcher(firstNameBox.getText().toString()).matches()) || !(NAME_PATTERN.matcher(lastNameBox.getText().toString()).matches())){
                    emailError.setVisibility(View.INVISIBLE);
                    if(!(NAME_PATTERN.matcher(firstNameBox.getText().toString()).matches())){
                        firstNameError.setVisibility(View.VISIBLE);
                        firstNameError.setText("Name can only contains a-z, A-Z.");
                    }
                    if(!(NAME_PATTERN.matcher(lastNameBox.getText().toString()).matches())){
                        lastNameError.setVisibility(View.VISIBLE);
                        lastNameError.setText("Name can only contains a-z, A-Z.");
                    }
                } // Case 3 : Wrong email format
                else if(!(EMAIL_PATTERN.matcher(emailBox.getText().toString()).matches())){
                    emailError.setVisibility(View.VISIBLE);
                    emailError.setText("The email address is in invalid format.");
                    firstNameError.setVisibility(View.INVISIBLE);
                    lastNameError.setVisibility(View.INVISIBLE);
                } // Case 4 : Contains other characters than a-z, A-Z, 0-9, or a period in email
                else if(!(EMAIL_CHARACTER_PATTERN.matcher(emailBox.getText().toString()).matches())){
                    emailError.setVisibility(View.VISIBLE);
                    emailError.setText("Only a-z, A-Z, 0-9, -, _, or a period is allowed.");
                    firstNameError.setVisibility(View.INVISIBLE);
                    lastNameError.setVisibility(View.INVISIBLE);
                } // Case 5 : Not G-mail account
                else if (!emailBox.getText().toString().trim().toLowerCase().contains("@gmail.com")){
                    emailError.setVisibility(View.VISIBLE);
                    emailError.setText("Must be Gmail account only");
                    firstNameError.setVisibility(View.INVISIBLE);
                    lastNameError.setVisibility(View.INVISIBLE);
                } // BEST CASE : If all conditions are met
                else {
                    confirmProfileChangeDialog = new Dialog(editProfilePageActivity.this);
                    confirmProfileChangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    confirmProfileChangeDialog.setContentView(R.layout.confirm_profile_change_popup);

                    cancelButton = confirmProfileChangeDialog.findViewById(R.id.cancelButton);
                    confirmButton = confirmProfileChangeDialog.findViewById(R.id.confirmButton);

                    confirmProfileChangeDialog.show();

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmProfileChangeDialog.dismiss();
                        }
                    });

                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            uploadFile();
                            // call endpoint
                            callToUpdateCustomerProfile();

                        }
                    });

                }

            }
        });

        editProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //มีไว้คู่กับ open file chooser เฉยๆ
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();


            //Picasso.with(this).load(mImageUri).into(mImageView);
            Glide.with(this)
                    .load(mImageUri)
                    .apply(RequestOptions.circleCropTransform())//.apply(option)
                    .into(profilePicture);
            uploadFile();

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));  //สร้างที่ไฟล์ ชื่อจาก System.currentTimeMillis() เพื่อไม่ให้มันชื่อซ้ำ

            progressDialog = new ProgressDialog(editProfilePageActivity.this);
            progressDialog = ProgressDialog.show(editProfilePageActivity.this, "",
                    "Uploading ...", true);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) { // เมื่ออัพโหลดเสร็จ ทำอะไร

                                    progressDialog.dismiss();

                                    downloadUri = uri;
                                    imageUrl = downloadUri.toString(); //ได้ URL ของรูปนั้นมาเป็น string
                                    //selectedMenu.setFoodImg(imageUrl); //เอาไปเก็บไว้ เตรียมส่งให้ MySQL ด้วย retrofit
                                    //customerSingleton.setCustomerImage(imageUrl);
                                    System.out.println("URL of image :"+imageUrl);
                                    Upload upload = new Upload(firstNameBox.getText().toString().trim(), downloadUri.toString());
                                    //สร้าง class ชื่อ Upload ด้วย

                                    String uploadId = mDatabaseRef.push().getKey();

                                    mDatabaseRef.child(uploadId).setValue(upload);

                                    Toast.makeText(editProfilePageActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                                    // call endpoint?
                                    //callToUpdateProfileImage();

                                    saveAndExitButton.setEnabled(true);
                                    saveAndExitButton.setAlpha(1f);

                                    //bitmap = getBitmapFromURL(vendorSingleton.getVendorImage());
                                    // vendorProfilePicture.setImageBitmap(bitmap);

                                    progressDialog.dismiss();

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { // อัพไม่สำเร็จ ทำอะไร
                            System.out.println("Fail to upload photo");
                            progressDialog.dismiss();
                            Toast.makeText(editProfilePageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) { //ระหว่างกำลังอัพ ทำอะไร
                            System.out.println("Try to upload image");
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            imageUrl = originalImageUrl;
        }
    }

    public void callToUpdateCustomerProfile(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vcanteen.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Void> callToEditProfileImage = jsonPlaceHolderApi.updateCustomerProfile(customerId, firstNameBox.getText().toString(),lastNameBox.getText().toString(),emailBox.getText().toString(),imageUrl);

        callToEditProfileImage.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(editProfilePageActivity.this, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                    System.out.println("CODE: "+response.code());
                    return;
                }

                if(response.code()==200) {

                    customerSingleton.setFirstname(firstNameBox.getText().toString());
                    customerSingleton.setLastname(lastNameBox.getText().toString());
                    customerSingleton.setEmail(emailBox.getText().toString());
                    customerSingleton.setCustomerImage(imageUrl+"");

                    Intent intent = new Intent(editProfilePageActivity.this, profilePageActivity.class);
                    //((profilePageActivity) c).updateProfile();
                    //Glide.with(SettingsActivity.this).load(vendorSingleton.getVendorImage()).apply(option).into(vendorProfilePicture);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Error in updating customer profile profile");
            }
        });
    }

}
