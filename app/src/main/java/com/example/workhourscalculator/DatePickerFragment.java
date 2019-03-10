package com.example.workhourscalculator;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.workhourscalculator.TimePickerFragment.EXTRA_SEND_DATE;

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_RECEIVED_DATE = "received date";
    private static final String ARG_RECEIVED_TYPE_OF_DATE = "received type of date start/end";
    public static final String EXTRA_SEND_DATE = "com.example.workhourscalculator.DatePickerFragment.sending_date";
    private int mTitleResId;
    private Date mDate;

    public static DatePickerFragment newInstance(Date date, int titleRes){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RECEIVED_DATE, date);
        bundle.putInt(ARG_RECEIVED_TYPE_OF_DATE, titleRes);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
   }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null, false);

        int titleResId = (int) getArguments().getInt(ARG_RECEIVED_TYPE_OF_DATE);
        Date date = (Date) getArguments().getSerializable(ARG_RECEIVED_DATE);
/*
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
*/
        final int hour = FormatLab.getHourOfDay(date);
        final int minute = FormatLab.getMinute(date);
        final int year = FormatLab.getYear(date);
        final int month = FormatLab.getMonth(date);
        final int day = FormatLab.getDayOfMonth(date);

        final DatePicker datePicker = view.findViewById(R.id.date_picker);
        datePicker.updateDate(year,month,day);

        return new AlertDialog.Builder(getActivity())
                .setTitle(titleResId)
                .setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .setView(view)
                .create();
    }

    private void sendResult(int resultCode, Date date){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEND_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
