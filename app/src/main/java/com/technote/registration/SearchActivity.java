package com.technote.registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;
import com.technote.services.Constants;
import com.technote.services.EnrollClient;
import com.technote.services.EnrollInterface;
import com.technote.services.models.Member;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity{
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_image) ImageView mDisplayImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_id) TextView mDisplayId;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_name) TextView mDisplayName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_gender) TextView mDisplayGender;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_birth) TextView mDisplayBirth;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_email) TextView mDisplayEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_phone) TextView mDisplayPhone;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.display_address) TextView mDisplayAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan the QRcode");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();
        ButterKnife.bind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                Toast.makeText(this,"Scan failed",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Scan successful",Toast.LENGTH_SHORT).show();
                String scan_result = intentResult.getContents();
                EnrollInterface client = EnrollClient.getClient();
                Call<List<Member>> call = client.search_member(scan_result);
                call.enqueue(
                        new Callback<List<Member>>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(@NotNull Call<List<Member>> call, @NotNull Response<List<Member>> response) {
                                List<Member> members = response.body();
                                Member member = members.get(0);
                                Picasso.get().load(Constants.image_url + member.getMemberImage()).into(mDisplayImage);
                                mDisplayId.setText("ID number : " + member.getIdNumber());
                                mDisplayName.setText("Full name: " + member.getFirstName()  + " " + member.getMiddleName() + " " + member.getSurname());
                                mDisplayGender.setText("Gender : " + member.getGender());
                                mDisplayEmail.setText("Email address : " + member.getEmailAddress());
                                mDisplayPhone.setText("Phone  : " + member.getPhoneNumber());
                                mDisplayBirth.setText("Birthday : " + member.getBirthday());
                                mDisplayAddress.setText("Physical address : " + member.getPhysicalAddress());
                            }

                            @Override
                            public void onFailure(@NotNull Call<List<Member>> call, @NotNull Throwable t) {

                            }
                        }
                );
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}