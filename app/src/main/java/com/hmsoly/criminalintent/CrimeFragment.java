package com.hmsoly.criminalintent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;


/**
 * Created by Harlan on 3/15/15.
 */
public class CrimeFragment extends Fragment{
    public static final String EXTRA_CRIME_ID =
            "com.HmsOly.android.CriminalIntent.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0xff;
    private static final int REQUEST_TIME = 0xfe;
    private static final int REQUEST_CHOICE = 0xfd;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    private void choiceDialogOpen(){
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        ChoiceDialogFragment dialogFragment = new ChoiceDialogFragment();
        dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_CHOICE);
        dialogFragment.show(fm, null);
    }

    private void timeDialogOpen(){
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
        dialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
        dialog.show(fm, null);
    }

    private void dateDialogOpen(){
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance((mCrime.getDate()));
        dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
        dialog.show(fm, null);

    }

    private void combineTime(Date time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCrime.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(time);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        Date finalDate = new GregorianCalendar(year, month, day, hours, minutes).getTime();
        mCrime.setDate(finalDate);
    }

    private void combineDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(mCrime.getDate());
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        Date finalDate = new GregorianCalendar(year,month,day,hours,minutes).getTime();
        mCrime.setDate(finalDate);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK) return;
        if(requestCode == REQUEST_DATE){
            Date date = (Date)data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            //mDateButton.setText(mCrime.getDate().toString());
            combineDate(date);
            updateDate();
        }
        if (requestCode == REQUEST_TIME){
            Date date = (Date)data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            combineTime(date);
            updateDate();
        }
        if (requestCode == REQUEST_CHOICE){
            int choice = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOICE, 0);
            if (choice == 0){
                Log.d("choice dialog", "requested choice returned nothing");
                return;
            }
            if (choice == ChoiceDialogFragment.CHOICE_DATE) dateDialogOpen();
            else if (choice == ChoiceDialogFragment.CHOICE_TIME) timeDialogOpen();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    public static CrimeFragment newInstance(UUID crimeID){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID,crimeID);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void updateDate(){
        mDateButton.setText(mCrime.getDateString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            //Intentional space
            }
        });


        //This line will change a date object into a format you specify.
        java.text.SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE', 'MMM' 'd', 'yyyy", Locale.US);

        mDateButton =(Button)v.findViewById(R.id.crime_date);
        updateDate();
        //mDateButton.setText(simpleDateFormat.format(mCrime.getDate()));
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                //DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
                choiceDialogOpen();

            }
        });
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Sets the crime solved property
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }
}
