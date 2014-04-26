package com.ngm.explaintome.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ngm.explaintome.RestConfig;
import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;

public class RestActionsImpl implements RestActions {
	private RestConfig config;
	private Handler uiThreadHandler;

	public RestActionsImpl(RestConfig config) {
		this.config = config;
		this.uiThreadHandler = new Handler();
	}

	@Override
	public void getTags(final Callback<List<Tag>> callback) {
		// XXX fixme leaking context from implicit annomyous class reference
		// good enough for hackathon though...
		Runnable asyncRunnable = new Runnable() {

			@Override
			public void run() {

				RestTemplate restTemplate = new RestTemplate();
				restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

				final String response = restTemplate.getForObject(config.getТаgsUri(), String.class);
				uiThreadHandler.post(new Runnable() {

					@Override
					public void run() {
						final ArrayList<Tag> tags = new ArrayList<Tag>();
						Tag tag = new Tag();
						tag.setName(response);
						tags.add(tag);

						callback.call(tags);
					}

				});
			}
		};

		AsyncTask.execute(asyncRunnable);

	}

	@Override
	public void getVideos(final List<Tag> tags, Callback<List<Video>> callback) {

	}

	@Override
	public void getQuestions(Video video, Callback<List<Question>> callback) {

	}

	@Override
	public void putTags(final Callback<List<Tag>> callback, final List<Tag> tags) {
		final RestTemplate template = new RestTemplate();

		final Gson gson = new Gson();
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("tags", gson.toJson(tags));

		final Runnable asyncRunnable = new Runnable() {

			@Override
			public void run() {
				
				
				HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString());
				template.getMessageConverters().add(new StringHttpMessageConverter());
				ResponseEntity<String> result = template.exchange(config.getТаgsUri(), HttpMethod.POST, requestEntity, String.class);

				if (result.getStatusCode() != HttpStatus.OK) {
					throw new RuntimeException();
				}
				callback.call(null);
			}
		};

		AsyncTask.execute(asyncRunnable);
	}
}
