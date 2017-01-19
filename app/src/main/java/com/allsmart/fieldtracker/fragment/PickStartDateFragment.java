package com.allsmart.fieldtracker.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by allsmartlt218 on 05-12-2016.
 */

public class PickStartDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pick_a_date_fragment,container,false);
        return view;
    }*/


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        populateSetDate(year, month+1, dayOfMonth);
    }

    public void populateSetDate(int year, int month, int day) {
        Bundle b = new Bundle();
        b.putString("year",String.valueOf(year));
        b.putString("month",String.valueOf(month));
        b.putString("day",String.valueOf(day));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getParentFragment().setArguments(b);
            getFragmentManager().popBackStackImmediate();

        }
    }
}
