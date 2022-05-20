package com.atish.journeyjournal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.atish.journeyjournal.Auth.RegisterActivity;
import com.atish.journeyjournal.DatabaseHelper.Journal;
import com.atish.journeyjournal.Journal.AddJournal;
import com.atish.journeyjournal.Journal.EditJournal;
import com.atish.journeyjournal.Journal.JournalDetails;
import com.atish.journeyjournal.Others.AboutUsActivity;
import com.atish.journeyjournal.Others.AppLinkActivity;
import com.atish.journeyjournal.Others.PrivacyPolicyActivity;
import com.atish.journeyjournal.Others.VersionActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView nav_view;
    RecyclerView journallist;
    ProgressBar progressBar;

    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Journal, JournalViewHolder> journalAdapter;
    FirebaseUser user;
    FirebaseAuth fAuth;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        progressBar = findViewById(R.id.main_progess);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        Query query = fStore.collection("Journals").document(user.getUid()).collection("myJournals").orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Journal> allJournals = new FirestoreRecyclerOptions.Builder<Journal>()
                .setQuery(query, Journal.class)
                .build();

        journalAdapter = new FirestoreRecyclerAdapter<Journal, JournalViewHolder>(allJournals) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull JournalViewHolder journalViewHolder, @SuppressLint("RecyclerView") int i, @NonNull final Journal journal) {
                progressBar.setVisibility(View.GONE);
                journalViewHolder.journalTitle.setText(journal.getTitle());
                journalViewHolder.journaldateandtime.setText(journal.getDatetime());
                Glide.with(journalViewHolder.imageView.getContext()).load(journal.getImage()).into(journalViewHolder.imageView);

                journalViewHolder.mCardView.setCardBackgroundColor(journalViewHolder.view.getResources().getColor(getRandomColor(), null));
                String docId = journalAdapter.getSnapshots().getSnapshot(i).getId();

                journalViewHolder.view.setOnClickListener(view -> {
                    Intent i1 = new Intent(view.getContext(), JournalDetails.class);
                    i1.putExtra("title", journal.getTitle());
                    i1.putExtra("datetime", journal.getDatetime());
                    i1.putExtra("content", journal.getAboutj());
                    i1.putExtra("location", journal.getLocation());
                    i1.putExtra("image", journal.getImage());
                    i1.putExtra("journalID", docId);
                    view.getContext().startActivity(i1);
                });

                ImageView menuIcon = journalViewHolder.view.findViewById(R.id.menuIcon);
                menuIcon.setOnClickListener(v -> {
                    final String docId1 = journalAdapter.getSnapshots().getSnapshot(i).getId();
                    PopupMenu menu = new PopupMenu(v.getContext(), v);
                    menu.setGravity(Gravity.END);
                    menu.getMenu().add("Edit").setOnMenuItemClickListener(item -> {
                        Intent i12 = new Intent(v.getContext(), EditJournal.class);
                        i12.putExtra("title", journal.getTitle());
                        i12.putExtra("content", journal.getAboutj());
                        i12.putExtra("location", journal.getLocation());
                        i12.putExtra("image", journal.getImage());
                        i12.putExtra("journalID", docId1);
                        startActivity(i12);
                        return false;
                    });

                    menu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Delete Journal");
                        dialog.setMessage("Are you sure?");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DocumentReference docRef = fStore.collection("Journals").document(user.getUid()).collection("myJournals").document(docId);
                                docRef.delete().addOnSuccessListener(unused -> Toast.makeText(MainActivity.this, "Journal Deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
                            }
                        });
                        dialog.setNegativeButton("Cancel", null);
                        dialog.setCancelable(false);
                        dialog.show();
                        return false;
                    });
                    menu.show();
                });
            }

            @NonNull
            @Override
            public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_view_layout, parent, false);
                return new JournalViewHolder(view);
            }
        };


        journallist = findViewById(R.id.journallist);

        drawerLayout = findViewById(R.id.drawer);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        journallist.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        journallist.setAdapter(journalAdapter);

        View headerView = nav_view.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.fullname);
        TextView userEmail = headerView.findViewById(R.id.emailview);
        userEmail.setText(user.getEmail());
        username.setText(user.getDisplayName());

        FloatingActionButton addfab = findViewById(R.id.addFab);
        addfab.setOnClickListener(view -> startActivity(new Intent(view.getContext(), AddJournal.class)));
    }



    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.nav_addnew:
                startActivity(new Intent(this, AddJournal.class));
                break;

            case R.id.nav_gallery:
                startActivity(new Intent(this, GalleryActivity.class));
                break;

            case R.id.nav_graph:
                startActivity(new Intent(this, GraphActivity.class));
                break;

            case R.id.nav_map:
                startActivity(new Intent(this, MapActivity.class));
                break;

            case R.id.nav_signout:
                showAlertDialog();
                break;

            case R.id.nav_aboutus:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;

            case R.id.nav_privacypolicy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;

            case R.id.nav_applink:
                startActivity(new Intent(this, AppLinkActivity.class));
                break;

            case R.id.nav_version:
                startActivity(new Intent(this, VersionActivity.class));
                break;

            default:
                Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show();
        }

        return false;
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

    private void showAlertDialog() {
        drawerLayout.closeDrawer(GravityCompat.START);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SignOut");
        dialog.setMessage("Are you sure?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                sharedPreferences.edit().putBoolean("rememberme", false).commit();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.setCancelable(false);
        dialog.show();
    }

}