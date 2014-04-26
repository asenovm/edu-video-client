package com.ngm.explaintome.service;

import java.util.ArrayList;
import java.util.List;

import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;

public class MockRestActions implements RestActions{

	@Override
	public List<Tag> getTags() {
		final ArrayList<Tag> arrayList = new ArrayList<Tag>();
		Tag logichesko = new Tag();
		logichesko.setName("logichesko");
		arrayList.add(logichesko);
		return arrayList;
	}

	@Override
	public List<Video> getVideos(List<Tag> tags) {
		final Video video = new Video();
		return new ArrayList<Video>();
	}

	@Override
	public List<Question> getQuestions(Video video) {
		return new ArrayList<Question>();
	}

}
