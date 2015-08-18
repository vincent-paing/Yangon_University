package aungkyawpaing.yangonuniversity.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aungkyawpaing.yangonuniversity.Activities.DetailActivity;
import aungkyawpaing.yangonuniversity.Activities.MainActivity;
import aungkyawpaing.yangonuniversity.Adapters.DepartmentAdapter;
import aungkyawpaing.yangonuniversity.ClassModels.Department;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Constants;
import aungkyawpaing.yangonuniversity.Utils.Database;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.ArrayList;

/**
 * Created by Vincent on 13-May-15.
 */
public class DepartmentFragment extends Fragment {

  @InjectView(R.id.department_list) RecyclerView mDepartmentRecyclerView;
  private ArrayList<Department> mDepartmentList;
  private Database mDatabase;
  private Context mContext;

  public static DepartmentFragment newInstance() {
    return new DepartmentFragment();
  }

  @Nullable @Override public View onCreateView(final LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_departments, container, false);
    ButterKnife.inject(this, rootView);

    mContext = getActivity().getApplicationContext();

    mDatabase = Database.getDatbase(mContext);
    mDepartmentList = new ArrayList<Department>();
    mDepartmentList.addAll(mDatabase.getAllDepartment());
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mDepartmentRecyclerView.setLayoutManager(layoutManager);
    DepartmentAdapter adapter = new DepartmentAdapter(mDepartmentList, getActivity());
    adapter.setOnItemClickListener(new DepartmentAdapter.OnItemClickListener() {
      @Override public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(mContext, DetailActivity.class);
        intent.putExtra(Constants.ARG_DEPARMENT, mDepartmentList.get(position));
        startActivity(intent);
      }
    });
    mDepartmentRecyclerView.setAdapter(adapter);
    return rootView;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(1);
  }
}
