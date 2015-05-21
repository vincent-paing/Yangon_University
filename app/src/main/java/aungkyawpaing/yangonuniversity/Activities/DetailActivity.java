package aungkyawpaing.yangonuniversity.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import aungkyawpaing.yangonuniversity.ClassModels.Department;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Constants;
import aungkyawpaing.yangonuniversity.Utils.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vincent on 14-May-15.
 */
public class DetailActivity extends ActionBarActivity {

    @InjectView(R.id.txt_dept_info) TextView mInfoTextView;
    @InjectView(R.id.my_awesome_toolbar) Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Department department = (Department) intent.getSerializableExtra(Constants.ARG_DEPARMENT);

        mToolBar.setTitle(department.getDept_name().substring(13));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mInfoTextView.setText(Util.unescape(department.getDept_info()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
