package com.ngm.explaintome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.ngm.explaintome.data.Tag;
import com.ngm.explaintome.data.Video;
import com.ngm.explaintome.service.MockRestActions;
import com.ngm.explaintome.service.RestActions;
import com.ngm.explaintome.utils.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpt2kan on 4/26/14.
 */
public class VideosActivity extends Activity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        Intent intent = getIntent();
        String name =  intent.getStringExtra("name");
        int id = intent.getIntExtra("id", -1);

        Tag tag  = new Tag();
        tag.setName(name);
        tag.setId(id);

        progressBar = (ProgressBar) findViewById(R.id.videosProgressBar);
        RestActions restActions = new MockRestActions();
        List<Tag> listTag = new ArrayList<Tag>();
        listTag.add(tag);
        onRestOperationStart();
        restActions.getVideos(listTag,new Callback<List<Video>>() {
            @Override
            public void call(List<Video> result) {
                final ListView listview = (ListView) findViewById(R.id.videosListView);

                final ArrayList<String> list = new ArrayList<String>();

                for (int i = 0; i < result.size(); ++i) {
                    list.add(result.get(i).getTitle());
                }

                final ArrayAdapter adapter = new ArrayAdapter(VideosActivity.this,
                        android.R.layout.simple_expandable_list_item_1, list);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

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
    private void onRestOperationStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void onRestOperationEnd(){
        progressBar.setVisibility(View.GONE);

    }
}