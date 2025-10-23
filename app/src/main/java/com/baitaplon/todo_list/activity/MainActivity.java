package com.baitaplon.todo_list.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.baitaplon.todo_list.R;
import com.baitaplon.todo_list.adapter.MainViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    TextView tabSchedule, tabNotes;
    FloatingActionButton fabAdd, fabClose, fabSchedule, fabNote;
    TextView tvFabSchedule, tvFabNote;
    MainViewPagerAdapter adapter;
    boolean isFabMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        adapter = new MainViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTabs(position);
            }
        });

        tabSchedule.setOnClickListener(v -> viewPager.setCurrentItem(0));
        tabNotes.setOnClickListener(v -> viewPager.setCurrentItem(1));

        updateTabs(0);

        fabAdd.setOnClickListener(v -> showFabMenu());
        fabClose.setOnClickListener(v -> closeFabMenu());
    }

    private void updateTabs(int position) {
        if (position == 0) {
            // Tab Lịch trình (Được chọn)
            tabSchedule.setBackgroundResource(R.drawable.bg_tab_selected);
            tabSchedule.setTextColor(ContextCompat.getColor(this, R.color.primary));

            // Tab Ghi chú (Không được chọn)
            tabNotes.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabNotes.setTextColor(ContextCompat.getColor(this, R.color.white));

        } else {
            // Tab Lịch trình (Không được chọn)
            tabSchedule.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabSchedule.setTextColor(ContextCompat.getColor(this, R.color.white));

            // Tab Ghi chú (Được chọn)
            tabNotes.setBackgroundResource(R.drawable.bg_tab_selected);
            tabNotes.setTextColor(ContextCompat.getColor(this, R.color.primary));
        }
    }

    private void initUI(){
        viewPager = findViewById(R.id.view_pager);
        tabSchedule = findViewById(R.id.tab_schedule);
        tabNotes = findViewById(R.id.tab_notes);

        fabAdd = findViewById(R.id.fab_add);
        fabClose = findViewById(R.id.fab_close);
        fabSchedule = findViewById(R.id.fab_schedule);
        fabNote = findViewById(R.id.fab_note);
        tvFabSchedule = findViewById(R.id.tv_fab_schedule);
        tvFabNote = findViewById(R.id.tv_fab_note);
    }

    private void showFabMenu() {
        isFabMenuOpen = true;
        fabAdd.setVisibility(View.GONE);
        fabClose.setVisibility(View.VISIBLE);

        fabSchedule.setVisibility(View.VISIBLE);
        tvFabSchedule.setVisibility(View.VISIBLE);
        fabNote.setVisibility(View.VISIBLE);
        tvFabNote.setVisibility(View.VISIBLE);
    }


    private void closeFabMenu() {
        isFabMenuOpen = false;
        fabAdd.setVisibility(View.VISIBLE);
        fabClose.setVisibility(View.GONE);

        fabSchedule.setVisibility(View.GONE);
        tvFabSchedule.setVisibility(View.GONE);
        fabNote.setVisibility(View.GONE);
        tvFabNote.setVisibility(View.GONE);
    }
}