package com.ngm.explaintome;

import android.net.Uri;

public class RestConfig {
	public static final String HOST = "localhost";
	public static final int PORT = 8080;
	private static final String SCHEME = "http";
	private static final String TAGS = "tags";

	public Uri getТаgsUri() {
		return Uri.fromParts(SCHEME, HOST + ":" + PORT, TAGS);
	}
}
