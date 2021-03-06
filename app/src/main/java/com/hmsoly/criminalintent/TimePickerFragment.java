package com.hmsoly.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Harlan on 4/26/15.
 */
public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_TIME = "com.HmsOly.CriminalIntent.time";
    private Date mDate;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME,date);

        TimePickerFragment fragment = new TimePickerFragment();

        fragment.setArguments(args);
        return fragment;

    }

    private void sendResult(int resultCode){
        if (getTargetFragment()==null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_TIME, mDate);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode, i);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date)getArguments().getSerializable(EXTRA_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_time, null);

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mDate);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);

                mDate= cal.getTime();
                getArguments().putSerializable(EXTRA_TIME, mDate);

            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }
}
