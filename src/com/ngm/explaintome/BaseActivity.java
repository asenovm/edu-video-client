package com.ngm.explaintome;

import android.app.Activity;
import android.content.Intent;

public class BaseActivity extends Activity {

	protected void launchActvity(Class<? extends Activity> activityClass) {
		Intent intent = new Intent(this, activityClass);
		startActivity(intent);
	}

}
