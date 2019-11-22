package com.eltherbiometric.ui.upload;

import android.os.Bundle;

import com.eltherbiometric.R;
import com.eltherbiometric.ui.upload.ui.main.TabAbsenceFragment;
import com.eltherbiometric.ui.upload.ui.main.TabPresenceFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.eltherbiometric.ui.upload.ui.main.TabAdapter;

public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabPresenceFragment(), "Hadir");
        adapter.addFragment(new TabAbsenceFragment(), "Absen");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}