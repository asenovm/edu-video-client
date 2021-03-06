package com.ngm.explaintome.service;

import java.util.List;

import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;

public interface RestActions {
	void getTags(Callback<List<Tag>> callback);
	void getVideos(List<Tag> tags, Callback<List<Video>> callback);
	void getQuestions(Video video, Callback<List<Question>> callback);
	
	void postTags(Callback<Boolean> callback, List<Tag> tags);
	void postVideo(Video video, Callback<Video> success);
}
