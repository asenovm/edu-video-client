package com.ngm.explaintome;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class ExplainActivity extends BaseActivity {
	private static final int REQUEST_CODE = 0x998;

	private static final String TAG = ExplainActivity.class.getSimpleName();
	
	private VideoFetchingState videoState = new VideoFetchingState();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explain);
		
		Log.d(TAG, videoState.toString());
		if (!videoState.hasVideo() && !videoState.isCancelled()){
			Intent multiChoiceIntent = createIntentChooser();
			startActivityForResult(multiChoiceIntent, REQUEST_CODE);
		}

	}
	

	private Intent createIntentChooser() {
		Intent videoCaptureIntent = createRecordIntent();

		final boolean hasCameraApp = videoCaptureIntent.resolveActivity(getPackageManager()) != null;
		if (!hasCameraApp) {
			toast("Wow! It seems you have somehow deleted your video taking app, well done!");
		}

		Intent videoFromGallery = createGalleryIntent();
		
		//XXX fixme text out of strings.xml
		Intent chooserIntent = Intent.createChooser(videoCaptureIntent, "Capture or pick from Gallery?");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {videoFromGallery});
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

	}

	private void onVideoReceived(Uri videoUri) {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2){
			videoUri = Uri.fromFile(generateVideoPath());
		}
		videoState.setVideoUri(videoUri);
		Log.d(TAG, videoUri.toString());
		toast("Received video " + videoUri.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explain, menu);
		return true;
	}
	
	private static class VideoFetchingState{
		private Uri videoUri;
		private boolean isCancelled;
		
		public void setCancelled(boolean cancelled){
			this.isCancelled = cancelled;
		}
		
		public void cancel(){
			setCancelled(true);
		}
		
		public void setVideoUri(Uri videoUri){
			this.videoUri = videoUri;
		}
		
		public boolean isCancelled(){
			return isCancelled;
		}
		
		public boolean hasVideo(){
			return videoUri != null;
		}
		@Override
		public String toString() {
			return "VideoFetchingState [videoUri=" + videoUri
					+ ", isCancelled=" + isCancelled + "]";
		}
		
	}
	
}
