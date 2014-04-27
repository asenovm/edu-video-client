package com.ngm.explaintome.service;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;

public class MockRestActions implements RestActions {

	@Override
	public void getVideos(List<Tag> tags, Callback<List<Video>> callback) {
        final ArrayList<Tag> tagList = new ArrayList<Tag>();
        Tag logichesko = new Tag();
        logichesko.setName("Логическо Програмиране");


        tagList.add(logichesko);

        final ArrayList<Video> arrayList = new ArrayList<Video>();

        Video logicheskoVideo1 = new Video();
        logicheskoVideo1.setTitle("Хорнови Дизюнкти");
        logicheskoVideo1.setDescription("Надънихте се на големия кур");
        logicheskoVideo1.setTags(tagList);
        logicheskoVideo1.setUri("https://www.youtube.com/watch?v=6aOaxcWA2XM");

        Video logicheskoVideo2 = new Video();
        logicheskoVideo2.setTitle("Либерална резолюция");
        logicheskoVideo2.setDescription("Надънихте се на още по-големия кур");
        logicheskoVideo2.setTags(tagList);
        logicheskoVideo2.setUri("https://www.youtube.com/watch?v=HvfQVtSfyAI");

        arrayList.add(logicheskoVideo1);
        arrayList.add(logicheskoVideo2);

		delay(callback,arrayList);
	}

	@Override
	public void getQuestions(Video video, Callback<List<Question>> callback) {
		delay(callback, new ArrayList<Question>());
	}

	@Override
	public void getTags(final Callback<List<Tag>> callback) {
		final ArrayList<Tag> arrayList = new ArrayList<Tag>();
		Tag logichesko = new Tag();
		logichesko.setName("Логическо Програмиране");

		Tag chislen = new Tag();
		chislen.setName("Числен Анализ");

		Tag algebra = new Tag();
		algebra.setName("Висша Алгебра");

		arrayList.add(logichesko);
		arrayList.add(chislen);
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
	public void putTags(Callback<Boolean> callback, List<Tag> tags) {
		// TODO Auto-generated method stub
		
	}

}
