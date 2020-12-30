package com.pltech.test_module_one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.pltech.lrouter.LRouter;
import com.pltech.lrouter_annotation.Route;

@Route(path = "/test/module1/test")
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_test);
    }

    public void testJumpToApp(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("bundle_key", "Hello, I come from test module, and now in app module.");
        LRouter.INSTANCE.jumpTo("/main/test", bundle);
    }
}