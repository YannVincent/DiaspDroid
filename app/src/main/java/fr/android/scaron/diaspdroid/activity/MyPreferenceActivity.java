package fr.android.scaron.diaspdroid.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import fr.android.scaron.diaspdroid.R;

public class MyPreferenceActivity extends PreferenceActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.myprefs);
    }
}