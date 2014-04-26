package com.ngm.explaintome;


import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import  java.util.ArrayList;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;

import java.util.HashMap;
import java.util.List;

public class BrowseActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse);
        final ListView listview = (ListView) findViewById(R.id.listView);

        final ArrayList<String> list = new ArrayList<String>();
        RestActions restActions = new MockRestActions();
        List<Tag> tags = restActions.getTags();

        for (int i = 0; i < tags.size(); ++i) {
            list.add(tags.get(i).toString());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse, menu);
		return true;
	}


}

private class StableArrayAdapter extends ArrayAdapter<String> {

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
