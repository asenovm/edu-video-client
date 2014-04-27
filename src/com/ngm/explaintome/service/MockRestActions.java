package com.ngm.explaintome.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Handler;

import android.widget.EditText;
import com.ngm.explaintome.data.*;

public class MockRestActions implements RestActions {

	@Override
	public void getVideos(List<Tag> tags, Callback<List<Video>> callback) {
		final ArrayList<Tag> tagList = new ArrayList<Tag>();
		Tag tag = new Tag();
		tag.setName("Анализ 1");

		tagList.add(tag);

		final ArrayList<Video> arrayList = new ArrayList<Video>();

		Video video1 = new Video();
		video1.setTitle("Теорема на Рол");
		video1.setDescription("Кратко видео за Теоремата на Рол");
		video1.setTags(tagList);
		video1.setUri("https://www.youtube.com/watch?v=6aOaxcWA2XM");

		Video video2 = new Video();
		video2.setTitle("Теорема на Коши");
		video2.setDescription("Кратко видео за Теоремата на Коши");
		video2.setTags(tagList);
		video2.setUri("https://www.youtube.com/watch?v=HvfQVtSfyAI");

		arrayList.add(video1);
		arrayList.add(video2);

		delay(callback, arrayList);
	}

	@Override
	public void getQuestions(Video video, Callback<List<Question>> callback) {

		final ArrayList<Question> questions = new ArrayList<Question>();

		Question question1 = new Question();

		ArrayList<Answer> answersList = new ArrayList<Answer>();

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
		question1
				.setText("Какви са условията за f(x) за да важи теоремата на Рол?");
		question1.setQuestionType(QuestionType.MULTIPLE_CHOICE.name());
		question1.setAnswers(answersList);

		Question question2 = new Question();

		final Answer answer21 = new Answer();
		answer21.setText("Съществува точка x0, в която f'(x0) = 0.");
		final Answer answer22 = new Answer();
		answer22.setText("Ако функцията е равна краищата на интервала, то тя е монотонна.");

		final Answer answer23 = new Answer();
		answer23.setText("Ако функцията е монотонна, то тя намира своята минимална и максимална стойност в интервала.");

		final Answer answer24 = new Answer();
		answer24.setText("Съществува подинтервал на дадения, в който функцията намира своя локален екстремум.");

		question2.setCorrectAnswer(answer21);
		question2.setTimestamp(12000);
		question2.setText("Какво казва Теоремата на Рол?");
		question2.setQuestionType(QuestionType.MULTIPLE_CHOICE.name());
		question2.setAnswers(Arrays.asList(answer21, answer22, answer23,
				answer24));

		questions.add(question1);
		questions.add(question2);

		delay(callback, questions);
	}

	@Override
	public void getTags(final Callback<List<Tag>> callback) {
		final ArrayList<Tag> arrayList = new ArrayList<Tag>();
		Tag analiz1 = new Tag();
		analiz1.setName("Анализ 1");

		Tag analiz2 = new Tag();
		analiz2.setName("Анализ 2");

		Tag algebra = new Tag();
		algebra.setName("Висша Алгебра");

		arrayList.add(analiz1);
		arrayList.add(analiz2);
		arrayList.add(algebra);

		delay(callback, arrayList);

	}

	private <T> void delay(final Callback<T> callback, final T arrayList) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				callback.call(arrayList);
			}
		}, 50);
	}

	@Override
	public void postTags(Callback<Boolean> callback, List<Tag> tags) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postVideo(Video video, Callback<Video> success) {
		// TODO Auto-generated method stub

	}

}
