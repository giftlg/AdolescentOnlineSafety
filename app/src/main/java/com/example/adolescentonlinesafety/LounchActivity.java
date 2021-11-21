package com.example.adolescentonlinesafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.adolescentonlinesafety.prevalent.Prevalent;

import io.paperdb.Paper;

public class LounchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (!Configuration.FirstEmailKey.contains("@gmail.com")) {
            intent = new Intent(LounchActivity.this, MainActivity.class);
        } else {
            intent = new Intent(LounchActivity.this, LoggedInActivity.class);
        }
        startActivity(intent);
        finish();
// note we never called setContentView()
    }


}