package com.ngm.explaintome;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;

/**
 * Created by cpt2kan on 4/26/14.
 */
public class VideosActivity extends BaseListActivity {
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
						intent.putExtra("description", video.getDescription());
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

	private final class VideoAdapter extends BaseAdapter implements Filterable {
		public List<Video> videos;

		private List<Video> originalVideos;

		private class VideoFilter extends Filter {

			private List<Video> getFilteredVideos(final String query) {
				final List<Video> result = new LinkedList<Video>();
				for (final Video video : originalVideos) {
					final String title = video.getTitle().toLowerCase(
							Locale.getDefault());
					if (title.contains(query.toLowerCase(Locale.getDefault()))) {
						result.add(video);
					}
				}
				return result;
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults results = new FilterResults();
				results.values = getFilteredVideos(constraint.toString());
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				final List<Video> videos = (List<Video>) results.values;
				VideoAdapter.this.videos = videos;
				notifyDataSetChanged();
			}

		}

		public VideoAdapter(List<Video> videos) {
			this.videos = videos;
			originalVideos = videos;
		}

		@Override
		public int getCount() {
			return videos.size();
		}

		@Override
		public Object getItem(int i) {
			return videos.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public LinearLayout getView(int i, View view, ViewGroup viewGroup) {
			LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(
					VideosActivity.this).inflate(R.layout.activity_video_list,
					null, false);

			TextView textView2 = (TextView) linearLayout
					.findViewById(R.id.videoListTextView2);
			TextView textView1 = (TextView) linearLayout
					.findViewById(R.id.videoListTextView1);

			textView1.setText(videos.get(i).getTitle());
			textView2.setText(videos.get(i).getDescription());

			return linearLayout;
		}

		@Override
		public Filter getFilter() {
			return new VideoFilter();
		}
	}
}