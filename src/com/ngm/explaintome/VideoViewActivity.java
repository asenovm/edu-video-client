package com.ngm.explaintome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.ngm.explaintome.YoutubeUrlAsyncTask.VideoTaskListener;

public class VideoViewActivity extends Activity implements VideoTaskListener {

	private VideoView videoView;
    final CharSequence[] items = {"Добре","Зле","Горе-Долу"};


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);

		final YoutubeUrlAsyncTask task = new YoutubeUrlAsyncTask(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if(title.equals("Хорнови Дизюнкти"))
		    task.execute("https://www.youtube.com/watch?v=6aOaxcWA2XM");
        else
            task.execute("https://www.youtube.com/watch?v=HvfQVtSfyAI");
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

        videoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                videoView.pause();
                final AlertDialog.Builder ad = new AlertDialog.Builder(VideoViewActivity.this);
                ad.setTitle("Как си ти?");
                ad.setSingleChoiceItems(items, -1,  new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Toast.makeText(getApplicationContext(),
                                "You Choose : " + items[arg1],
                                Toast.LENGTH_LONG).show();

                    arg0.cancel();
                        videoView.start();

                    }
                });
                ad.show();
            }

        }, 8000);

	}

    private static class myCustomAlertDialog extends AlertDialog {

        protected myCustomAlertDialog(Context context) {
            super(context);

            setTitle("Profile");

            Button connect = new Button(getContext());
            setView(connect);
            connect.setText("Don't push me");
            connect.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // I want the dialog to close at this point
                    dismiss();
                }
            });
        }

    }
}

