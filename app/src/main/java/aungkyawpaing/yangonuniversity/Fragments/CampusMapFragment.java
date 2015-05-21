package aungkyawpaing.yangonuniversity.Fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;

import aungkyawpaing.yangonuniversity.Activities.MainActivity;
import aungkyawpaing.yangonuniversity.ClassModels.MarkerData;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Database;
import butterknife.ButterKnife;

/**
 * Created by Vincent on 13-May-15.
 */
public class CampusMapFragment extends Fragment {

    private GoogleMap mMap;
    private LatLng NEBOUND =  new LatLng(16.835285, 96.142251);
    private LatLng SWBOUND = new LatLng(16.825524, 96.128848);
    private LatLngBounds MapBoundary = new LatLngBounds(SWBOUND, NEBOUND);
    private LatLng CENTRE = new LatLng(16.828693, 96.135320);
    private Handler MapHandler;
    private int MAX_ZOOM = 20;
    private int MIN_ZOOM = 16;
    private ArrayList<Marker> markers;
    private ArrayList<MarkerData> markerdata_list;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.inject(this, rootView);

        mContext = rootView.getContext();
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        initalizeMap();
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setUpHandler();
        MapHandler.sendEmptyMessageDelayed(0, 50);
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpHandler() {
        MapHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                fixMapBounds();
                sendEmptyMessageDelayed(0, 50);
            }
        };
    }

    private void fixMapBounds() {
        CameraPosition position = mMap.getCameraPosition();
        VisibleRegion region = mMap.getProjection().getVisibleRegion();
        float zoom = 0;
        if (position.zoom < MIN_ZOOM) zoom = MIN_ZOOM;
        if (position.zoom > MAX_ZOOM) zoom = MAX_ZOOM;
        LatLng correction = correctLatLng(region.latLngBounds);
        if (zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
            zoom = (zoom==0)? position.zoom:zoom;
            if (zoom == 0) zoom = position.zoom;
            double lat = position.target.latitude + correction.latitude;
            double lon = position.target.longitude + correction.longitude;
            CameraPosition newPosition = new CameraPosition(new LatLng(lat, lon), zoom, position.tilt, position.bearing);
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
            mMap.moveCamera(update);
        }
    }

    private LatLng correctLatLng(LatLngBounds cameraBounds) 	{
        double latitude = 0;
        double longitude = 0;
        if (cameraBounds.southwest.latitude < MapBoundary.southwest.latitude) {
            latitude = MapBoundary.southwest.latitude - cameraBounds.southwest.latitude;
        }
        if(cameraBounds.southwest.longitude < MapBoundary.southwest.longitude) {
            longitude = MapBoundary.southwest.longitude - cameraBounds.southwest.longitude;
        }
        if(cameraBounds.northeast.latitude > MapBoundary.northeast.latitude) {
            latitude = MapBoundary.northeast.latitude - cameraBounds.northeast.latitude;
        }
        if(cameraBounds.northeast.longitude > MapBoundary.northeast.longitude) {
            longitude = MapBoundary.northeast.longitude - cameraBounds.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }

    private void initalizeMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(CENTRE).zoom(17).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Database database = new Database(mContext);
        markerdata_list = new ArrayList<MarkerData>();
        markers = new ArrayList<Marker>();
        markerdata_list.addAll(database.getallMarkers());
        for (MarkerData data : markerdata_list) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(data.getLatitude(), data.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(data.getIconres(mContext)))
                    .title(data.getTitle()));
        }
    }

    public static CampusMapFragment newInstance() {
        return new CampusMapFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }
}