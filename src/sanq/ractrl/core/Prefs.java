package sanq.ractrl.core;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import sanq.ractrl.launcher.R;

public class Prefs extends PreferenceActivity {
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 this.addPreferencesFromResource(R.xml.settings);

         Preference customPref = (Preference) findPreference("cmdClose");
         customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
             public boolean onPreferenceClick(Preference preference) {
                 finish();
                 return true;
             }
         });
     }

 public static String getIp(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("ip", "192.168.0.1");
    }


public static int getPort(Context context) {
	return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("port", "1935"));
}

    public static int getWifi(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("wifi", "1"));
    }

}
