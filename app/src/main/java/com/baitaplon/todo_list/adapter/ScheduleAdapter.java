package com.baitaplon.todo_list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baitaplon.todo_list.R;
import com.baitaplon.todo_list.model.Schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private static final int VIEW_TYPE_PRIMARY = 1;
    private static final int VIEW_TYPE_SECONDARY = 2;
    private Context mContext;
    private List<Schedule> scheduleList;
    private OnScheduleItemClickListener listener;

    public interface OnScheduleItemClickListener {
        void onItemClick(Schedule schedule);
        void onCheckboxClick(Schedule schedule, boolean isChecked);
    }

    public ScheduleAdapter(Context mContext, List<Schedule> scheduleList, OnScheduleItemClickListener listener) {
        this.mContext = mContext;
        this.scheduleList = scheduleList;
        this.listener = listener;
    }

    public void setData(List<Schedule> newList) {
        this.scheduleList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Schedule item = scheduleList.get(position);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        //item_schedule có time bắt đầu là ngày hôm nay sẽ set giao diện với màu primary
         if (item.getStartTime().substring(0, 11).equals(formattedDateTime.substring(0, 11))) {
            return VIEW_TYPE_PRIMARY;
         } else {
            return VIEW_TYPE_SECONDARY;
         }
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_PRIMARY) {
            view = inflater.inflate(R.layout.item_schedule_primary, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_schedule_secondary, parent, false);
        }

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule currentSchedule = scheduleList.get(position);

        holder.tvTitle.setText(currentSchedule.getTitle());
        holder.tvTime.setText(currentSchedule.getTimeFromTo());
        holder.tvPlace.setText(currentSchedule.getPlace());
        holder.tvNotes.setText(currentSchedule.getNotes());
        holder.checkBox.setChecked(currentSchedule.getCompleted());

        //ẩn hiện ngày: nếu item trước có ngày giống với ngày item hiện tại sẽ không set tvngay
        String dayOfMonth = getDayOfMonth(currentSchedule.getStartTime());
        holder.tvDay.setText(dayOfMonth);
        if (position == 0) {
            holder.tvDay.setVisibility(View.VISIBLE);
        } else {
            Schedule previousSchedule = scheduleList.get(position - 1);

            String currentDatePart = getDatePart(currentSchedule.getStartTime());
            String previousDatePart = getDatePart(previousSchedule.getStartTime());

            if (currentDatePart.equals(previousDatePart)) {
                holder.tvDay.setVisibility(View.INVISIBLE);
            } else {
                holder.tvDay.setVisibility(View.VISIBLE);
            }
        }

        //event
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> {
                listener.onItemClick(currentSchedule);
            });

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (buttonView.isPressed()) {
                    listener.onCheckboxClick(currentSchedule, isChecked);
                }
            });
        }
    }


    private String getDatePart(String dateTime) {
        if (dateTime == null || dateTime.length() < 10) {
            return "";
        }
        return dateTime.substring(0, 10); // "yyyy-MM-dd"
    }

    private String getDayOfMonth(String dateTime) {
        if (dateTime == null || dateTime.length() < 10) {
            return "?";
        }
        return dateTime.substring(8, 10); // "dd"
    }

    @Override
    public int getItemCount() {
        return scheduleList != null ? scheduleList.size() : 0;
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTitle, tvTime, tvPlace, tvNotes;
        CheckBox checkBox;
        CardView cardView;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_schedule_day);
            tvTitle = itemView.findViewById(R.id.tv_schedule_title);
            tvTime = itemView.findViewById(R.id.tv_schedule_time);
            tvPlace = itemView.findViewById(R.id.tv_schedule_place);
            tvNotes = itemView.findViewById(R.id.tv_schedule_notes);
            checkBox = itemView.findViewById(R.id.checkbox_schedule_complete);
            cardView = itemView.findViewById(R.id.card_schedule_item);
        }
    }
}
