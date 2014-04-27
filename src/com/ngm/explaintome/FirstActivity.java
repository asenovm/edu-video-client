package com.ngm.explaintome;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View.OnClickListener;

import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
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
			case R.id.first_activity_youtube_button:
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Constants.YOUTUBE_CHANNEL_URL);
				startActivity(intent);
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
		findViewById(R.id.first_activity_youtube_button).setOnClickListener(buttonClickListener);

		testRestActions();
	}

	private void testRestActions() {
		final RestActions actions = new RestActionsImpl(new RestConfig());

		final ArrayList<Tag> tags = new ArrayList<Tag>();
		Tag tag = new Tag();
		tag.setName("BBBB");
		tags.add(tag);

		actions.getVideos(tags, new Callback<List<Video>>(){

			@Override
			public void call(List<Video> result) {
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.first, menu);
		return true;
	}

}
