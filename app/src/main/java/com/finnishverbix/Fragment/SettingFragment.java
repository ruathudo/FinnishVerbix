package com.finnishverbix.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.finnishverbix.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    CheckBox checkBoxSetTime ;
    TimePicker timePicker;
    View rootView;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        checkBoxSetTime = (CheckBox) rootView.findViewById(R.id.checkBox);
        timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        if(savedInstanceState != null) {
            checkBoxSetTime.setChecked(savedInstanceState.getBoolean("IsCheckedBox"));
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState != null) {
            checkBoxSetTime.setChecked(savedInstanceState.getBoolean("IsCheckedBox"));
        }
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
    private void save(final boolean isChecked) {

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("check", isChecked);
        editor.commit();
    }

    private boolean load() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check", false);
    }
}
