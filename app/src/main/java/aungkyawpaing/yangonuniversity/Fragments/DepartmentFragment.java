package aungkyawpaing.yangonuniversity.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aungkyawpaing.yangonuniversity.activities.DetailActivity;
import aungkyawpaing.yangonuniversity.activities.MainActivity;
import aungkyawpaing.yangonuniversity.adapters.DepartmentAdapter;
import aungkyawpaing.yangonuniversity.database.FireBaseDatabaseHelper;
import aungkyawpaing.yangonuniversity.firebase.FireBaseDatabaseManager;
import aungkyawpaing.yangonuniversity.models.Department;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.utils.Constants;
import aungkyawpaing.yangonuniversity.utils.Database;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 13-May-15.
 */
public class DepartmentFragment extends Fragment {

  @Bind(R.id.department_list) RecyclerView mDepartmentRecyclerView;
  private ArrayList<Department> mDepartmentList;
  private Database mDatabase;
  private Context mContext;
  private DepartmentAdapter departmentAdapter;

  public static DepartmentFragment newInstance() {
    return new DepartmentFragment();
  }

  @Nullable @Override public View onCreateView(final LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_departments, container, false);
    ButterKnife.bind(this, rootView);

    mContext = getActivity().getApplicationContext();

    //mDatabase = Database.getDatbase(mContext);
    //mDepartmentList = new ArrayList<Department>();
    //mDepartmentList.addAll(mDatabase.getAllDepartment());

    setup();

    return rootView;
  }

  private void setup() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mDepartmentRecyclerView.setLayoutManager(layoutManager);
    departmentAdapter = new DepartmentAdapter();
    mDepartmentRecyclerView.setAdapter(departmentAdapter);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loadData();
  }

  @Override public void onResume() {
    super.onResume();
  }

  private void loadData() {
    FireBaseDatabaseManager.getDepartmentReference()
        .addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<Department>> tempIndicator =
                new GenericTypeIndicator<List<Department>>() {
                };
            mDepartmentList = new ArrayList<Department>();
            mDepartmentList.addAll(dataSnapshot.getValue(tempIndicator));
            departmentAdapter.setDepartments(mDepartmentList);
            departmentAdapter.setOnItemClickListener(new DepartmentAdapter.OnItemClickListener() {
              @Override public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(mContext, DetailActivity.class);
                intent.putExtra(Constants.ARG_DEPARMENT, mDepartmentList.get(position));
                startActivity(intent);
              }
            });
          }

          @Override public void onCancelled(DatabaseError databaseError) {

          }
        });
  }
}
