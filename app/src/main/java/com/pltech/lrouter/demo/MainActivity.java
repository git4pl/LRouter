package com.pltech.lrouter.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.pltech.lrouter.LRouter;
import com.pltech.lrouter_annotation.Route;

@Route(path = "/main/home")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LRouter.init(this);
    }

    public void jumpToTest(View view) {
        LRouter.INSTANCE.jumpTo("/test/module1/test", null);
    }
}