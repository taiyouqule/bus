package tongji.edu.activity;

import tongji.edu.util.AllActivity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Window;

/**
 * 参数设置模块，可以设置ip，port
 * 
 * @author zlj
 * 
 */
public class Preferences extends PreferenceActivity {

	private static final String PREFERENCES = "Preferences";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.d(PREFERENCES, "Preferences->onCreate");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs); // 相当于setContentView
		AllActivity allActivity = AllActivity.getInstance();
		allActivity.addActivity(this);
	}

	@Override
	protected void onDestroy() {

		Log.d(PREFERENCES, "Preferences->onDestroy");
		super.onDestroy();

	}

	@Override
	protected void onStop() {

		Log.d(PREFERENCES, "Preferences->onStop");
		super.onStop();

	}

	@Override
	protected void onPause() {

		Log.d(PREFERENCES, "Preferences->onPause");
		super.onPause();

	}

	@Override
	protected void onRestart() {

		Log.d(PREFERENCES, "Preferences->onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {

		Log.d(PREFERENCES, "Preferences->onStart");
		super.onStart();
	}
}
