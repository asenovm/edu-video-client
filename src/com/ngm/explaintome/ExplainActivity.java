package com.ngm.explaintome;

import java.io.File;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ngm.explaintome.data.Answer;
import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.QuestionType;
import com.ngm.explaintome.data.Video;

public class ExplainActivity extends BaseActivity {
	private static final int REQUEST_CODE = 0x998;

	private static final String TAG = ExplainActivity.class.getSimpleName();

	private VideoState videoState = new VideoState();

	private final android.content.DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			AlertDialog dialog = (AlertDialog) arg0;
			switch (arg1) {
			case Dialog.BUTTON_POSITIVE:
				arg0.dismiss();
				videoState.play();
				
				Question question = new Question();
				question.setQuestionType(QuestionType.MULTIPLE_CHOICE.name());
				
				question.setTimestamp(videoState.getVideoView().getCurrentPosition());
				EditText inputQuestion = ((EditText) dialog.findViewById(R.id.dialog_input_question));
				question.setText(inputQuestion.getText().toString());
				
				Answer answer1 = new Answer();
				Answer answer2 = new Answer();
				Answer answer3 = new Answer();
				Answer answer4 = new Answer();
				
				final String text1 = ((EditText) dialog.findViewById(R.id.dialog_answer1)).getText().toString();
				final String text2 = ((EditText) dialog.findViewById(R.id.dialog_answer1)).getText().toString();
				final String text3 = ((EditText) dialog.findViewById(R.id.dialog_answer1)).getText().toString();
				final String text4 = ((EditText) dialog.findViewById(R.id.dialog_answer1)).getText().toString();
				
				final boolean checked1 = ((CheckBox) dialog.findViewById(R.id.dialog_checkbox1)).isChecked();
				final boolean checked2 = ((CheckBox) dialog.findViewById(R.id.dialog_checkbox1)).isChecked();
				final boolean checked3 = ((CheckBox) dialog.findViewById(R.id.dialog_checkbox1)).isChecked();
				final boolean checked4 = ((CheckBox) dialog.findViewById(R.id.dialog_checkbox1)).isChecked();
				
				answer1.setText(text1);
				answer2.setText(text2);
				answer3.setText(text3);
				answer4.setText(text4);
				
				question.getAnswers().add(answer1);
				question.getAnswers().add(answer2);
				question.getAnswers().add(answer3);
				question.getAnswers().add(answer4);
				
				if (checked1) question.setCorrectAnswer(answer1);
				if (checked2) question.setCorrectAnswer(answer2);
				if (checked3) question.setCorrectAnswer(answer3);
				if (checked4) question.setCorrectAnswer(answer4);
				
				videoState.getVideo().getQuestions().add(question);
				
				
				break;
			case Dialog.BUTTON_NEGATIVE:
				arg0.dismiss();
				videoState.play();

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
				videoState.pause();
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
			final int position = videoState.getVideoView().getCurrentPosition() / 1000;
			((TextView) inflate.findViewById(R.id.dialog_video_time)).setText(textAt + " " + String.format("%02d", position / 60) + ":"
					+ String.format("%02d", position % 60));

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
		VideoView videoView = (VideoView) findViewById(R.id.explain_activity_video_view);
		videoState.setVideoView(videoView);

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
		
		videoState.onVideoReceived(videoUri);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explain, menu);
		return true;
	}

	private  class VideoState {

		private Video video = new Video();
		private VideoView videoView;
		private boolean isCancelled;
		private Uri videoUri;

		public void setVideoView(VideoView v) {
			this.videoView = v;
		}

		public void onVideoReceived(Uri videoUri2) {
			setVideoUri(videoUri2);
			
			videoView.setMediaController(new MediaController(ExplainActivity.this));
			play();
			
			
//			Video video = new Video();
//			video.setUri(videoUri.toString());
//
//			// TODO UI for title and description
//			video.setTitle("title");
//			video.setDescription("description");
//
//			final ArrayList<Question> questions = new ArrayList<Question>();
//			for (int i = 0; i < 4; i++) {
//				final Question question = new Question();
//				question.setTimestamp((long) (Math.random() * 100000));
//				question.setText("Question number " + i);
//				final List<Answer> answers = new ArrayList<Answer>();
//				for (int j = 0; j < 4; j++) {
//					Answer answer = new Answer();
//					answer.setText("Answer number " + j + "");
//
//					answers.add(answer);
//				}
//
//				question.setAnswers(answers);
//				question.setCorrectAnswer(answers.get(2));
//				question.setQuestionType(QuestionType.MULTIPLE_CHOICE.name());
//
//				questions.add(question);
//			}
//
//			video.setQuestions(questions);
//
//			final ArrayList<Tag> tagList = new ArrayList<Tag>();
//			Tag logichesko = new Tag();
//			logichesko.setName("logichesko");
//
//			Tag chislen = new Tag();
//			chislen.setName("Chislen");
//
//			Tag algebra = new Tag();
//			algebra.setName("Visha algebra");
//
//			tagList.add(logichesko);
//			tagList.add(chislen);
//			tagList.add(algebra);
//
//			video.setTags(tagList);
//
//			RestActions restActions = new RestActionsImpl(new RestConfig());
			// restActions.postVideo(video, new Callback<Video>() {
			//
			// @Override
			// public void call(Video result) {
			// Log.d(TAG, result.toString());
			// }
			// });

			Log.d(TAG, videoUri.toString());

		}

		public VideoView getVideoView() {
			return videoView;
		}

		public void play() {
			videoView.start();
		}
		
		public void pause(){
			videoView.pause();
		}

		public Video getVideo() {
			return video;
		}

		public void setCancelled(boolean cancelled) {
			this.isCancelled = cancelled;
		}

		public void cancel() {
			setCancelled(true);
		}

		public void setVideoUri(Uri videoUri) {
			// FIXME keeping URI in two places, not sure if it will be seemless
			// to remove one of them...
			this.videoUri = videoUri;
			videoView.setVideoURI(videoUri);
			video.setUri(videoUri.toString());
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
