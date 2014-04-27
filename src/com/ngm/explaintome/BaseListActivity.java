package com.ngm.explaintome;

import android.view.View;
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
}
