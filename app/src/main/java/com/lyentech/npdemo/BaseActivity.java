package com.lyentech.npdemo;

import androidx.appcompat.app.AppCompatActivity;

import com.lyentech.np.NpServer;

abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        NpServer.stayOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NpServer.stayOnPause(this);
    }
}
