package com.example.klikzdaj;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Klasa8Activity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Dialog loadingDialog;

    private TextView mTextView;
    private RecyclerView recyclerView;
    private List<CategoryModel> list;
    public static String category;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_klasa1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.klasa_8);
        getSupportActionBar().setTitle(null);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_klasa1){
                    startActivity(new Intent( Klasa8Activity.this, MainActivity.class));
                }
                if(item.getItemId() == R.id.nav_klasa2){
                    startActivity(new Intent( Klasa8Activity.this, Klasa2Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa3){
                    startActivity(new Intent( Klasa8Activity.this, Klasa3Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa4){
                    startActivity(new Intent( Klasa8Activity.this, Klasa4Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa5){
                    startActivity(new Intent( Klasa8Activity.this, Klasa5Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa6){
                    startActivity(new Intent( Klasa8Activity.this, Klasa6Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa7){
                    startActivity(new Intent( Klasa8Activity.this, Klasa7Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa8){
                    startActivity(new Intent( Klasa8Activity.this, Klasa8Activity.class));
                }
                if(item.getItemId() == R.id.zapisane){
                    startActivity(new Intent( Klasa8Activity.this, BookmarkActivity.class));
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        //moze byc konflikt

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);



        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();



        CategoryAdapter adapter = new CategoryAdapter(list);
        recyclerView.setAdapter(adapter);


        loadingDialog.show();
        myRef.child("Klasa8").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    list.add(dataSnapshot1.getValue(CategoryModel.class));
                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Klasa8Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}