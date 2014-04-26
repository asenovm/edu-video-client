package com.ngm.explaintome.service;

import java.util.List;

import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;

public interface RestActions {
	List<Tag> getTags();
	List<Video> getVideos(List<Tag> tags);
	List<Question> getQuestions(Video video);
	
}
