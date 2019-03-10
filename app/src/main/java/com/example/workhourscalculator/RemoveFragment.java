package com.example.workhourscalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RemoveFragment extends DialogFragment {
    private static final String ARG_RECEIVED_UUID = "received uuid";

    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;
    private TextView mStartDayOfMonthTextView;
    private TextView mStartDayOfWeekTextView;
    private TextView mEndDayOfMonthTextView;
    private TextView mEndtDayOfWeekTextView;
    private TextView mNumberOfHoursTextView;
    private TextView mNumberOfMinuteTextView;


    static public RemoveFragment newInstance(UUID uuid){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RECEIVED_UUID, uuid);
        RemoveFragment removeFragment = new RemoveFragment();
        removeFragment.setArguments(bundle);
        return removeFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete,null, false);

        UUID uuid = (UUID) getArguments().getSerializable(ARG_RECEIVED_UUID);
        WorkSession workSession = WorkSessionLab.get().getWorkSession(uuid);
        Date startDate = workSession.getDateStart();
        Date endDate = workSession.getDateEnd();

        int color;
        int weekendColor;
        int numberOfHours = Hours.hoursBetween(new DateTime((startDate)),new DateTime(endDate)).getHours();
        int numberOfMinutes = Minutes.minutesBetween(new DateTime(startDate), new DateTime(endDate)).getMinutes();

        mStartTimeTextView = (TextView) view.findViewById(R.id.start_time_textView);
        mEndTimeTextView = (TextView) view.findViewById(R.id.end_time_textView);
        mStartDayOfMonthTextView = (TextView) view.findViewById(R.id.day_month_start_textView);
        mStartDayOfWeekTextView = (TextView) view.findViewById(R.id.day_of_week_start_textView);
        mEndDayOfMonthTextView = (TextView) view.findViewById(R.id.day_month_end_textView);
        mEndtDayOfWeekTextView = (TextView) view.findViewById(R.id.day_of_week_end_textView);
        mNumberOfHoursTextView = (TextView) view.findViewById(R.id.number_of_hours_textView);
        mNumberOfMinuteTextView = (TextView) view.findViewById(R.id.number_of_minutes_textView);


        if(workSession.isWorkedOut()) {
            color = getResources().getColor(R.color.Gray);
            weekendColor = getResources().getColor(R.color.Red);
        }
        else{
            color = getResources().getColor(R.color.DarkGray);
            weekendColor = getResources().getColor(R.color.DarkRed);
        }

        mStartDayOfMonthTextView.setText(FormatLab.getDateFormatDayOfMonth().format(startDate));
        mStartDayOfWeekTextView.setText(FormatLab.getDateFormatDayOfWeek().format(startDate));
        mEndDayOfMonthTextView.setText(FormatLab.getDateFormatDayOfMonth().format(endDate));
        mEndtDayOfWeekTextView.setText(FormatLab.getDateFormatDayOfWeek().format(endDate));
        mStartTimeTextView.setText(FormatLab.getDateFormatTime().format(startDate));
        mEndTimeTextView.setText(FormatLab.getDateFormatTime().format(endDate));
        mNumberOfHoursTextView.setText(String.valueOf(numberOfHours));
        mNumberOfMinuteTextView.setText(String.valueOf(numberOfMinutes % 60));

        mStartDayOfMonthTextView.setTextColor(color);
        mEndDayOfMonthTextView.setTextColor(color);
        mEndtDayOfWeekTextView.setTextColor(color);
        mStartDayOfWeekTextView.setTextColor(color);
        mStartTimeTextView.setTextColor(color);
        mEndTimeTextView.setTextColor(color);
        mNumberOfHoursTextView.setTextColor(color);
        mNumberOfMinuteTextView.setTextColor(color);
        if(FormatLab.getDayOfWeek(workSession.getDateStart()) == Calendar.SATURDAY
                || FormatLab.getDayOfWeek(workSession.getDateStart()) == Calendar.SUNDAY)
            mStartDayOfWeekTextView.setTextColor(weekendColor);

        if(FormatLab.getDayOfWeek(workSession.getDateEnd()) == Calendar.SATURDAY
                || FormatLab.getDayOfWeek(workSession.getDateEnd()) == Calendar.SUNDAY)
            mEndtDayOfWeekTextView.setTextColor(weekendColor);


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.remove_question)
                .setPositiveButton(R.string.delete_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.negative_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_CANCELED);
                    }
                })
                .setView(view)
                .show();
    }
    private void sendResult(int resultCode){
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, null);
    }
}
