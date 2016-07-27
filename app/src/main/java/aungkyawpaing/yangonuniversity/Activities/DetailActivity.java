package aungkyawpaing.yangonuniversity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import aungkyawpaing.yangonuniversity.models.Department;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.utils.Constants;
import aungkyawpaing.yangonuniversity.utils.Util;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vincent on 14-May-15.
 */
public class DetailActivity extends ActionBarActivity {

  @Bind(R.id.txt_dept_info) TextView mInfoTextView;
  @Bind(R.id.my_awesome_toolbar) Toolbar mToolBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);

    Intent intent = getIntent();
    Department department = (Department) intent.getSerializableExtra(Constants.ARG_DEPARMENT);

    mToolBar.setTitle(department.name.substring(13));
    setSupportActionBar(mToolBar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mInfoTextView.setText(Util.unescape(department.info));
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
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
