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
		Tag tag1 = new Tag();
		tag1.setName("logichesko");
		arrayList.add(tag1);
        Tag tag2 = new Tag();
        tag2.setName("analiz");
        arrayList.add(tag2);
        Tag tag3 = new Tag();
        tag3.setName("algebra");
        arrayList.add(tag3);
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
