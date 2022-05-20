package com.atish.journeyjournal.Journal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.atish.journeyjournal.R;
import com.bumptech.glide.Glide;

public class FullScreenImage extends AppCompatActivity {

    Intent data;
    ImageView imageFullssView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent();

        imageFullssView = findViewById(R.id.fullImage);

        String image = data.getStringExtra("image");

        Glide.with(imageFullssView.getContext()).load(data.getStringExtra("image")).into(imageFullssView);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}