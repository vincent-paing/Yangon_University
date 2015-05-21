package aungkyawpaing.yangonuniversity.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import aungkyawpaing.yangonuniversity.Activities.DetailActivity;
import aungkyawpaing.yangonuniversity.Activities.MainActivity;
import aungkyawpaing.yangonuniversity.Adapters.DepartmentAdapter;
import aungkyawpaing.yangonuniversity.ClassModels.Department;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Constants;
import aungkyawpaing.yangonuniversity.Utils.Database;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vincent on 13-May-15.
 */
public class DepartmentFragment extends Fragment {

    @InjectView(R.id.department_list) ListView mDepartmentListView;
    private ArrayList<Department> mDepartmentList;
    private Database mDatabase;

    public static DepartmentFragment newInstance() {
        return new DepartmentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departments, container, false);
        ButterKnife.inject(this, rootView);

        mDatabase = new Database(getActivity().getApplicationContext());
        mDepartmentList = new ArrayList<Department>();
        mDepartmentList.addAll(mDatabase.getAllDepartment());
        mDepartmentListView.setAdapter(new DepartmentAdapter(getActivity().getApplicationContext(), mDepartmentList));
        mDepartmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), DetailActivity.class);
                intent.putExtra(Constants.ARG_DEPARMENT, mDepartmentList.get(position));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }


}
