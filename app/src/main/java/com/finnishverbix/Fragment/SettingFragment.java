package com.finnishverbix.Fragment;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import com.finnishverbix.AlarmReceiver;
import com.finnishverbix.R;

import java.util.Calendar;


/**
 * A Setting Fragment.
 */
public class SettingFragment extends Fragment {
    View rootView;

    //items for time setting
    CheckBox checkBoxSetTime ;
    TimePicker timePicker;
    Button btnTimeSetting;
    TimePickerDialog timePickerDialog;
    Calendar calSet;


    //Create an boardcast daily notification
    AlarmManager alarmManager;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        //Mapping the variable with the view
        checkBoxSetTime = (CheckBox) rootView.findViewById(R.id.checkBox);
        btnTimeSetting = (Button) rootView.findViewById(R.id.buttonTime);

        final boolean isChecked = load();
        //Handling the time is set
        checkBoxSetTime.setChecked(isChecked);
        checkBoxSetTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                save(isChecked);
            }
        });
        //handling the button setting time.
        btnTimeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePickerDialog(true);
            }
        });

        return rootView;
    }

    //Open the time picker dialog and set time here.
    private void openTimePickerDialog(boolean b) {
        final Calendar calendar = Calendar.getInstance();
        //Time picker dialog
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calNow = Calendar.getInstance();
                 calSet= (Calendar) calNow.clone();
                //Set time
                calSet.set(Calendar.HOUR_OF_DAY,hour);
                calSet.set(Calendar.MINUTE,minute);
                calSet.set(Calendar.SECOND,0);
                calSet.set(Calendar.MILLISECOND,0);
                //Set alarm
                alarmManager = (AlarmManager) rootView.getContext().getSystemService(Context.ALARM_SERVICE);
                alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), getInterval(), pendingIntent);
            }
        }
        ,calendar.get(Calendar.HOUR_OF_DAY)
        ,calendar.get(Calendar.MINUTE),
                b);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private int getInterval() {
        return 1* 24*60*60*1000; // day* hours * minutes* seconds * milliseconds
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IsCheckedBox",checkBoxSetTime.isChecked());
    }

    @Override
    public void onPause() {
        super.onPause();
        save(checkBoxSetTime.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    //Save the status of the check box even the app is closed
    private void save(final boolean isChecked) {

        SharedPreferences sharedPreferences = getActivity().getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check", isChecked);
        editor.commit();
    }
    // Load the status of the check box.
    private boolean load() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check", false);
    }
}
