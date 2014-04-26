package com.ngm.explaintome;

import java.net.URI;
import java.net.URISyntaxException;

public class RestConfig {
	public static final String HOST = "192.168.1.129";
	public static final int PORT = 8080;
	private static final String SCHEME = "http";
	private static final String TAGS = "tags";

	public URI getТаgsUri() {
		try {
			//XXX FIXME use proper constructors with different parts! No time to figure out what is wrong when calling one of them..
			return new URI(SCHEME + "://" + HOST + ":" + PORT + "/" + TAGS);
		} catch (URISyntaxException e) {
			throw new RuntimeException();
		}
	}

}
