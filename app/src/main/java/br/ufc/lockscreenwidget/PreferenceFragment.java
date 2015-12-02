package br.ufc.lockscreenwidget;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PreferenceFragment extends android.preference.PreferenceFragment {


    private static final String PASSWORD = "password";
    private static final String ASK_PASSWORD = "ask_password";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference etPreference =  findPreference(PASSWORD);

        etPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String password = newValue.toString();
                if(password.length() < 4){
                    Toast.makeText(getActivity(), "A senha deve ter pelo menos 4 digitos", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

    }


}