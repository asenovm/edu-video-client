package com.ngm.explaintome;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.ngm.explaintome.YoutubeUrlAsyncTask.VideoTaskListener;
import com.ngm.explaintome.data.Answer;
import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;

public class VideoViewActivity extends Activity implements VideoTaskListener {

	private VideoView videoView;

	private RestActions restActions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);

		videoView = (VideoView) findViewById(R.id.video_view);

		restActions = new MockRestActions();

		final YoutubeUrlAsyncTask task = new YoutubeUrlAsyncTask(this);

		Intent intent = getIntent();
		String name = intent.getStringExtra("title");

		task.execute("https://www.youtube.com/watch?v=zyEednV9A94");

		final MediaController controller = new MediaController(this);
		controller.setMediaPlayer(videoView);
		controller.setAnchorView(videoView);

		videoView.setMediaController(controller);
	}

	private void showDialog(final String title, final CharSequence[] items,
			final List<Question> questions, final int questionIndex,
			final int videoPosition) {
		final AlertDialog.Builder ad = new AlertDialog.Builder(
				VideoViewActivity.this);
		ad.setTitle(title);
		ad.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int position) {

						Toast.makeText(getApplicationContext(),
								"You Chose : " + items[position],
								Toast.LENGTH_LONG).show();
						dialog.cancel();
						videoView.start();
						startNextQuestion(questions, questionIndex,
								videoPosition);
					}
				});
		videoView.pause();
		ad.show();
	}

	private void startNextQuestion(final List<Question> questions,
			final int index, final int videoTime) {
		if (index >= questions.size()) {
			return;
		}
		final Question question = questions.get(index);
		videoView.postDelayed(new Runnable() {

			@Override
			public void run() {
				String[] items = new String[question.getAnswers().size()];
				final List<Answer> answers = question.getAnswers();
				for (int i = 0; i < answers.size(); ++i) {
					items[i] = answers.get(i).getText();
				}

				showDialog(question.getText(), items, questions, index + 1,
						videoView.getCurrentPosition());
			}

		}, question.getTimestamp() - videoTime);
	}

	@Override
	public void onUrlRetrieved(String url) {
		videoView.setVideoURI(Uri.parse(url));
		videoView.start();
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(final MediaPlayer mp) {
				restActions.getQuestions(null, new Callback<List<Question>>() {

					@Override
					public void call(final List<Question> questions) {
						if (questions.isEmpty()) {
							return;
						}

						final Question question = questions.get(0);
						videoView.postDelayed(new Runnable() {

							@Override
							public void run() {
								String[] items = new String[question
										.getAnswers().size()];
								final List<Answer> answers = question
										.getAnswers();
								for (int i = 0; i < answers.size(); ++i) {
									items[i] = answers.get(i).getText();
								}

								showDialog(question.getText(), items,
										questions, 1,
										videoView.getCurrentPosition());
							}

						}, question.getTimestamp());
					}
				});
			}
		});
	}
}
