package aungkyawpaing.yangonuniversity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import aungkyawpaing.yangonuniversity.activities.MainActivity;
import aungkyawpaing.yangonuniversity.adapters.SearchListAdapter;
import aungkyawpaing.yangonuniversity.models.MarkerData;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.utils.Database;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.ArrayList;

/**
 * Created by AKP on 8/16/2015.
 */
public class SearchResultFragment extends Fragment {

  @Bind(R.id.list_search) ListView searchResultList;
  private ArrayList<MarkerData> markerDatas = new ArrayList<>();
  private SearchListAdapter mSearchListAdapter;
  private Database database;
  private Context mContext;
  private String query;
  private static String ARG_QUERY = "ARG_QUERY";

  public static SearchResultFragment newInstance(String query) {
    SearchResultFragment fragment = new SearchResultFragment();
    Bundle args = new Bundle();
    args.putString(ARG_QUERY, query);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_search, container, false);
    ButterKnife.bind(this, rootView);

    mContext = getActivity().getApplicationContext();
    query = getArguments().getString(ARG_QUERY);
    database = Database.getDatbase(mContext);
    for (MarkerData data : database.getallMarkers()) {
      if (data.getTitle().toLowerCase().contains(query.toLowerCase())) {
        markerDatas.add(data);
      }
    }
    mSearchListAdapter = new SearchListAdapter(mContext, markerDatas);
    searchResultList.setAdapter(mSearchListAdapter);
    searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ((MainActivity) getActivity()).onSearchResultClick(markerDatas.get(position).getTitle());
      }
    });
    return rootView;
  }
}
