package com.hecvd19.pas.home;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hecvd19.pas.R;
import com.hecvd19.pas.home.tabs.ChannelsFragment;
import com.hecvd19.pas.home.tabs.PostFragment;
import com.hecvd19.pas.home.ui.SectionPagerAdapter;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(), 0);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
        tabLayout.setupWithViewPager(viewPager);
        adapter.addFragment(new PostFragment(), "Posts");
        adapter.addFragment(new ChannelsFragment(), "My Channels");
        viewPager.setAdapter(adapter);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            Toast.makeText(this, "Log out!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
