package com.lyentech.npdemo;

import android.os.Bundle;
import android.view.View;

import com.lyentech.np.NpServer;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        findViewById(R.id.btnEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject js = new JSONObject();
                try {
                    js.put("id", 3);
                    js.put("name", "tom");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NpServer.postEvent("video_source", js.toString());
                NpServer.postEvent("video_source", "1");
            }
        });
    }
}