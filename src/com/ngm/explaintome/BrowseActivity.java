package com.ngm.explaintome;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class BrowseActivity extends BaseActivity {
	final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		// RestActions restActions = new MockRestActions();

		// onRestOperationStart();
		// List<Tag> tags = restActions.getTags(new Callback<List<Tag>>() {
		// @Override
		// public void call(List<Tag> result) {
		// final ListView listview = (ListView) findViewById(R.id.listView);
		//
		// final ArrayList<String> list = new ArrayList<String>();
		// for (int i = 0; i < result.size(); ++i) {
		// list.add(result.get(i).toString());
		// }
		//
		// final StableArrayAdapter adapter = new
		// StableArrayAdapter(BrowseActivity.this,
		// android.R.layout.simple_list_item_1, list);
		// listview.setAdapter(adapter);
		//
		// listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		// {
		//
		// });
		// onRestOperationEnd();
		//
		// }
		// });

	}

	private void onRestOperationStart() {
		progressBar.setVisibility(View.VISIBLE);
	}

	private void onRestOperationEnd() {
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse, menu);
		return true;
	}

	class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}