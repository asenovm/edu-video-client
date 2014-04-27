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
import android.widget.*;

import com.ngm.explaintome.YoutubeUrlAsyncTask.VideoTaskListener;
import com.ngm.explaintome.data.*;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;

import java.util.ArrayList;
import java.util.List;

public class VideoViewActivity extends Activity implements VideoTaskListener {

	private VideoView videoView;
    private List<Question> questionList;
    int counter = 0;
    private CharSequence[] items;
    Question question1 = new Question();
    ArrayList<Answer> answersList = new ArrayList<Answer>();
    Question question2 = new Question();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);

		final YoutubeUrlAsyncTask task = new YoutubeUrlAsyncTask(this);

        Answer answer1 = new Answer();
        answer1.setText("Непрекъсната и диференцирана в съответния интервал.");

        Answer answer2 = new Answer();
        answer2.setText("Непрекъсната и ограничена в интервала.");

        Answer answer3 = new Answer();
        answer3.setText("Неограничена в разширения интервал.");

        Answer answer4 = new Answer();
        answer4.setText("f(x) е дефинирана в домейна на комплексните числа.");

        answersList.add(answer1);
        answersList.add(answer2);
        answersList.add(answer3);
        answersList.add(answer4);


        question1.setCorrectAnswer(answer1);
        question1.setTimestamp(5000);
        question1.setText("Какви са условията за f(x) за да важи теоремата на Рол?");
        question1.setQuestionType(QuestionType.MULTIPLE_CHOICE.name());
        question1.setAnswers(answersList);
        answersList.clear();


        answer1.setText("Съществува точка x0, в която f'(x0) = 0.");
        answer2.setText("Ако функцията е равна краищата на интервала, то тя е монотонна.");
        answer3.setText("Ако функцията е монотонна, то тя намира своята минимална и максимална стойност в интервала.");
        answer4.setText("Съществува подинтервал на дадения, в който функцията намира своя локален екстремум.");

        answersList.add(answer1);
        answersList.add(answer2);
        answersList.add(answer3);
        answersList.add(answer4);

        question2.setCorrectAnswer(answer1);
        question2.setTimestamp(12000);
        question2.setText("Какво казва Теоремата на Рол?");
        question2.setQuestionType(QuestionType.MULTIPLE_CHOICE.name());
        question2.setAnswers(answersList);


        questionList.add(question1);
        questionList.add(question2);

        Intent intent = getIntent();
        String name = intent.getStringExtra("title");
        if(name.equals("Теорема на Рол."))
            task.execute("https://www.youtube.com/watch?v=mk48xRzuNvA");
        else
            task.execute("https://www.youtube.com/watch?v=gV1-M4uRiiw");
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
        final Question question = new Question();
        if(counter==0)
        {
            question.setCorrectAnswer(question1.getCorrectAnswer());
            question.setTimestamp(question1.getTimestamp());
            question.setText(question1.getText());
            question.setAnswers(question1.getAnswers());
            counter++;


        }
        else if(counter==1)
        {
            question.setCorrectAnswer(question2.getCorrectAnswer());
            question.setTimestamp(question2.getTimestamp());
            question.setText(question2.getText());
            question.setAnswers(question2.getAnswers());
            counter++;
        }
             int i=0;
            items = new CharSequence[4];
             for(Answer answer: question.getAnswers())
             {
                items[i]=answer.getText();
                 i++;
             }
                            videoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                videoView.pause();
                final AlertDialog.Builder ad = new AlertDialog.Builder(VideoViewActivity.this);
                ad.setTitle(question.getText());
                ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

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

}

