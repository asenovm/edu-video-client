package com.ngm.explaintome.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ngm.explaintome.RestConfig;
import com.ngm.explaintome.data.Question;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class RestActionsImpl implements RestActions {
	private static final String TAG = RestActions.class.getSimpleName();
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

				RestTemplate restTemplate = newRestTemplate();
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

	private RestTemplate newRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ResponseErrorHandler() {

			@Override
			public void handleError(ClientHttpResponse arg0) throws IOException {
				Log.e(TAG, "Error connecting!!!"  + arg0.toString());
			}

			@Override
			public boolean hasError(ClientHttpResponse arg0) throws IOException {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		return restTemplate;
	}
	
	@Override
	public void getVideos(final List<Tag> tags, Callback<List<Video>> callback) {
		final RestTemplate template = newRestTemplate();

		final Gson gson = new Gson();

		try {
			final String encodedJsonObject = URLEncoder.encode(gson.toJson(tags), "UTF-8");
			final String query = "?tags=" + encodedJsonObject;
			final Runnable asyncRunnable = new Runnable() {

				@Override
				public void run() {
					template.getMessageConverters().add(new StringHttpMessageConverter());
					final String response = template.getForObject(config.getVideosUri(query), String.class);

				}

			};
			AsyncTask.execute(asyncRunnable);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void getQuestions(Video video, Callback<List<Question>> callback) {

	}

	@Override
	public void postTags(final Callback<Boolean> callback, final List<Tag> tags) {
		final RestTemplate template = newRestTemplate();

		final Gson gson = new Gson();
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("tags", gson.toJson(tags));

		final Runnable asyncRunnable = new Runnable() {

			@Override
			public void run() {
				final HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), headers);
				template.getMessageConverters().add(new StringHttpMessageConverter());
				ResponseEntity<String> result = template.exchange(config.getТаgsUri(), HttpMethod.POST, requestEntity, String.class);

				callback.call(result.getStatusCode() == HttpStatus.OK);
			}
		};

		AsyncTask.execute(asyncRunnable);
	}

	@Override
	public void postVideo(final Video video, Callback<Video> success) {

		final Runnable asyncRunnable = new Runnable() {

			@Override
			public void run() {
				Gson gson = new Gson();

				final HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				headers.setAccept(new ArrayList() {{ add(MediaType.APPLICATION_OCTET_STREAM); }}); 

				MultiValueMap<String, Object> postParams = new LinkedMultiValueMap<String, Object>();
				postParams.add("file", new FileSystemResource(video.getVideoUri().getPath()));
				postParams.add("tags", gson.toJson(video.getTags()));
				postParams.add("title", video.getTitle());
				postParams.add("description", video.getDescription());
				postParams.add("questions", gson.toJson(video.getQuestions()));

				// Populate the MultiValueMap being serialized and headers in an
				// HttpEntity object to use for the request
				HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(postParams, headers);

				final RestTemplate restTemplate = newRestTemplate();
				FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
				formHttpMessageConverter.setCharset(Charset.forName("UTF-8"));
				restTemplate.getMessageConverters().add(formHttpMessageConverter);


				ResponseEntity<String> response = restTemplate.exchange(config.getVideosUri(""), HttpMethod.POST, requestEntity, String.class);
				// ResponseEntity<String> response =
				// restTemplate.exchange(config.getVideosUri(""),
				// HttpMethod.POST, requestEntity, String.class);

				Log.d(TAG, response.toString());

				// gson

			}

		};

		AsyncTask.execute(asyncRunnable);

	}
}
