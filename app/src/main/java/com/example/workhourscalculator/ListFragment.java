package com.example.workhourscalculator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class ListFragment extends Fragment {
    private List<WorkSession> mWorkSessions;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_date_list,container,false);
         mRecyclerView = (RecyclerView) v.findViewById(R.id.work_recyclerView);
         mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
         mWorkSessions = WorkSessionLab.get().getWorkSessions();
         mAdapter = new WorkSessionAdapter(mWorkSessions);
         mRecyclerView.setAdapter(mAdapter);
         return v;
    }



    private class WorkSessionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout mStartLayout;
        private LinearLayout mEndLayout;
        private TextView mStartTimeTextView;
        private TextView mEndTimeTextView;

        private TextView mStartDayOfMonthTextView;
        private TextView mStartDayOfWeekTextView;
        private TextView mEndDayOfMonthTextView;
        private TextView mEndtDayOfWeekTextView;
        private TextView mNumberOfHoursTextView;
        private TextView mNumberOfMinuteTextView;


        public WorkSessionHolder(LayoutInflater layoutInflater, ViewGroup parent){
            super(layoutInflater.inflate(R.layout.list_item_date,parent,false));
            mStartLayout = (LinearLayout) itemView.findViewById(R.id.start_date_layout);
            mEndLayout = (LinearLayout) itemView.findViewById(R.id.end_date_layout);
            mStartTimeTextView = (TextView) itemView.findViewById(R.id.start_time_textView);
            mEndTimeTextView = (TextView) itemView.findViewById(R.id.end_time_textView);

            mStartDayOfMonthTextView = (TextView) itemView.findViewById(R.id.day_month_start_textView);
            mStartDayOfWeekTextView = (TextView) itemView.findViewById(R.id.day_of_week_start_textView);
            mEndDayOfMonthTextView = (TextView) itemView.findViewById(R.id.day_month_end_textView);
            mEndtDayOfWeekTextView = (TextView) itemView.findViewById(R.id.day_of_week_end_textView);

            mNumberOfHoursTextView = (TextView) itemView.findViewById(R.id.number_of_hours_textView);
            mNumberOfMinuteTextView = (TextView) itemView.findViewById(R.id.number_of_minutes_textView);

            mStartLayout.setOnClickListener(this);
            mEndLayout.setOnClickListener(this);
            mStartTimeTextView.setOnClickListener(this);
            mEndTimeTextView.setOnClickListener(this);
        }

        public void bind(WorkSession workSession){
            Date startDate = workSession.getDateStart();
            Date endDate = workSession.getDateEnd();

            int numberOfHours = Hours.hoursBetween(new DateTime((startDate)),new DateTime(endDate)).getHours();
            int numberOfMinutes = Minutes.minutesBetween(new DateTime(startDate), new DateTime(endDate)).getMinutes();
/*
        //Remake this for a using joda.time library!
            SimpleDateFormat dateFormatDayOfWeek = new SimpleDateFormat("EEEE");
            SimpleDateFormat dateFormatDayOfMonth = new SimpleDateFormat("dd MMM");
            SimpleDateFormat dateFormatTime = new SimpleDateFormat("hh:mm");
            mStartDayOfMonthTextView.setText(dateFormatDayOfMonth.format(startDate));
            mStartDayOfWeekTextView.setText(dateFormatDayOfWeek.format(startDate));
            mEndDayOfMonthTextView.setText(dateFormatDayOfMonth.format(endDate));
            mEndtDayOfWeekTextView.setText(dateFormatDayOfWeek.format(endDate));
            mStartTimeTextView.setText(dateFormatTime.format(startDate));
            mEndTimeTextView.setText(dateFormatTime.format(endDate));
        // ====

            mNumberOfHoursTextView.setText(String.valueOf(numberOfHours));
            mNumberOfMinuteTextView.setText(String.valueOf(numberOfMinutes));
*/
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_date_layout:
                    break;
                case R.id.end_date_layout:
                    break;
                case R.id.start_time_textView:
                    break;
                case R.id.end_time_textView:
                    break;
            }
        }
    }



    private class WorkSessionAdapter extends RecyclerView.Adapter<WorkSessionHolder>{
        private List<WorkSession> mWorkSessions;

        public WorkSessionAdapter(List<WorkSession> workSessionsList){
            mWorkSessions = workSessionsList;
        }

        @NonNull

        @Override
        public WorkSessionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new WorkSessionHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkSessionHolder workSessionHolder, int i) {
            WorkSession workSession = mWorkSessions.get(workSessionHolder.getLayoutPosition());
            workSessionHolder.bind(workSession);
            Log.d("KEY_Bind", workSessionHolder.getLayoutPosition() + "was bindied");
        }

        @Override
        public int getItemCount() {
            return mWorkSessions.size();
        }
    }
}



