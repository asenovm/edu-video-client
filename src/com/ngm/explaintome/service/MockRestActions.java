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
		delay(callback, new ArrayList<Video>());
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
		logichesko.setName("Chislen");

		Tag algebra = new Tag();
		logichesko.setName("Visha algebra");

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
		}, 350);
	}

	@Override
	public void putTags(Callback<List<Tag>> callback, List<Tag> tags) {
		// TODO Auto-generated method stub
		
	}

}
