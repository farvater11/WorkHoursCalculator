package com.example.workhourscalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_KEY = "saved subtitle state";
    private static final int REQUEST_START_DATE = 0;
    private static final int REQUEST_END_DATE = 1;
    private static final String DIALOG_START_DATE = "DialogDateStart";
    private static final String DIALOG_END_DATE = "DialogDateEnd";

    private List<WorkSession> mWorkSessions;
    private RecyclerView mRecyclerView;
    private WorkSessionAdapter mAdapter;
    private boolean mVisibleSubtitle;
    protected int mClickedItemIndex;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
            return;

        WorkSession modifyWorkSession = mWorkSessions.get(mClickedItemIndex); // Получаем кликнутый элемент
        switch (requestCode){
            case REQUEST_START_DATE:
                Date dateStart = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_SEND_DATE);
                //Получаем дату измененную, как узнать какому item было изменено? Чтоб ему установить
                modifyWorkSession.setDateStart(dateStart);// заменяем дату в кликнутом item
                WorkSessionLab.get().updateSession(modifyWorkSession); //обновляем в Lab этот элемент
                updateUI();
                break;
            case REQUEST_END_DATE:
                Date dateEnd = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_SEND_DATE);
                modifyWorkSession.setDateEnd(dateEnd);// заменяем дату в кликнутом item
                WorkSessionLab.get().updateSession(modifyWorkSession); //обновляем в Lab этот элемент
                updateUI();
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState != null)
            mVisibleSubtitle = savedInstanceState.getBoolean(SAVED_SUBTITLE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_date_list,container,false);
         mRecyclerView = (RecyclerView) v.findViewById(R.id.work_recyclerView);
         mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

         updateUI();
         //mWorkSessions = WorkSessionLab.get().getWorkSessions();
         //mAdapter = new WorkSessionAdapter(mWorkSessions);
         //mRecyclerView.setAdapter(mAdapter);
         return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SAVED_SUBTITLE_KEY,mVisibleSubtitle);
        super.onSaveInstanceState(outState);
    }

//Option menu section
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_date_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mVisibleSubtitle)
            subtitleItem.setTitle(R.string.hide_subtitle);
        else
            subtitleItem.setTitle(R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item_menu:
                WorkSessionLab.get().addSession(new WorkSession());
                updateUI();
                return true;
            case R.id.show_subtitle:
                mVisibleSubtitle =! mVisibleSubtitle;
                updateSubtitle();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//End option menu section


//Modification UI section
    private void updateUI(){//update ui and adapter in recyclerView
        mWorkSessions = WorkSessionLab.get().getWorkSessions();
        if (mAdapter == null){
            mAdapter = new WorkSessionAdapter(mWorkSessions);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setWorkSessions(mWorkSessions);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    private void updateSubtitle(){
        getActivity().invalidateOptionsMenu(); // mark option menu not actual
        int numWorkSessions = WorkSessionLab.get().getWorkSessions().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, numWorkSessions,numWorkSessions);
        if(!mVisibleSubtitle)
            subtitle = null;
        AppCompatActivity appCompatActivity =(AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle); // set new subtitle
    }
//End modification UI section


    private class WorkSessionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout mStartLayout;
        private LinearLayout mEndLayout;
        private TextView mStartTimeTextView;
        private TextView mEndTimeTextView;

        private ConstraintLayout mMainItemLayout;

        private TextView mStartDayOfMonthTextView;
        private TextView mStartDayOfWeekTextView;
        private TextView mEndDayOfMonthTextView;
        private TextView mEndtDayOfWeekTextView;
        private TextView mNumberOfHoursTextView;
        private TextView mNumberOfMinuteTextView;

        private UUID mUUID;


        public WorkSessionHolder(LayoutInflater layoutInflater, ViewGroup parent){
            super(layoutInflater.inflate(R.layout.list_item_date,parent,false));

            mMainItemLayout = (ConstraintLayout) itemView.findViewById(R.id.main_item_layout);

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

            mMainItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getActivity(),"Dele", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

        }

        public void bind(WorkSession workSession){
            Date startDate = workSession.getDateStart();
            Date endDate = workSession.getDateEnd();
            mUUID = workSession.getUUID();
            int numberOfHours = Hours.hoursBetween(new DateTime((startDate)),new DateTime(endDate)).getHours();
            int numberOfMinutes = Minutes.minutesBetween(new DateTime(startDate), new DateTime(endDate)).getMinutes();

        //Remake this for a using joda.time library!
            SimpleDateFormat dateFormatDayOfWeek = new SimpleDateFormat("EEEE");
            SimpleDateFormat dateFormatDayOfMonth = new SimpleDateFormat("dd MMM");
            SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
            mStartDayOfMonthTextView.setText(dateFormatDayOfMonth.format(startDate));
            mStartDayOfWeekTextView.setText(dateFormatDayOfWeek.format(startDate));
            mEndDayOfMonthTextView.setText(dateFormatDayOfMonth.format(endDate));
            mEndtDayOfWeekTextView.setText(dateFormatDayOfWeek.format(endDate));
            mStartTimeTextView.setText(dateFormatTime.format(startDate));
            mEndTimeTextView.setText(dateFormatTime.format(endDate));
        // ====

            mNumberOfHoursTextView.setText(String.valueOf(numberOfHours));
            mNumberOfMinuteTextView.setText(String.valueOf(numberOfMinutes % 60));

        }


        @Override
        public void onClick(View v) {
            mClickedItemIndex = getLayoutPosition();
            switch (v.getId()){
                case R.id.start_date_layout:
                    break;
                case R.id.end_date_layout:
                    break;
                case R.id.start_time_textView:
                    FragmentManager fragmentManager = getFragmentManager();
                    TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(
                            WorkSessionLab.get().getWorkSession(mUUID).getDateStart(),R.string.start_time_title_alert);
                    timePickerFragment.setTargetFragment(ListFragment.this, REQUEST_START_DATE);
                    timePickerFragment.show(fragmentManager, DIALOG_START_DATE);
                    break;
                case R.id.end_time_textView:
                    FragmentManager fragmentManager1 = getFragmentManager();
                    TimePickerFragment timePickerFragment1 = TimePickerFragment.newInstance(
                            WorkSessionLab.get().getWorkSession(mUUID).getDateEnd(), R.string.end_time_title_alert);
                    timePickerFragment1.setTargetFragment(ListFragment.this, REQUEST_END_DATE);
                    timePickerFragment1.show(fragmentManager1, DIALOG_END_DATE);
                    break;
            }
        }
    }



    private class WorkSessionAdapter extends RecyclerView.Adapter<WorkSessionHolder>{
        private List<WorkSession> mWorkSessions;

        public WorkSessionAdapter(List<WorkSession> workSessionsList){
            mWorkSessions = workSessionsList;
        }

        public void setWorkSessions(List<WorkSession> workSessionList){
            mWorkSessions = workSessionList;
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
        }

        @Override
        public int getItemCount() {
            return mWorkSessions.size();
        }
    }
}



