package com.atish.journeyjournal;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.atish.journeyjournal.DatabaseHelper.Journal;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView journallist;
    ProgressBar progressBar;

    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Journal, MainActivity.JournalViewHolder> journalAdapter;
    FirebaseUser user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        progressBar = findViewById(R.id.main_progess);


        Query query = fStore.collection("Journals").document(user.getUid()).collection("myJournals").orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Journal> allJournals = new FirestoreRecyclerOptions.Builder<Journal>()
                .setQuery(query, Journal.class)
                .build();


        journalAdapter = new FirestoreRecyclerAdapter<Journal, MainActivity.JournalViewHolder>(allJournals) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull MainActivity.JournalViewHolder journalViewHolder, @SuppressLint("RecyclerView") int i, @NonNull final Journal journal) {
                progressBar.setVisibility(View.GONE);
                journalViewHolder.journalTitle.setVisibility(View.GONE);
                journalViewHolder.journaldateandtime.setVisibility(View.GONE);
                Glide.with(journalViewHolder.imageView.getContext()).load(journal.getImage()).into(journalViewHolder.imageView);

            }

            @NonNull
            @Override
            public MainActivity.JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_view_layout, parent, false);
                return new MainActivity.JournalViewHolder(view);
            }
        };

        journallist = findViewById(R.id.journallist);


        journallist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        journallist.setAdapter(journalAdapter);

        FloatingActionButton addfab = findViewById(R.id.addFab);
        addfab.setVisibility(View.GONE);
    }


    public static class JournalViewHolder extends RecyclerView.ViewHolder {

        TextView journalTitle, journaldateandtime;
        ImageView imageView;
        View view;
        CardView mCardView;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            journalTitle = itemView.findViewById(R.id.titles);
            journaldateandtime = itemView.findViewById(R.id.dateandtimeview);
            imageView = itemView.findViewById(R.id.imageView);
            mCardView = itemView.findViewById(R.id.journalCard);
            view = itemView;
        }
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.pink1);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.red);
        colorCode.add(R.color.blue);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.notgreen);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }

    protected void onStart() {
        super.onStart();
        journalAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (journalAdapter != null) {
            journalAdapter.startListening();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
