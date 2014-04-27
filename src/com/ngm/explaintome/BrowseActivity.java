package com.ngm.explaintome;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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

	/**
	 * {@value}
	 */
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

	private final class TagAdapter extends BaseAdapter implements Filterable {
		public List<Tag> tags;

		private final List<Tag> originalTags;

		private class TagFilter extends Filter {

			private List<Tag> getFilteredTags(final CharSequence query) {
				final List<Tag> result = new LinkedList<Tag>();
				for (final Tag tag : originalTags) {
					final String tagName = tag.getName().toLowerCase(
							Locale.getDefault());
					final String queryString = query.toString().toLowerCase(
							Locale.getDefault());
					if (tagName.contains(queryString)) {
						result.add(tag);
					}
				}

				return result;
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults result = new FilterResults();
				result.values = getFilteredTags(constraint);
				return result;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				final List<Tag> tags = (List<Tag>) results.values;
				TagAdapter.this.tags = tags;
				notifyDataSetChanged();
			}

		}

		public TagAdapter(List<Tag> tags) {
			this.tags = tags;
			this.originalTags = tags;
		}

		@Override
		public int getCount() {
			return tags.size();
		}

		@Override
		public Object getItem(int i) {
			return tags.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			TextView textView = (TextView) LayoutInflater.from(
					BrowseActivity.this).inflate(
					android.R.layout.simple_list_item_1, null, false);
			textView.setText(tags.get(i).getName());
			return textView;
		}

		@Override
		public Filter getFilter() {
			return new TagFilter();
		}
	}

}
