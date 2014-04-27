package com.ngm.explaintome.data;

import java.util.ArrayList;
import java.util.List;

import com.ngm.explaintome.FilterableEntity;

import android.net.Uri;

public class Video extends ModelElement implements FilterableEntity {
	private List<Tag> tags = new ArrayList<Tag>();
	private Uri videoUri;
	private String title;
	private String description;
	private List<Question> questions = new ArrayList<Question>();
	private float rating;

	public Uri getVideoUri() {
		return videoUri;
	}

	public void setVideoUri(Uri videoUri) {
		this.videoUri = videoUri;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "Video [tags=" + tags + ", videoUri=" + videoUri + ", title="
				+ title + ", description=" + description + ", questions="
				+ questions + ", rating=" + rating + "]";
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public void setUri(String path) {
		this.videoUri = Uri.parse(path);
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Uri getPath() {
		return videoUri;
	}

	public List<Tag> getTags() {
		return tags;
	}

	@Override
	public String getFilterText() {
		return getTitle();
	}
}
