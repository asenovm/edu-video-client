package com.ngm.explaintome;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;

public class BaseListActivity extends BaseActivity {

	protected View progressBar;

	protected SearchView searchView;

	protected ListView listView;

	protected void onRestOperationStart() {
		progressBar.setVisibility(View.VISIBLE);
	}

	protected void onRestOperationEnd() {
		progressBar.setVisibility(View.GONE);
		listView.setEmptyView(findViewById(R.id.empty_view));
	}

	protected abstract class BaseListAdapter<T extends FilterableEntity>
			extends BaseAdapter implements Filterable {
		protected final List<T> originalEntities;

		protected List<T> entities;

		protected BaseListAdapter(List<T> entities) {
			this.entities = entities;
			this.originalEntities = entities;
		}

		private class BaseListFilter extends Filter {

			private List<T> getFilteredTags(final CharSequence query) {
				final List<T> result = new LinkedList<T>();
				for (final T entity : originalEntities) {
					final String entityText = entity.getFilterText()
							.toLowerCase(Locale.getDefault());
					final String queryString = query.toString().toLowerCase(
							Locale.getDefault());
					if (entityText.contains(queryString)) {
						result.add(entity);
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
				final List<T> entities = (List<T>) results.values;
				BaseListAdapter.this.entities = entities;
				notifyDataSetChanged();
			}

		}

		@Override
		public int getCount() {
			return entities.size();
		}

		@Override
		public Object getItem(int i) {
			return entities.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public Filter getFilter() {
			return new BaseListFilter();
		}
	}

}
