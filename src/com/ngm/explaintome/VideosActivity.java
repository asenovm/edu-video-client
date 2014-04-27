package com.ngm.explaintome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;

/**
 * Created by cpt2kan on 4/26/14.
 */
public class VideosActivity extends Activity {
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videos);

		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String id = intent.getStringExtra("id");

		Tag tag = new Tag();
		tag.setName(name);
		tag.setId(id);

		progressBar = (ProgressBar) findViewById(R.id.videosProgressBar);
		RestActions restActions = new MockRestActions();
		List<Tag> listTag = new ArrayList<Tag>();
		listTag.add(tag);
		onRestOperationStart();
		restActions.getVideos(listTag, new Callback<List<Video>>() {
			@Override
			public void call(List<Video> result) {
				final ListView listview = (ListView) findViewById(R.id.videosListView);

				final ArrayList<String> list = new ArrayList<String>();

				for (int i = 0; i < result.size(); ++i) {
					list.add(result.get(i).getTitle());
				}


                listview.setAdapter(new VideoAdapter(result));
				listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {
                        Video video = (Video) adapterView.getAdapter().getItem(position);
						final Intent intent = new Intent(VideosActivity.this,
								VideoViewActivity.class);
                        intent.putExtra("title",video.getTitle());
                        intent.putExtra("description",video.getDescription());
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

	private void onRestOperationStart() {
		progressBar.setVisibility(View.VISIBLE);
	}

	private void onRestOperationEnd() {
		progressBar.setVisibility(View.GONE);

	}

    private final class VideoAdapter extends BaseAdapter {
        public final List<Video> video;

        public VideoAdapter(List<Video> video){
            this.video = video;
        }

        @Override
        public int getCount() {
            return video.size();
        }

        @Override
        public Object getItem(int i) {
            return video.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public LinearLayout getView(int i, View view, ViewGroup viewGroup) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(VideosActivity.this).inflate(R.layout.activity_video_list, null);

            TextView textView2 =  (TextView) linearLayout.findViewById(R.id.videoListTextView2);
            TextView textView1 =  (TextView) linearLayout.findViewById(R.id.videoListTextView1);

            textView1.setText(video.get(i).getTitle());
            textView2.setText(video.get(i).getDescription());

            return linearLayout;
        }
    }
}