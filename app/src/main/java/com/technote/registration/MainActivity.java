package com.technote.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.add_activity) Button mAddButton;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.search_activity) Button mSearchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAddButton.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton){
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
            finish();
        } else if (v == mSearchButton){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            finish();
        }
    }
}