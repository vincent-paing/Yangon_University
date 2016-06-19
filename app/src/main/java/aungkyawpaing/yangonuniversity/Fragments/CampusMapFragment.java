package aungkyawpaing.yangonuniversity.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aungkyawpaing.yangonuniversity.activities.MainActivity;
import aungkyawpaing.yangonuniversity.models.MarkerData;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.utils.Database;
import aungkyawpaing.yangonuniversity.utils.Pref;
import butterknife.ButterKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

/**
 * Created by Vincent on 13-May-15.
 */
public class CampusMapFragment extends Fragment
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MainActivity.OnDataPass {

  private GoogleMap mMap;
  private double MAX_LNG = 96.142251;
  private double MIN_LNG = 96.128848;
  private double MAX_LAT = 16.835285;
  private double MIN_LAT = 16.825524;
  private LatLng NEBOUND = new LatLng(MAX_LAT, MAX_LNG);
  private LatLng SWBOUND = new LatLng(MIN_LAT, MIN_LNG);
  private LatLngBounds MapBoundary = new LatLngBounds(SWBOUND, NEBOUND);
  private LatLng CENTRE = new LatLng(16.828693, 96.135320);
  private Handler MapHandler;
  private int MAX_ZOOM = 20;
  private int MIN_ZOOM = 16;
  private ArrayList<Marker> markers;
  private Context mContext;
  private static String ARG_MAP = "ARG_MAP";
  private GoogleApiClient mGoogleApiClient;
  private LocationRequest mLocationRequest;
  private MapLocationListener mLocationListener;
  private Pref mPref;
  private int LOCATION_PERMISSION;
  final private int REQUEST_CODE_LOCATION_PERMISSIONS = 123;

  public static CampusMapFragment newInstance() {
    CampusMapFragment fragment = new CampusMapFragment();
    Bundle args = new Bundle();
    args.putString(ARG_MAP, null);
    fragment.setArguments(args);
    return fragment;
  }

  public static CampusMapFragment newInstance(String query) {
    CampusMapFragment fragment = new CampusMapFragment();
    Bundle args = new Bundle();
    args.putString(ARG_MAP, query);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_map, container, false);
    ButterKnife.bind(this, rootView);

    mContext = rootView.getContext();
    mPref = Pref.getInstance(mContext);

    mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

    buildGoogleApiClient();
    initalizeMap();
    setHasOptionsMenu(true);
    return rootView;
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_LOCATION_PERMISSIONS:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          requestLocation();
        } else {
          showErrorDialog(R.string.permission_denied_message);
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override public void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  @Override public void onStop() {
    if (mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
    super.onStop();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    setUpHandler();
    MapHandler.sendEmptyMessageDelayed(0, 50);
    super.onActivityCreated(savedInstanceState);
  }

  private void setUpHandler() {
    MapHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
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
      zoom = (zoom == 0) ? position.zoom : zoom;
      if (zoom == 0) zoom = position.zoom;
      double lat = position.target.latitude + correction.latitude;
      double lon = position.target.longitude + correction.longitude;
      CameraPosition newPosition =
          new CameraPosition(new LatLng(lat, lon), zoom, position.tilt, position.bearing);
      CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
      mMap.moveCamera(update);
    }
  }

  private LatLng correctLatLng(LatLngBounds cameraBounds) {
    double latitude = 0;
    double longitude = 0;
    if (cameraBounds.southwest.latitude < MapBoundary.southwest.latitude) {
      latitude = MapBoundary.southwest.latitude - cameraBounds.southwest.latitude;
    }
    if (cameraBounds.southwest.longitude < MapBoundary.southwest.longitude) {
      longitude = MapBoundary.southwest.longitude - cameraBounds.southwest.longitude;
    }
    if (cameraBounds.northeast.latitude > MapBoundary.northeast.latitude) {
      latitude = MapBoundary.northeast.latitude - cameraBounds.northeast.latitude;
    }
    if (cameraBounds.northeast.longitude > MapBoundary.northeast.longitude) {
      longitude = MapBoundary.northeast.longitude - cameraBounds.northeast.longitude;
    }
    return new LatLng(latitude, longitude);
  }

  private void initalizeMap() {
    CameraPosition cameraPosition = new CameraPosition.Builder().target(CENTRE).zoom(17).build();
    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    Database database = Database.getDatbase(mContext);
    ArrayList<MarkerData> markerdata_list = new ArrayList<MarkerData>();
    markerdata_list.addAll(database.getallMarkers());
    markers = new ArrayList<>(markerdata_list.size());
    for (MarkerData data : markerdata_list) {
      Marker marker = mMap.addMarker(
          new MarkerOptions().position(new LatLng(data.getLatitude(), data.getLongitude()))
              .icon(BitmapDescriptorFactory.fromResource(data.getIconres(mContext)))
              .title(data.getTitle()));
      markers.add(marker);
    }
  }

  @Override public void onConnected(Bundle bundle) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      Log.e("wtf", "nigga");
      LOCATION_PERMISSION = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
      if (LOCATION_PERMISSION != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
            REQUEST_CODE_LOCATION_PERMISSIONS);
      } else {
        requestLocation();
      }
    } else {
      requestLocation();
    }
  }

  private void requestLocation() {
    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    if (mLastLocation != null) {
      if (isLocationWithinBound(mLastLocation)) {
        mMap.setMyLocationEnabled(true);
      }
    } else {
      //@TODO
      LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
      boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
      boolean isNETWORKEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

      boolean canLocationRequest = isGPSEnabled || isNETWORKEnabled;

      if (!canLocationRequest) {
        showErrorDialog(R.string.gps_off_message);
      } else {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationListener = new MapLocationListener();

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,
            mLocationListener);


      }
    }
  }

  @Override public void onConnectionSuspended(int i) {

  }

  @Override public void onConnectionFailed(ConnectionResult connectionResult) {
    showErrorDialog(R.string.gps_message);
  }

  @Override public void onDataPass(String data) {
    for (Marker marker : markers) {
      if (marker.getTitle().equals(data)) {
        CameraPosition newPosition =
            new CameraPosition(marker.getPosition(), 18, mMap.getCameraPosition().tilt,
                mMap.getCameraPosition().bearing);
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
        mMap.moveCamera(update);
        marker.showInfoWindow();
      }
    }
  }

  private class MapLocationListener implements LocationListener {
    @Override public void onLocationChanged(Location location) {
      //REMOVE LOCATION REQUEST
      //@TODO:
      if (location != null) {
        if (isLocationWithinBound(location)) {
          mMap.setMyLocationEnabled(true);
        } else {
          mMap.setMyLocationEnabled(false);
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
            mLocationListener);
      }
    }
  }

  private boolean isLocationWithinBound(Location location) {
    if (location.getLongitude() < MAX_LNG
        && location.getLongitude() > MIN_LNG
        && location.getLatitude() < MAX_LAT
        && location.getLatitude() > MIN_LAT) {
      return true;
    } else {
      return false;
    }
  }

  private void showErrorDialog(int dialogID) {
    boolean status = mPref.getDialogStatus();
    if (status) {
      AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
      builder.setTitle(R.string.gps_title)
          .setMessage(dialogID)
          .setPositiveButton(R.string.dialog_okay, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.dismiss();
            }
          })
          .setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
              mPref.setDialogStatus(false);
            }
          })
          .show();
    }
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).setupDataPasser(this);
  }
}
