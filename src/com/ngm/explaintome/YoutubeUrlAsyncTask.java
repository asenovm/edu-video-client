package com.ngm.explaintome;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;
import android.util.Log;

public class YoutubeUrlAsyncTask extends AsyncTask<String, Void, String> {

	public static interface VideoTaskListener {
		void onUrlRetrieved(final String url);
	}

	/**
	 * {@value}
	 */
	private static final String TAG = YoutubeUrlAsyncTask.class.getSimpleName();

	private final VideoTaskListener listener;

	public YoutubeUrlAsyncTask(final VideoTaskListener listener) {
		this.listener = listener;
	}

	@Override
	protected String doInBackground(String... params) {
		return getUrlVideoRTSP(params[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		listener.onUrlRetrieved(result);
	}

	public String getUrlVideoRTSP(String urlYoutube) {
		try {
			String gdy = "http://gdata.youtube.com/feeds/api/videos/";
			DocumentBuilder documentBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			String id = extractYoutubeId(urlYoutube);
			URL url = new URL(gdy + id);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			Document doc = documentBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");// /media:content
			String cursor = urlYoutube;
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node != null) {
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap<String, String> maps = new HashMap<String, String>();
					for (int j = 0; j < nodeMap.getLength(); j++) {
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					if (maps.containsKey("yt:format")) {
						String f = maps.get("yt:format");
						if (maps.containsKey("url")) {
							cursor = maps.get("url");
						}
						if (f.equals("1"))
							return cursor;
					}
				}
			}
			return cursor;
		} catch (Exception ex) {
			Log.e(TAG, ex.toString());
		}
		return urlYoutube;

	}

	private String extractYoutubeId(String url) {
		String id = null;
		String query;
		try {
			query = new URL(url).getQuery();
			if (query != null) {
				String[] param = query.split("&");
				for (String row : param) {
					String[] param1 = row.split("=");
					if (param1[0].equals("v")) {
						id = param1[1];
					}
				}
			} else {
				if (url.contains("embed")) {
					id = url.substring(url.lastIndexOf("/") + 1);
				}
			}
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		}
		return id;
	}

}
