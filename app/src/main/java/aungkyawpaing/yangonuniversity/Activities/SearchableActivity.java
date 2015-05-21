package aungkyawpaing.yangonuniversity.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import aungkyawpaing.yangonuniversity.Adapters.SearchListAdapter;
import aungkyawpaing.yangonuniversity.ClassModels.MarkerData;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Database;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vincent on 19-May-15.
 */
public class SearchableActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.my_awesome_toolbar) Toolbar toolbar;
    @InjectView(R.id.search_list) ListView mSearchListVIew;
    private ArrayList<MarkerData> mDataList;
    private SearchListAdapter mSearchAdapter;
    private ArrayList<MarkerData> mTempData;
    private Database database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = new Database(getApplicationContext());
        mDataList = new ArrayList<MarkerData>();
        mTempData = new ArrayList<MarkerData>();
        mDataList.addAll(database.getallMarkers());
        mSearchAdapter = new SearchListAdapter(getApplicationContext(), mTempData);
        mSearchListVIew.setAdapter(mSearchAdapter);
//        mSearchListVIew.setOnItemClickListener(this);

        HandleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        HandleIntent(intent);
    }

    private void HandleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            toolbar.setTitle("Search Results : " + query);
            for (MarkerData data : mDataList) {
                if (data.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    mTempData.add(data);
                    mSearchAdapter.notifyDataSetChanged();
                    if (mTempData.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No Results could be found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Intent searchdone_intent;
//        searchdone_intent = new Intent(view.getContext(), MainActivity.class);
//        searchdone_intent.putExtra("search", adapter.getItem(position).getTitle());
//        startActivity(searchdone_intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
