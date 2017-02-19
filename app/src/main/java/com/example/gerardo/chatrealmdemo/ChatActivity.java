package com.example.gerardo.chatrealmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle b = getIntent().getExtras();
        int idCanal = 0;
        if (b.getInt("id_canal") != 0){
            idCanal = b.getInt("id_canal");
            Log.d("CANAL",idCanal+"");
        }

    }
}
