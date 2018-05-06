package com.lx.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchKeyActivity extends AppCompatActivity {

    private EditText keyInput;
    private ListView plantListView;
    private TextView btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_key);

        initView();
    }

    private void initView(){
        btnCancel = findViewById(R.id.btn_cancel);
        keyInput = findViewById(R.id.key_input);
        plantListView = findViewById(R.id.plant_list_view);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
