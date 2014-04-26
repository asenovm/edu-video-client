package com.ngm.explaintome;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.RestActions;
import com.ngm.explaintome.service.RestActionsImpl;

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

		findViewById(R.id.first_activity_browse_button).setOnClickListener(
				buttonClickListener);
		findViewById(R.id.first_activity_explain_button).setOnClickListener(
				buttonClickListener);

//		testRestActions();
	}

	private void testRestActions() {
		RestActions actions = new RestActionsImpl(new RestConfig());
		actions.getTags(new Callback<List<Tag>>() {

			@Override
			public void call(List<Tag> result) {
				Toast.makeText(FirstActivity.this,
						"woohoo! " + result.toString(), Toast.LENGTH_LONG)
						.show();
			}
		});

		final ArrayList<Tag> tags = new ArrayList<Tag>();
		Tag tag = new Tag();
		tag.setName("aaaaa");
		tags.add(tag);

		actions.putTags(new Callback<List<Tag>>() {
			public void call(java.util.List<Tag> result) {
				Toast.makeText(FirstActivity.this, result.toString(),
						Toast.LENGTH_LONG).show();
			}
		}, tags);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.first, menu);
		return true;
	}

}
