package com.technote.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.technote.registration.hashing.Security;
import com.technote.services.Constants;
import com.technote.services.EnrollClient;
import com.technote.services.EnrollInterface;
import com.technote.services.models.Agent;

import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_name) EditText mRegisterName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_email) EditText mRegisterEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_location) EditText mRegisterLocation;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_password) EditText mRegisterPassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.confirm_password) EditText mConfirmPassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_activity) TextView mLoginText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_text) FloatingActionButton mRegisterButton;
    private ProgressDialog mAuthProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        mLoginText.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        createAuthProgressDialog();
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginText){
            Intent intent = new Intent(AddActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else if (v == mRegisterButton){
          String full_name = mRegisterName.getText().toString();
          String email_address = mRegisterEmail.getText().toString();
          String location = mRegisterLocation.getText().toString();
          String password = mRegisterPassword.getText().toString();
          String confirm_password = mConfirmPassword.getText().toString();
          boolean validEmail = isValidEmail(email_address);
          boolean validPassword = isValidPassword(password,confirm_password);if (!validEmail || !validPassword) return;
            try {
                password = Security.getHashPassword(password, Constants.password_salt.getBytes());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            add_agent(full_name,location,email_address,password);
        }
    }
    private void add_agent(final String full_name, String location, String email_address, String password){
        mAuthProgress.show();
        EnrollInterface client = EnrollClient.getClient();
        Call<Agent> call = client.new_agent(full_name, email_address, location, password);
        call.enqueue(new Callback<Agent>() {
            @Override
            public void onResponse(@NotNull Call<Agent> call, @NotNull Response<Agent> response) {
                mAuthProgress.hide();
                if (response.isSuccessful()){
                    Toast.makeText(AddActivity.this,"Welcome " + full_name + " ;",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AddActivity.this,MainActivity.class);
                    i.putExtra("full_name",full_name);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(AddActivity.this,"Manager not added",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Agent> call, @NotNull Throwable t) {
                mAuthProgress.hide();
                Toast.makeText(AddActivity.this,"Your internet is not stable,try again later",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isValidEmail(String email){
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if(!isGoodEmail){
            mRegisterEmail.setError("Please enter a valid email Address");
            return false;
        }
        return true;
    }
    private boolean isValidPassword(String Password,String confirmPassword){
        if (Password.length() < 6) {
            mRegisterPassword.setError("The password should be at least 6 Characters");
            return false;

        }else if(!Password.equals(confirmPassword)){
            mRegisterPassword.setError("Passwords don't match");
            return false;
        }
        return true;
    }

    private void createAuthProgressDialog(){
        mAuthProgress  = new ProgressDialog(this);
        mAuthProgress.setTitle("Loading...");
        mAuthProgress.setMessage("Creating your account....");
        mAuthProgress.setCancelable(false);
    }
}