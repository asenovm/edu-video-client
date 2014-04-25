package com.ngm.explaintome;

import android.os.Bundle;
import android.view.Menu;
import android.view.View.OnClickListener;

public class FirstActivity extends BaseActivity {

	private final OnClickListener buttonClickListener = new OnClickListener() {
		public void onClick(android.view.View v) {
			switch (v.getId()) {
			case R.id.first_activity_browse_button:
				launchActvity(BrowseActivity.class);
				break;
			case R.id.first_activity_explain_button:
				launchActvity(ExplainActivity.class);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		findViewById(R.id.first_activity_browse_button).setOnClickListener(buttonClickListener);
		findViewById(R.id.first_activity_explain_button).setOnClickListener(buttonClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.first, menu);
		return true;
	}

}
