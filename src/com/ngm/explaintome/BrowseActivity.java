package com.ngm.explaintome;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;

public class BrowseActivity extends BaseListActivity {

	@SuppressWarnings("unused")
	private static final String TAG = BrowseActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

		listView = (ListView) findViewById(R.id.listView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		searchView = (SearchView) findViewById(R.id.searchView);

		RestActions restActions = new MockRestActions();
		// RestActions restActions = new RestActionsImpl(new RestConfig());

		onRestOperationStart();
		restActions.getTags(new Callback<List<Tag>>() {
			@Override
			public void call(List<Tag> result) {
				final TagAdapter adapter = new TagAdapter(result);

				listView.setAdapter(adapter);
				searchView.setOnQueryTextListener(new OnQueryTextListener() {

					@Override
					public boolean onQueryTextSubmit(String query) {
						adapter.getFilter().filter(query);
						return true;
					}

					@Override
					public boolean onQueryTextChange(String newText) {
						adapter.getFilter().filter(newText);
						return true;
					}
				});
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {
						Tag tag = (Tag) adapterView.getAdapter().getItem(
								position);

						Intent intent = new Intent(BrowseActivity.this,
								VideosActivity.class);
						intent.putExtra("name", tag.getName());
						intent.putExtra("id", tag.getId());

						startActivity(intent);
					}
				});
				onRestOperationEnd();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse, menu);
		return true;
	}

	private class TagAdapter extends BaseListAdapter<Tag> {

		public TagAdapter(List<Tag> entities) {
			super(entities);
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			TextView textView = (TextView) LayoutInflater.from(
					BrowseActivity.this).inflate(
					android.R.layout.simple_list_item_1, null, false);
			textView.setText(entities.get(i).getName());
			return textView;
		}
	}

}
