package com.timi.qrcodeapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {


    public void onClick(View view) {
        scan(0, new ScanQRCodeResultListener() {
            @Override
            public void scanSuccess(int requestCode, String result) {
                Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initBundle(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
