 package com.technote.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_email) EditText mLoginEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_password) EditText mLoginPassword;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_activity) TextView mLoginText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_button) FloatingActionButton mLoginButton;
    private ProgressDialog mAuthProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginText.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        createAuthProgressDialog();
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginText){
            Intent intent = new Intent(LoginActivity.this,AddActivity.class);
            startActivity(intent);
            finish();
        }else if (v == mLoginButton){
            String email_address = mLoginEmail.getText().toString();
            byte[] salt = Constants.password_salt.getBytes();
            String password = mLoginPassword.getText().toString();
            try {
                password = Security.getHashPassword(password, salt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (email_address.equals("")){
                mLoginEmail.setError("Please enter your Email address");
                return;
            }
            if (password.equals("")){
                mLoginPassword.setError("Please enter your password");
                return;
            }
            get_manager(email_address,password);
        }
    }
    private void get_manager(String email_address,String password){
        mAuthProgress.show();
        EnrollInterface client = EnrollClient.getClient();
        Call<List<Agent>> call = client.get_agent(email_address,password);
        call.enqueue(new Callback<List<Agent>>() {
            @Override
            public void onResponse(@NotNull Call<List<Agent>> call, @NotNull Response<List<Agent>> response) {
                mAuthProgress.hide();
                if (response.isSuccessful()){
                    List<Agent> agents = response.body();
                    assert agents != null;
                    Agent agent = agents.get(0);
                    Toast.makeText(LoginActivity.this,"Welcome back " + agent.getUsername() + " ;",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.putExtra("full_name",agent.getUsername());
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Agent>> call, @NotNull Throwable t) {
                mAuthProgress.hide();
                Toast.makeText(LoginActivity.this,"Your internet is not stable,try again later",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAuthProgressDialog(){
        mAuthProgress  = new ProgressDialog(this);
        mAuthProgress.setTitle("Loading...");
        mAuthProgress.setMessage("loading your account....");
        mAuthProgress.setCancelable(false);
    }
}