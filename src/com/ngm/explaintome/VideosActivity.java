package com.ngm.explaintome;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;

/**
 * Created by cpt2kan on 4/26/14.
 */
public class VideosActivity extends BaseListActivity {
    private Intent intent;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videos);

		searchView = (SearchView) findViewById(R.id.videosSearchView);

		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String id = intent.getStringExtra("id");

		Tag tag = new Tag();
		tag.setName(name);
		tag.setId(id);

		progressBar = (ProgressBar) findViewById(R.id.videosProgressBar);
		listView = (ListView) findViewById(R.id.videosListView);
		RestActions restActions = new MockRestActions();
		List<Tag> listTag = new ArrayList<Tag>();
		listTag.add(tag);
		onRestOperationStart();
		restActions.getVideos(listTag, new Callback<List<Video>>() {
			@Override
			public void call(List<Video> result) {

				final TextView emptyView = new TextView(VideosActivity.this);
				emptyView.setText("Nothing to show here, move along");
				emptyView.setBackgroundColor(Color.RED);
				emptyView.setWidth(200);
				emptyView.setHeight(200);
				listView.setEmptyView(emptyView);

				final ArrayList<String> list = new ArrayList<String>();

				for (int i = 0; i < result.size(); ++i) {
					list.add(result.get(i).getTitle());
				}

				final VideoAdapter adapter = new VideoAdapter(result);
				listView.setAdapter(adapter);

				searchView.setOnQueryTextListener(new OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String query) {
						adapter.getFilter().filter(query);
						return true;
					}

					@Override
					public boolean onQueryTextChange(String newText) {
						adapter.getFilter().filter(newText);
						return true;
					}
				});
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						Video video = (Video) adapterView.getAdapter().getItem(
								position);
						final Intent intent = new Intent(VideosActivity.this,
								VideoViewActivity.class);
						intent.putExtra("title", video.getTitle());
                        intent.putExtra("Uri",video.getVideoUri());
                        List<Question> questions = video.getQuestions();
                        for(int i = 0; i < questions.size(); i++)
                            intent.putExtra("question"+i,questions.get(i).getId());
						startActivity(intent);

					}
				});
				onRestOperationEnd();

			}
		});

	}


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse, menu);
		return true;
	}

	private final class VideoAdapter extends BaseListAdapter<Video> implements
			Filterable {

		protected VideoAdapter(List<Video> entities) {
			super(entities);
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			RelativeLayout layout = (RelativeLayout) LayoutInflater.from(
					VideosActivity.this).inflate(R.layout.activity_video_list,
					null, false);

			TextView textView2 = (TextView) layout
					.findViewById(R.id.videoListTextView2);
			TextView textView1 = (TextView) layout
					.findViewById(R.id.videoListTextView1);

			textView1.setText(entities.get(i).getTitle());
			textView2.setText(entities.get(i).getDescription());

			return layout;
		}

	}
}