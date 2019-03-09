package com.example.workhourscalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private static final int REQUEST_START_TIME = 0;
    private static final int REQUEST_END_TIME = 1;
    private static final int REQUEST_START_DATE = 2;
    private static final int REQUEST_END_DATE = 3;
    private static final int REQUEST_REMOVE = 4;


    private static final String DIALOG_START_TIME_TAG = "DialogTimeStart";
    private static final String DIALOG_END_TIME_TAG = "DialogTimeEnd";
    private static final String DIALOG_START_DATE_TAG = "DialogDateStart";
    private static final String DIALOG_END_DATE_TAG = "DialogDateEnd";
    private static final String DIALOG_REMOVE_TAG = "DialogRemove";

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
            case REQUEST_START_TIME:
                Date timeStart = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_SEND_DATE);
                //Получаем дату измененную, как узнать какому item было изменено? Чтоб ему установить
                modifyWorkSession.setDateStart(timeStart);// заменяем дату в кликнутом item
                WorkSessionLab.get().updateSession(modifyWorkSession); //обновляем в Lab этот элемент
                break;
            case REQUEST_END_TIME:
                Date timeEnd = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_SEND_DATE);
                modifyWorkSession.setDateEnd(timeEnd);// заменяем дату в кликнутом item
                WorkSessionLab.get().updateSession(modifyWorkSession); //обновляем в Lab этот элемент
                break;
            case REQUEST_START_DATE:
                Date dateStart = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_SEND_DATE);
                modifyWorkSession.setDateStart(dateStart);
                WorkSessionLab.get().updateSession(modifyWorkSession);
                break;
            case REQUEST_END_DATE:
                Date dateEnd = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_SEND_DATE);
                modifyWorkSession.setDateEnd(dateEnd);
                WorkSessionLab.get().updateSession(modifyWorkSession);
                break;
            case REQUEST_REMOVE:
                WorkSessionLab.get().remSession(mWorkSessions.get(mClickedItemIndex).getUUID());
                break;
        }
        updateUI();
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


    private class WorkSessionHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
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
            //super(layoutInflater.inflate(R.layout.dialog_delete,parent,false));
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

            itemView.setOnLongClickListener(this);
        }

        public void bind(WorkSession workSession){
            Date startDate = workSession.getDateStart();
            Date endDate = workSession.getDateEnd();

            int numberOfHours = Hours.hoursBetween(new DateTime((startDate)),new DateTime(endDate)).getHours();
            int numberOfMinutes = Minutes.minutesBetween(new DateTime(startDate), new DateTime(endDate)).getMinutes();

        //Remake this for a using joda.time library!
            SimpleDateFormat dateFormatDayOfWeek = new SimpleDateFormat("EEEE");
            SimpleDateFormat dateFormatDayOfMonth = new SimpleDateFormat("dd MMM");
            SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
            mStartDayOfMonthTextView.setText(FormatLab.getDateFormatDayOfMonth().format(startDate));
            mStartDayOfWeekTextView.setText(FormatLab.getDateFormatDayOfWeek().format(startDate));
            mEndDayOfMonthTextView.setText(FormatLab.getDateFormatDayOfMonth().format(endDate));
            mEndtDayOfWeekTextView.setText(FormatLab.getDateFormatDayOfWeek().format(endDate));
            mStartTimeTextView.setText(FormatLab.getDateFormatTime().format(startDate));
            mEndTimeTextView.setText(FormatLab.getDateFormatTime().format(endDate));
        // ====

            mNumberOfHoursTextView.setText(String.valueOf(numberOfHours));
            mNumberOfMinuteTextView.setText(String.valueOf(numberOfMinutes % 60));

        }


        @Override
        public void onClick(View v) {
            mClickedItemIndex = getLayoutPosition();
            FragmentManager fragmentManager = getFragmentManager();

            switch (v.getId()){
                case R.id.start_date_layout:
                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(
                            mWorkSessions.get(mClickedItemIndex).getDateStart(), R.string.start_date_title_alert);
                    datePickerFragment.setTargetFragment(ListFragment.this, REQUEST_START_DATE);
                    datePickerFragment.show(fragmentManager, DIALOG_START_DATE_TAG);
                    break;
                case R.id.end_date_layout:
                    DatePickerFragment datePickerFragment1 = DatePickerFragment.newInstance(
                            mWorkSessions.get(mClickedItemIndex).getDateEnd(), R.string.end_date_title_alert);
                    datePickerFragment1.setTargetFragment(ListFragment.this, REQUEST_END_DATE);
                    datePickerFragment1.show(fragmentManager, DIALOG_END_DATE_TAG);
                    break;
                case R.id.start_time_textView:
                    TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(
                            mWorkSessions.get(mClickedItemIndex).getDateStart(), R.string.start_time_title_alert);
                    timePickerFragment.setTargetFragment(ListFragment.this, REQUEST_START_TIME);
                    timePickerFragment.show(fragmentManager, DIALOG_START_TIME_TAG);
                    break;
                case R.id.end_time_textView:
                    TimePickerFragment timePickerFragment1 = TimePickerFragment.newInstance(
                            mWorkSessions.get(mClickedItemIndex).getDateEnd(), R.string.end_time_title_alert);
                    timePickerFragment1.setTargetFragment(ListFragment.this, REQUEST_END_TIME);
                    timePickerFragment1.show(fragmentManager, DIALOG_END_TIME_TAG);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mClickedItemIndex = getLayoutPosition();
            FragmentManager fragmentManager = getFragmentManager();
            RemoveFragment removeFragment = RemoveFragment.newInstance(mWorkSessions.get(mClickedItemIndex).getUUID());
            removeFragment.setTargetFragment(ListFragment.this, REQUEST_REMOVE);
            removeFragment.show(fragmentManager, DIALOG_REMOVE_TAG);
            Toast.makeText(getActivity(),"Picked item#" + mClickedItemIndex, Toast.LENGTH_SHORT).show();
            return true;
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



