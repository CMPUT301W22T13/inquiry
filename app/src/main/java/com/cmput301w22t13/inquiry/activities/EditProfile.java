package com.cmput301w22t13.inquiry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cmput301w22t13.inquiry.R;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Implement edittext here and change in firebase
        //assert its an alphabet? no maybe
        //assert total number of characters

        //tests
        //on confirm change username
        //show message(fragment) that name has been changed
    }
}