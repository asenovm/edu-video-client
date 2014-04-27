package com.ngm.explaintome;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.service.Callback;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;
import com.ngm.explaintome.service.RestActionsImpl;

public class BrowseActivity extends BaseActivity {
    ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RestActions restActions = new RestActionsImpl(new RestConfig());

        onRestOperationStart();
        restActions.getTags(new Callback<List<Tag>>() {
            @Override
            public void call(List<Tag> result) {
                final ListView listview = (ListView) findViewById(R.id.listView);



                listview.setAdapter(new TagAdapter(result));
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Tag tag = (Tag) adapterView.getAdapter().getItem(position);

                        Intent intent = new Intent(BrowseActivity.this, VideosActivity.class);
                        intent.putExtra("name",tag.getName());
                        intent.putExtra("id", tag.getId());

                        startActivity(intent);
                    }
                });
                onRestOperationEnd();

            }
        });



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

   private final class TagAdapter extends BaseAdapter{
        public final List<Tag> tags;

        public TagAdapter(List<Tag> tags){
            this.tags = tags;
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
            TextView textView = (TextView) LayoutInflater.from(BrowseActivity.this).inflate(android.R.layout.simple_list_item_1, null);
            textView.setText(tags.get(i).getName());
            return textView;
        }
    }

}
