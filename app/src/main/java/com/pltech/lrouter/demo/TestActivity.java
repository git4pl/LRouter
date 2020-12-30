package com.pltech.lrouter.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.pltech.lrouter_annotation.Route;

@Route(path = "/main/test")
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView tips = findViewById(R.id.tv_app_module_tips);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tips.setText(bundle.getString("bundle_key"));
        }
    }
}