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
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_RECEIVED_DATE = "receive date";
    private static final String ARG_RECEIVED_TYPE_OF_TIME = "received type of date start/end";
    public static final String EXTRA_SEND_DATE = "com.example.workhourscalculator.TimePickerFragment.sending_date";

    static public TimePickerFragment newInstance(Date date, int titleRes){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RECEIVED_DATE, date);
        bundle.putInt(ARG_RECEIVED_TYPE_OF_TIME, titleRes);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time_picker,null,false);

        int title = (int) getArguments().getInt(ARG_RECEIVED_TYPE_OF_TIME);

        final Date date = (Date) getArguments().getSerializable(ARG_RECEIVED_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minutes);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();

                        Date date1 = new GregorianCalendar(year,month,day,hour,minute).getTime();
                        sendResult(Activity.RESULT_OK, date1);
                    }
                })
                .setView(view)
                .create();
    }
    private void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEND_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode, intent);
    }
}
