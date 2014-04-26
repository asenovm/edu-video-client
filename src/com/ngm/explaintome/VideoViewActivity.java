package com.ngm.explaintome;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ngm.explaintome.YoutubeUrlAsyncTask.VideoTaskListener;

public class VideoViewActivity extends Activity implements VideoTaskListener {

	private VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);

		final YoutubeUrlAsyncTask task = new YoutubeUrlAsyncTask(this);
		task.execute("https://www.youtube.com/watch?v=zyEednV9A94");

		videoView = (VideoView) findViewById(R.id.video_view);

		final MediaController controller = new MediaController(this);
		controller.setMediaPlayer(videoView);
		controller.setAnchorView(videoView);

		videoView.setMediaController(controller);
	}

	@Override
	public void onUrlRetrieved(String url) {
		videoView.setVideoURI(Uri.parse(url));
		videoView.start();
	}
}
