package com.ngm.explaintome;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
<<<<<<< HEAD
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class BrowseActivity extends BaseActivity {
	final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

=======

import  java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;
import com.ngm.explaintome.utils.Callback;

import java.util.List;

public class BrowseActivity extends BaseActivity {
    ProgressBar progressBar;
>>>>>>> 72b3e2e29338ada58874ef2215c789ddb7791bc4
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);

<<<<<<< HEAD
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
=======

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        RestActions restActions = new MockRestActions();

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
>>>>>>> 72b3e2e29338ada58874ef2215c789ddb7791bc4

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

<<<<<<< HEAD
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
=======
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


>>>>>>> 72b3e2e29338ada58874ef2215c789ddb7791bc4
