package com.baitaplon.todo_list.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baitaplon.todo_list.R;
import com.baitaplon.todo_list.adapter.ScheduleAdapter;
import com.baitaplon.todo_list.database.OnTimeDatabase;
import com.baitaplon.todo_list.database.ScheduleDao;
import com.baitaplon.todo_list.model.Schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment implements ScheduleAdapter.OnScheduleItemClickListener{
    private CalendarView calendarView;
    private RecyclerView recyclerViewSchedule;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> currentScheduleList;
    private ScheduleDao scheduleDao;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerViewSchedule = view.findViewById(R.id.recyclerView_schedule);

        scheduleDao = OnTimeDatabase.getInstance(requireContext()).scheduleDao();

        setupRecyclerView();

        calendarView.setOnDateChangeListener((viewCalendar, year, month, dayOfMonth) -> {
            String selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
            loadSchedulesForDate(selectedDate);
        });

        loadInitialSchedules();
    }

    private void setupRecyclerView() {
        currentScheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(requireContext(), currentScheduleList, this);
        recyclerViewSchedule.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewSchedule.setAdapter(scheduleAdapter);
    }

    private void loadInitialSchedules() {
        LocalDate today = LocalDate.now();
        String todayString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        loadSchedulesForDate(todayString);
    }

    private void loadSchedulesForDate(String date) {
        List<Schedule> schedules = scheduleDao.getSchedulesForDate(date);

        currentScheduleList = schedules;
        scheduleAdapter.setData(currentScheduleList);
    }

    @Override
    public void onItemClick(Schedule schedule) {
         Toast.makeText(requireContext(), "Clicked: " + schedule.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckboxClick(Schedule schedule, boolean isChecked) {
        schedule.setCompleted(isChecked);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}
