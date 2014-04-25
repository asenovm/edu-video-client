package com.ngm.explaintome;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.widget.Toast;

public class ExplainActivity extends BaseActivity {
	private static final int REQUEST_CODE_VIDEO_CAPTURE = 0x998;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explain);

		launchCameraApp();
	}

	private void launchCameraApp() {
		Intent videoCaptureIntent = createVideoIntent();

		final boolean hasCameraApp = videoCaptureIntent.resolveActivity(getPackageManager()) != null;
		if (hasCameraApp) {
			startActivityForResult(videoCaptureIntent, REQUEST_CODE_VIDEO_CAPTURE);
		} else {
			toast("Wow! It seems you have somehow deleted your video taking app, well done!");
		}
	}

	private Intent createVideoIntent() {
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
		if (requestCode == REQUEST_CODE_VIDEO_CAPTURE) {
			if (resultCode == RESULT_OK) {
				Uri videoUri = data.getData();
				onVideoRecorded(videoUri);
			} else if (resultCode == RESULT_CANCELED) {
				onVideoCancelled();
			} else {
				toast("Unknown error! Please check logs!");
			}
		}
	}

	private void onVideoCancelled() {
		toast("Cancelled");

	}

	private void onVideoRecorded(Uri videoUri) {
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2){
			videoUri = Uri.fromFile(generateVideoPath());
		}
		toast("Received video " + videoUri.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explain, menu);
		return true;
	}

}
