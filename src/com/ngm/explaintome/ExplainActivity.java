package com.ngm.explaintome;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ngm.explaintome.data.Answer;
import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.QuestionType;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.RestActions;
import com.ngm.explaintome.service.RestActionsImpl;

public class ExplainActivity extends BaseActivity {
	private static final int REQUEST_CODE = 0x998;

	private static final String TAG = ExplainActivity.class.getSimpleName();

	private VideoFetchingState videoState = new VideoFetchingState();

	private VideoView videoView;

	private final android.content.DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			switch (arg1) {
			case Dialog.BUTTON_POSITIVE:
				arg0.dismiss();
				videoView.start();

				break;
			case Dialog.BUTTON_NEGATIVE:
				arg0.dismiss();
				videoView.start();

				break;

			default:
				break;
			}
		}
	};

	private final OnClickListener buttonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.explain_activity_make_remark:
				createModalDialog().show();
				videoView.pause();
				break;

			default:
				break;
			}

		}

		private Dialog createModalDialog() {
			final AlertDialog.Builder builder = new AlertDialog.Builder(ExplainActivity.this);
			builder.setTitle(ExplainActivity.this.getString(R.string.hello_world));
			LinearLayout inflate = (LinearLayout) LayoutInflater.from(ExplainActivity.this).inflate(R.layout.dialog_layout, null);

			final String textAt = ExplainActivity.this.getString(R.string.dialog_video_at);
			final int position = videoView.getCurrentPosition() / 1000;
			((TextView) inflate.findViewById(R.id.dialog_video_time)).setText(textAt + " " + String.format("%02d", position / 60) +  ":" + String.format("%02d", position % 60));

			OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
					if (isChecked)
						((View) buttonView.getParent()).setBackgroundColor(0x70007000);
					else {
						((View) buttonView.getParent()).setBackgroundColor(Color.TRANSPARENT);
					}
				} 
			};

			attachToEveryCheckBox(inflate, onCheckedChangeListener);
			builder.setView(inflate);
			builder.setPositiveButton("OK", dialogClickListener);
			builder.setNegativeButton("Cancel", dialogClickListener);
			return builder.create();
		}

		private void attachToEveryCheckBox(ViewGroup inflate, OnCheckedChangeListener onCheckedChangeListener) {
			for (int i = 0; i < inflate.getChildCount(); i++) {
				View childAt = inflate.getChildAt(i);
				if (childAt instanceof CheckBox) {
					((CheckBox) childAt).setOnCheckedChangeListener(onCheckedChangeListener);
				}
				if (childAt instanceof ViewGroup) {
					attachToEveryCheckBox((ViewGroup) childAt, onCheckedChangeListener);
				}
				childAt.setOnClickListener(buttonClickListener);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explain);

		findViewById(R.id.explain_activity_make_remark).setOnClickListener(buttonClickListener);
		videoView = (VideoView) findViewById(R.id.explain_activity_video_view);

		Log.d(TAG, videoState.toString());

		// at the end of the queue!
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				if (!videoState.hasVideo() && !videoState.isCancelled()) {
					Intent multiChoiceIntent = createIntentChooser();
					startActivityForResult(multiChoiceIntent, REQUEST_CODE);
				}

			}
		});

	}

	private Intent createIntentChooser() {
		Intent videoCaptureIntent = createRecordIntent();

		final boolean hasCameraApp = videoCaptureIntent.resolveActivity(getPackageManager()) != null;
		if (!hasCameraApp) {
			toast("Wow! It seems you have somehow deleted your video taking app, well done!");
		}

		Intent videoFromGallery = createGalleryIntent();

		// XXX fixme text out of strings.xml
		Intent chooserIntent = Intent.createChooser(videoCaptureIntent, "Capture or pick from Gallery?");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { videoFromGallery });
		return chooserIntent;
	}

	private Intent createGalleryIntent() {
		Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		i.setType("video/mpeg");
		return i;
	}

	private Intent createRecordIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		// fix for a horrible 4.3 bug
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
			File videoFile = generateVideoPath();

			takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
		}

		return takeVideoIntent;
	}

	private File generateVideoPath() {
		final String videoTitle = "ExplainIt_video" + ".mp4";
		final File galleryPath = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DCIM);
		File videoFile = new File(galleryPath, videoTitle);
		return videoFile;
	}

	private void toast(String what) {
		Toast.makeText(this, what, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri videoUri = data.getData();
				onVideoReceived(videoUri);
			} else if (resultCode == RESULT_CANCELED) {
				onCancelled();
			} else {
				toast("Unknown error! Please check logs!");
			}
		}
	}

	private void onCancelled() {
		videoState.cancel();
		toast("Cancelled");
		finish();
	}

	private void onVideoReceived(Uri videoUri) {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
			videoUri = Uri.fromFile(generateVideoPath());
		}
		videoState.setVideoUri(videoUri);
		videoView.setVideoURI(videoUri);

		videoView.setMediaController(new MediaController(ExplainActivity.this));
		videoView.start();

		Video video = new Video();
		video.setUri(videoUri.toString());

		// TODO UI for title and description
		video.setTitle("title");
		video.setDescription("description");

		final ArrayList<Question> questions = new ArrayList<Question>();
		for (int i = 0; i < 4; i++) {
			final Question question = new Question();
			question.setTimestamp((long) (Math.random() * 100000));
			question.setText("Question number " + i);
			final List<Answer> answers = new ArrayList<Answer>();
			for (int j = 0; j < 4; j++) {
				Answer answer = new Answer();
				answer.setText("Answer number " + j + "");

				answers.add(answer);
			}

			question.setAnswers(answers);
			question.setCorrectAnswer(answers.get(2));
			question.setQuestionType(QuestionType.MULTIPLE_CHOICE.name());

			questions.add(question);
		}

		video.setQuestions(questions);

		final ArrayList<Tag> tagList = new ArrayList<Tag>();
		Tag logichesko = new Tag();
		logichesko.setName("logichesko");

		Tag chislen = new Tag();
		chislen.setName("Chislen");

		Tag algebra = new Tag();
		algebra.setName("Visha algebra");

		tagList.add(logichesko);
		tagList.add(chislen);
		tagList.add(algebra);

		video.setTags(tagList);

		RestActions restActions = new RestActionsImpl(new RestConfig());
		// restActions.postVideo(video, new Callback<Video>() {
		//
		// @Override
		// public void call(Video result) {
		// Log.d(TAG, result.toString());
		// }
		// });

		Log.d(TAG, videoUri.toString());
		toast("Received video " + videoUri.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explain, menu);
		return true;
	}

	private static class VideoFetchingState {
		private Uri videoUri;
		private boolean isCancelled;

		public void setCancelled(boolean cancelled) {
			this.isCancelled = cancelled;
		}

		public void cancel() {
			setCancelled(true);
		}

		public void setVideoUri(Uri videoUri) {
			this.videoUri = videoUri;
		}

		public boolean isCancelled() {
			return isCancelled;
		}

		public boolean hasVideo() {
			return videoUri != null;
		}

		@Override
		public String toString() {
			return "VideoFetchingState [videoUri=" + videoUri + ", isCancelled=" + isCancelled + "]";
		}
	}

}
