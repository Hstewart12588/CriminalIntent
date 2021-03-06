package com.hmsoly.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Harlan on 4/26/15.
 */
public class ChoiceDialogFragment extends DialogFragment {
    public static final String EXTRA_CHOICE = "com.hmsoly.criminalintent.choice";
    private int mChoice = 0;
    public static final int CHOICE_DATE = 1;
    public static final int CHOICE_TIME = 2;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.choice_picker_message)
                .setPositiveButton(R.string.choice_picked_date, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoice = CHOICE_DATE;
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.choice_picked_time, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoice = CHOICE_TIME;
                        sendResult(Activity.RESULT_OK);
                    }
                });
        return builder.create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment()==null)return;
        Intent i = new Intent();
        i.putExtra(EXTRA_CHOICE,mChoice);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);

    }
}
