package com.ngm.explaintome;

import java.net.URI;
import java.net.URISyntaxException;

public class RestConfig {
	public static final String HOST = "10.0.201.58";
	public static final int PORT = 8080;
	private static final String SCHEME = "http";
	private static final String TAGS = "tags";
	private static final String VIDEOS = "videos";
	private static final String SERVER_URL = SCHEME + "://" + HOST + ":" + PORT + "/";

	public URI getТаgsUri() {
		try {
			// XXX FIXME use proper constructors with different parts! No time
			// to figure out what is wrong when calling one of them..
			return new URI(SERVER_URL + TAGS);
		} catch (URISyntaxException e) {
			throw new RuntimeException();
		}
	}

	public URI getVideosUri(String query) {
		try {
			return new URI(SERVER_URL + VIDEOS + query );
		} catch (URISyntaxException e) {
			throw new RuntimeException();
		}
	}

}
