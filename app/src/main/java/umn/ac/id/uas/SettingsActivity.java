package umn.ac.id.uas;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {


    public static final String KEY_PREF_NOTIFICATION_SWITCH = "NotificationSwitch";
    public static final String KEY_PREF_FINGERPRINT_SWITCH = "FingerprintSwitch";

    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        setContentView(R.layout.settings_activity);
        frameLayout = findViewById( R.id.settings );

        if(frameLayout != null){

            if(savedInstanceState!=null){
                return;
            }

            getSupportFragmentManager().beginTransaction().add( R.id.settings, new SettingsFragment()).commit();
        }

        /*getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.settings, new SettingsFragment() )
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }*/
    }

   /* public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource( R.xml.root_preferences, rootKey );
        }
    }*/
}