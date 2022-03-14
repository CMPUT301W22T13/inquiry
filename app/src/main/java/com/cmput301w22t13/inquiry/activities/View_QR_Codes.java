package com.cmput301w22t13.inquiry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmput301w22t13.inquiry.R;

import java.util.ArrayList;
import java.util.List;

public class View_QR_Codes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr_codes);
        LinearLayout layout = (LinearLayout) findViewById(R.id.ViewQR);

        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        for (int i = 0; i < list.size(); i++) {
            TextView text = new TextView(this);
            text.setText(list.get(i));
            layout.addView(text);

        }

    }
}