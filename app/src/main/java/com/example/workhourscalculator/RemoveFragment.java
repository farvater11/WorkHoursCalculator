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

        int numberOfHours = Hours.hoursBetween(new DateTime((workSession.getDateStart())),new DateTime(workSession.getDateEnd())).getHours();
        int numberOfMinutes = Minutes.minutesBetween(new DateTime(workSession.getDateStart()), new DateTime(workSession.getDateEnd())).getMinutes();

        mStartTimeTextView = (TextView) view.findViewById(R.id.start_time_textView);
        mEndTimeTextView = (TextView) view.findViewById(R.id.end_time_textView);
        mStartDayOfMonthTextView = (TextView) view.findViewById(R.id.day_month_start_textView);
        mStartDayOfWeekTextView = (TextView) view.findViewById(R.id.day_of_week_start_textView);
        mEndDayOfMonthTextView = (TextView) view.findViewById(R.id.day_month_end_textView);
        mEndtDayOfWeekTextView = (TextView) view.findViewById(R.id.day_of_week_end_textView);
        mNumberOfHoursTextView = (TextView) view.findViewById(R.id.number_of_hours_textView);
        mNumberOfMinuteTextView = (TextView) view.findViewById(R.id.number_of_minutes_textView);


        mStartDayOfMonthTextView.setText(FormatLab.getDateFormatDayOfMonth().format(workSession.getDateStart()));
        mStartDayOfWeekTextView.setText(FormatLab.getDateFormatDayOfWeek().format(workSession.getDateStart()));
        mEndDayOfMonthTextView.setText(FormatLab.getDateFormatDayOfMonth().format(workSession.getDateEnd()));
        mEndtDayOfWeekTextView.setText(FormatLab.getDateFormatDayOfWeek().format(workSession.getDateEnd()));
        mStartTimeTextView.setText(FormatLab.getDateFormatTime().format(workSession.getDateStart()));
        mEndTimeTextView.setText(FormatLab.getDateFormatTime().format(workSession.getDateEnd()));

        mNumberOfHoursTextView.setText(String.valueOf(numberOfHours));
        mNumberOfMinuteTextView.setText(String.valueOf(numberOfMinutes % 60));



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
