package com.example.klikzdaj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.klikzdaj.QuestionsActivity.FILE_NAME;
import static com.example.klikzdaj.QuestionsActivity.KEY_NAME;

public class BookmarkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<QuestionModel> bookmarksList;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_bookmark);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.zapisane);
        getSupportActionBar().setTitle(null);



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_klasa1){
                    startActivity(new Intent( BookmarkActivity.this, MainActivity.class));
                }
                if(item.getItemId() == R.id.nav_klasa2){
                    startActivity(new Intent( BookmarkActivity.this, Klasa2Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa3){
                    startActivity(new Intent( BookmarkActivity.this, Klasa3Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa4){
                    startActivity(new Intent( BookmarkActivity.this, Klasa4Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa5){
                    startActivity(new Intent( BookmarkActivity.this, Klasa5Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa6){
                    startActivity(new Intent( BookmarkActivity.this, Klasa6Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa7){
                    startActivity(new Intent( BookmarkActivity.this, Klasa7Activity.class));
                }
                if(item.getItemId() == R.id.nav_klasa8){
                    startActivity(new Intent( BookmarkActivity.this, Klasa8Activity.class));
                }
                if(item.getItemId() == R.id.zapisane){
                    startActivity(new Intent( BookmarkActivity.this, BookmarkActivity.class));
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });


        recyclerView = findViewById(R.id.rv_bookmarks);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        BookmarksAdapter adapter = new BookmarksAdapter(bookmarksList);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onPause(){
        super.onPause();
        storeBookmarks();
    }

    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME,"");

        Type type = new TypeToken<List<QuestionModel>>(){}.getType();

        bookmarksList = gson.fromJson(json,type);

        if(bookmarksList == null){
            bookmarksList = new ArrayList<>();
        }
    }


    private void storeBookmarks(){

        String json = gson.toJson(bookmarksList);

        editor.putString(KEY_NAME,json);
        editor.commit();

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