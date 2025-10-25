package com.baitaplon.todo_list.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.baitaplon.todo_list.R;
import com.baitaplon.todo_list.database.OnTimeDatabase;
import com.baitaplon.todo_list.database.ScheduleDao;
import com.baitaplon.todo_list.model.Schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditScheduleFragment extends Fragment {

    ImageView btnBack, btnSave;
    CheckBox cbCompleteTop;
    EditText edtTitle, edtPlace, edtNotes;
    SwitchCompat switchAllDay;
    RelativeLayout layoutStartTime, layoutEndTime, layoutRepeat, layoutAlarm;
    TextView tvStartTime, tvEndTime, tvRepeat, tvAlarm;

    private ScheduleDao scheduleDao;
    private Schedule currentSchedule;
    private Calendar startCalendar, endCalendar;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm", new Locale("vi", "VN"));
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", new Locale("vi", "VN"));
    private SimpleDateFormat dbDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); // Định dạng lưu vào DB


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addedit_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        scheduleDao = OnTimeDatabase.getInstance(requireContext()).scheduleDao();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.HOUR, 1);

        // Kiểm tra xem có dữ liệu Schedule được truyền vào để chỉnh sửa không
        if (getArguments() != null && getArguments().containsKey("schedule_id")) {
            int scheduleId = getArguments().getInt("schedule_id", -1);
            if (scheduleId != -1) {
                loadScheduleData(scheduleId);
            } else {
                updateDateTimeViews();
            }
        } else {
            updateDateTimeViews();
        }

        setupClickListeners();
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btn_back_schedule);
        btnSave = view.findViewById(R.id.btn_save_schedule);
        cbCompleteTop = view.findViewById(R.id.checkbox_complete_top);
        edtTitle = view.findViewById(R.id.edt_schedule_title);
        edtPlace = view.findViewById(R.id.edt_schedule_place);
        edtNotes = view.findViewById(R.id.edt_schedule_notes);
        switchAllDay = view.findViewById(R.id.switch_all_day);
        layoutStartTime = view.findViewById(R.id.layout_start_time);
        layoutEndTime = view.findViewById(R.id.layout_end_time);
        layoutRepeat = view.findViewById(R.id.layout_repeat);
        layoutAlarm = view.findViewById(R.id.layout_alarm);
        tvStartTime = view.findViewById(R.id.tv_start_time);
        tvEndTime = view.findViewById(R.id.tv_end_time);
        tvRepeat = view.findViewById(R.id.tv_repeat);
        tvAlarm = view.findViewById(R.id.tv_alarm);
    }

    private void updateDateTimeViews() {
        if (switchAllDay.isChecked()) {
            tvStartTime.setText(dateFormat.format(startCalendar.getTime()));
            tvEndTime.setText(dateFormat.format(endCalendar.getTime()));
        } else {
            tvStartTime.setText(dateTimeFormat.format(startCalendar.getTime()));
            tvEndTime.setText(dateTimeFormat.format(endCalendar.getTime()));
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> closeFragment());
        btnSave.setOnClickListener(v -> saveSchedule());

        switchAllDay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateDateTimeViews();
        });

        layoutStartTime.setOnClickListener(v -> showDateTimePicker(startCalendar, tvStartTime));
        layoutEndTime.setOnClickListener(v -> showDateTimePicker(endCalendar, tvEndTime));

        layoutRepeat.setOnClickListener(v -> Toast.makeText(getContext(), "Chọn lặp lại", Toast.LENGTH_SHORT).show());
        layoutAlarm.setOnClickListener(v -> Toast.makeText(getContext(), "Chọn báo thức", Toast.LENGTH_SHORT).show());
    }

    private void showDateTimePicker(final Calendar calendarToSet, final TextView textViewToUpdate) {
        int year = calendarToSet.get(Calendar.YEAR);
        int month = calendarToSet.get(Calendar.MONTH);
        int day = calendarToSet.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    calendarToSet.set(Calendar.YEAR, yearSelected);
                    calendarToSet.set(Calendar.MONTH, monthOfYear);
                    calendarToSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (!switchAllDay.isChecked()) {
                        showTimePicker(calendarToSet, textViewToUpdate);
                    } else {
                        updateDateTimeViews();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker(final Calendar calendarToSet, final TextView textViewToUpdate) {
        int hour = calendarToSet.get(Calendar.HOUR_OF_DAY);
        int minute = calendarToSet.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    calendarToSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendarToSet.set(Calendar.MINUTE, minuteOfHour);
                    calendarToSet.set(Calendar.SECOND, 0);

                    updateDateTimeViews();
                }, hour, minute, true);

        timePickerDialog.show();
    }


    private void loadScheduleData(int scheduleId) {

    }

    private void saveSchedule() {
        String title = edtTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
            return;
        }

        String place = edtPlace.getText().toString().trim();
        String notes = edtNotes.getText().toString().trim();
        boolean isCompleted = cbCompleteTop.isChecked();
        boolean isAllDay = switchAllDay.isChecked();

        String startTimeDb = dbDateTimeFormat.format(startCalendar.getTime());
        String endTimeDb = dbDateTimeFormat.format(endCalendar.getTime());

        if (isAllDay) {
            Calendar startOfDay = (Calendar) startCalendar.clone();
            startOfDay.set(Calendar.HOUR_OF_DAY, 0);
            startOfDay.set(Calendar.MINUTE, 0);
            startOfDay.set(Calendar.SECOND, 0);
            startTimeDb = dbDateTimeFormat.format(startOfDay.getTime());

            Calendar endOfDay = (Calendar) endCalendar.clone();
            endOfDay.set(Calendar.HOUR_OF_DAY, 0);
            endOfDay.set(Calendar.MINUTE, 0);
            endOfDay.set(Calendar.SECOND, 0);
            endTimeDb = dbDateTimeFormat.format(endOfDay.getTime());
        }


        if (currentSchedule == null) {
            currentSchedule = new Schedule();
        }

        currentSchedule.setTitle(title);
        currentSchedule.setStartTime(startTimeDb);
        currentSchedule.setEndTime(endTimeDb);
        currentSchedule.setPlace(place);
        currentSchedule.setNotes(notes);
        currentSchedule.setCompleted(isCompleted);

    }

    private void closeFragment() {
        FragmentManager fm = getParentFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else if (getActivity() != null) {
            getActivity().finish();
        }
    }
}