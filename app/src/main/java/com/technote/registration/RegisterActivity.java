  package com.technote.registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.technote.services.EnrollClient;
import com.technote.services.EnrollInterface;
import com.technote.services.models.Member;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.member_image) ImageView mMemberImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.front_image) ImageView mFrontImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.back_image) ImageView mBackImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sign_image) ImageView mSignImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title_type) Spinner mTitleType;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.identification) EditText mIdentification;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.id_type) Spinner mIdentificationType;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.first_name) EditText mFirstName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.middle_name) EditText mMiddleName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.surname) EditText mSurname;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.phone_number) EditText mPhone;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.email_address) EditText mEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.gender) Spinner mGender;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.postal_address) EditText mPostalAddress;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.date_birth) EditText mDateBirth;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.add_customer) Button mAddButton;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.capture_profile) Button mCaptureProfile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.capture_front) Button mCaptureFront;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.capture_back) Button mCaptureBack;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.capture_sign) Button mCaptureSign;
    DatePickerDialog datePickerDialog;
    ProgressDialog progressDialog;
    public  static final int RequestPermissionCode  = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        get_gender();
        get_identification();
        get_title();
        EnableRuntimePermission();
        mDateBirth.setOnClickListener(this);
        mCaptureProfile.setOnClickListener(this);
        mCaptureFront.setOnClickListener(this);
        mCaptureBack.setOnClickListener(this);
        mCaptureSign.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        createAuthDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0 :
                if (resultCode == RESULT_OK){
                    assert data != null;
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mMemberImage.setImageBitmap(imageBitmap);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK){
                    assert data != null;
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mFrontImage.setImageBitmap(imageBitmap);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK){
                    assert data != null;
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mBackImage.setImageBitmap(imageBitmap);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK){
                    assert data != null;
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mSignImage.setImageBitmap(imageBitmap);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mDateBirth){
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            mDateBirth.setText(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if (v == mCaptureProfile){
            Toast.makeText(this,"Initiating face capture",Toast.LENGTH_SHORT).show();
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);//zero can be replaced with any action code
        }
        else if (v == mCaptureFront){
            Toast.makeText(this,"Initiating front document capture",Toast.LENGTH_SHORT).show();
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 1);//zero can be replaced with any action code
        }
        else if (v == mCaptureBack){
            Toast.makeText(this,"Initiating back document capture",Toast.LENGTH_SHORT).show();
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 2);//zero can be replaced with any action code
        }
        else if (v == mCaptureSign){
            Toast.makeText(this,"Initiating signature capture",Toast.LENGTH_SHORT).show();
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 3);//zero can be replaced with any action code
        }
        else if (v == mAddButton){
            send_data();
        }
    }

    private void get_gender(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGender.setAdapter(adapter);
    }
    private void get_title(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.title_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleType.setAdapter(adapter);
    }
    private void get_identification(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.identification_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIdentificationType.setAdapter(adapter);
    }

    private Bitmap get_bitmap(ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

    private void send_data(){
        byte[] profile_bytes = getImageByte(mMemberImage);
        RequestBody profile_body = RequestBody.create(MediaType.parse("image/jpeg"),profile_bytes);
        MultipartBody.Part profile_part = MultipartBody.Part.createFormData("member_image","image.jpg",profile_body);
        byte[] sign_bytes = getImageByte(mSignImage);
        RequestBody sign_body = RequestBody.create(MediaType.parse("image/jpeg"),sign_bytes);
        MultipartBody.Part sign_part = MultipartBody.Part.createFormData("sign_image","image.jpg",sign_body);
        String id_type = (String) mIdentificationType.getSelectedItem();
        RequestBody id_type_body = RequestBody.create(MediaType.parse("text/plain"),id_type);
        String id_number = mIdentification.getText().toString();
        RequestBody id_number_body = RequestBody.create(MediaType.parse("text/plain"),id_number);
        String title = (String) mTitleType.getSelectedItem();
        RequestBody title_body = RequestBody.create(MediaType.parse("text/plain"),title);
        String first_name = mFirstName.getText().toString();
        RequestBody first_body = RequestBody.create(MediaType.parse("text/plain"),first_name);
        String middleName = mMiddleName.getText().toString();
        RequestBody middle_body = RequestBody.create(MediaType.parse("text/plain"),middleName);
        String surname = mSurname.getText().toString();
        RequestBody surname_body = RequestBody.create(MediaType.parse("text/plain"),surname);
        String gender = mGender.getSelectedItem().toString();
        RequestBody gender_body = RequestBody.create(MediaType.parse("text/plain"),gender);
        String date_birth = mDateBirth.getText().toString();
        RequestBody birthday_body = RequestBody.create(MediaType.parse("text/plain"),date_birth);
        String phone_number = mPhone.getText().toString();
        RequestBody phone_body = RequestBody.create(MediaType.parse("text/plain"),phone_number);
        String email_address = mEmail.getText().toString();
        RequestBody email_body = RequestBody.create(MediaType.parse("text/plain"),email_address);
        String postal_address = mPostalAddress.getText().toString();
        RequestBody postal_body = RequestBody.create(MediaType.parse("text/plain"),postal_address);
        Intent intent = getIntent();
        String added_by = intent.getStringExtra("full_name");
        RequestBody added_body = RequestBody.create(MediaType.parse("text/plain"),added_by);
        byte[] front_bytes = getImageByte(mFrontImage);
        RequestBody front_body = RequestBody.create(MediaType.parse("image/jpeg"),front_bytes);
        MultipartBody.Part front_part = MultipartBody.Part.createFormData("front_image","image.jpg",front_body);
        byte[] back_bytes = getImageByte(mBackImage);
        RequestBody back_body = RequestBody.create(MediaType.parse("image/jpeg"),back_bytes);
        MultipartBody.Part back_part = MultipartBody.Part.createFormData("back_image","image.jpg",back_body);
        progressDialog.show();
        EnrollInterface client = EnrollClient.getClient();
        Call<Member> memberCall = client.new_member(profile_part,id_type_body,id_number_body,title_body,first_body
                ,middle_body,surname_body,gender_body,birthday_body,email_body,phone_body,added_body,postal_body,front_part,back_part,sign_part);
        memberCall.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(@NotNull Call<Member> call, @NotNull Response<Member> response) {
                progressDialog.hide();
                if (response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Member added successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Try again later",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(RegisterActivity.this,"Registration not successful",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAuthDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Creating new member");
        progressDialog.setCancelable(false);
    }


    private byte[] getImageByte(ImageView mImage){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable) mImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        return outputStream.toByteArray();
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(RegisterActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(RegisterActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(RegisterActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}