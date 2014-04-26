package com.ngm.explaintome.service;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
import com.ngm.explaintome.utils.Callback;

public class RestActionsImpl implements RestActions{

	@Override
	public void getTags(Callback<List<Tag>> callback) {
		RestTemplate restTemplate = new RestTemplate();
	}

	@Override
	public void getVideos(List<Tag> tags, Callback<List<Video>> callback) {
		
	}

	@Override
	public void getQuestions(Video video, Callback<List<Question>> callback) {
		
	}

}
