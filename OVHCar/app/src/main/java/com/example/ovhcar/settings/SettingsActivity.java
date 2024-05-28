package com.example.ovhcar.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.ovhcar.R;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout preorder, testDrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);


        changeStatusBarColor();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        testDrive = findViewById(R.id.testDrive);
        preorder = findViewById(R.id.preorder);


        preorder.setOnClickListener(_preorder);
        testDrive.setOnClickListener(_testDrive);

    }

    View.OnClickListener _testDrive = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(SettingsActivity.this, TestDrive.class);
            startActivity(intent);

        }
    };


    View.OnClickListener _preorder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(SettingsActivity.this, PreOrder.class);
            startActivity(intent);

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }

}