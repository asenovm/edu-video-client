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
        logichesko.setName("logichesko");


        tagList.add(logichesko);

        final ArrayList<Video> arrayList = new ArrayList<Video>();
        Video logicheskoVideo1 = new Video();
        logicheskoVideo1.setTitle("Hornovi Diziunkti");
        logicheskoVideo1.setTags(tagList);
        Video logicheskoVideo2 = new Video();
        logicheskoVideo2.setTitle("Liberalna rezoliuciq");
        logicheskoVideo2.setTags(tagList);

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
		logichesko.setName("logichesko");

		Tag chislen = new Tag();
		chislen.setName("Chislen");

		Tag algebra = new Tag();
		algebra.setName("Visha algebra");

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
