package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.instagram.fragments.ComposeFragment;
import com.example.instagram.fragments.PostsFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MenuItem last;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        last= bottomNavigationView.getMenu().findItem(bottomNavigationView.getSelectedItemId());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(last.getItemId()){
                    case R.id.action_compose:
                        last.setIcon(R.drawable.post_outline);
                        break;
                    case R.id.action_profile:
                        last.setIcon(R.drawable.user_outline);
                         break;
                    case R.id.action_home:
                        last.setIcon(R.drawable.home_outline);
                        break;
                }
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_compose:
                        menuItem.setIcon(R.drawable.post_filled);
                        fragment=new ComposeFragment();
                        break;
                    case R.id.action_home:
                        menuItem.setIcon(R.drawable.home_filled);
                        fragment=new PostsFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        menuItem.setIcon(R.drawable.user_filled);
                        fragment=new ProfileFragment();
                        break;
                }
                last= menuItem;
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
                }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
    public void onLogOut(MenuItem mi){
        ParseUser.logOut();
        Intent i = new Intent(this, StartActivity.class);
        startActivity(i);
    }

}