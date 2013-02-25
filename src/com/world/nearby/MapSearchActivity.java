package com.world.nearby;

import google.place.helper.GooglePlaceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.helper.ui.LocationHelper;
import com.app.helper.ui.NetworkHelper;
import com.app.helper.ui.LocationHelper.LocationResult;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import custome.overlay.helper.CustomItemizedOverlay;
import custome.overlay.helper.CustomOverlayItem;

public class MapSearchActivity extends MapActivity implements OnClickListener {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	private Spinner spnrPlaces, spnrDistance;
	private SpinnerAdapter spnrPlacesAdapter;
	private ImageView imgHome, imgMapSearch;

	public static ArrayList<HashMap<String, Object>> result;
	private HashMap<String, Object> singleResult;
	private HashMap<String, Double> location;
	private ArrayList<String> types;
	private List<Overlay> mapOverlays;
	private MapView mapView;
	private CustomOverlayItem overlayItem;
	private CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay;
	private String places = null;
	private String radius = null;
	private Drawable drawable;
	public static Location currentLocation = null;
	NetworkHelper networkhelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wn_map_search);
		
		networkhelp = new NetworkHelper();

		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();

		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				if (location == null) {
					try {
						final MyLocationOverlay mMyLocationOverlay = new MyLocationOverlay(
								MapSearchActivity.this, mapView);
						mMyLocationOverlay.enableMyLocation();
						mMyLocationOverlay.runOnFirstFix(new Runnable() {
							public void run() {
								mapView.getController().animateTo(
										mMyLocationOverlay.getMyLocation());
								Log.d("***************", ""
										+ mMyLocationOverlay.getMyLocation()
												.getLatitudeE6());
								mMyLocationOverlay.getMyLocation();
								currentLocation.setLatitude((mMyLocationOverlay
										.getMyLocation().getLatitudeE6()) / 1E6);
								currentLocation
										.setLongitude((mMyLocationOverlay
												.getMyLocation()
												.getLongitudeE6()) / 1E6);
							}
						});
					} catch (Exception e) {
						
					}
				} else {
					currentLocation = location;
				}
			}
		};
		LocationHelper myLocation = new LocationHelper();
		myLocation.getLocation(this, locationResult);

		drawable = getResources().getDrawable(R.drawable.map_pin);
		// itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
		// drawable, mapView);

		result = new ArrayList<HashMap<String, Object>>();

		spnrPlaces = (Spinner) findViewById(R.id.spnrPlaces);
		spnrDistance = (Spinner) findViewById(R.id.spnrDistance);

		String[] spin_arry = getResources().getStringArray(R.array.places);
		spnrPlacesAdapter = new PlacesAdapter<CharSequence>(this, spin_arry);
		spnrPlaces.setAdapter(spnrPlacesAdapter);

		String[] spin_arry_radius = getResources().getStringArray(
				R.array.radius);
		spnrPlacesAdapter = new PlacesAdapter<CharSequence>(this,
				spin_arry_radius);
		spnrDistance.setAdapter(spnrPlacesAdapter);

		spnrPlaces
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						Object item = parent.getItemAtPosition(pos);
						places = item.toString();
						if (radius != null
								&& !radius.equalsIgnoreCase(parent
										.getItemAtPosition(0).toString())) {
							serachPlace();
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		spnrDistance
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						Object item = parent.getItemAtPosition(pos);
						radius = item.toString();
						if (places != null
								&& !places.equalsIgnoreCase(parent
										.getItemAtPosition(0).toString())) {
							serachPlace();
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgHome.setOnClickListener(this);
		imgMapSearch = (ImageView) findViewById(R.id.imgMapSearch);
		imgMapSearch.setOnClickListener(this);
	}

	static class PlacesAdapter<T> extends ArrayAdapter<T> {
		Context context;

		public PlacesAdapter(Context ctx, T[] objects) {
			super(ctx, android.R.layout.simple_spinner_item, objects);
			this.context = ctx;
		}

		// other constructors

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);

			// we know that simple_spinner_item has android.R.id.text1 TextView:

			/* if(isDroidX) { */
			TextView text = (TextView) view.findViewById(android.R.id.text1);
			text.setTextColor(Color.parseColor("#3371ab"));// choose your color
			
			text.setPadding(2, 2, 2, 2);
			/* } */

			return view;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.imgHome:
			finish();
			break;
		case R.id.imgMapSearch:
			serachPlace();
			break;

		default:
			break;
		}
	}

	private void serachPlace() {
		if (!places.equalsIgnoreCase("places")
				&& !radius.equalsIgnoreCase("radius in meter")) {
			Void[] i = null;
			mapOverlays.clear();
			mapView.removeAllViews();
			mapView.invalidate();
			
			if (networkhelp.isOnline(MapSearchActivity.this)) {
				new ParsingTask(this).execute(i);
			}else{
				Toast.makeText(MapSearchActivity.this,
						"No Internet Connection!", Toast.LENGTH_SHORT).show();
			}
			
		} else {
			Toast.makeText(this, "Select both the fields.",
					Toast.LENGTH_LONG).show();
		}
	}

	private void extractPlaces(ArrayList<HashMap<String, Object>> result) {
		itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(
				drawable, mapView, result);
		for (int i = 0; i < result.size(); i++) {
			singleResult = result.get(i);
			Log.d("", "name : " + singleResult.get("name" + i));
			location = (HashMap<String, Double>) singleResult.get("location"
					+ i);
			types = (ArrayList<String>) singleResult.get("types" + i);
			String type = "";
			try {
				for (int j = 0; j < types.size(); j++) {
					type = "" + types.get(j) + ", ";
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			populateMap(location.get("lat" + i), location.get("lng" + i), ""
					+ singleResult.get("name" + i),
					"" + singleResult.get("icon" + i), type);
		}
	}

	private void populateMap(double lat, double lng, String name, String icon,
			String type) {

		Log.d("", "name : " + name);
		GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		overlayItem = new CustomOverlayItem(point, name, type, icon);
		itemizedOverlay.addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);
		MapController mapController = mapView.getController();
		mapController.animateTo(point);
		mapController.setZoom(12);
		mapView.invalidate();
	}

	public class ParsingTask extends
			AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

		private Context ctx;
		private Dialog searchDialog;

		public ParsingTask(Context context) {
			super();
			this.ctx = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			searchDialog = new Dialog(ctx);
			searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			searchDialog.setContentView(R.layout.wn_map_search_dialog);
			searchDialog.setCancelable(false);
			searchDialog.show();
		}

		protected ArrayList<HashMap<String, Object>> doInBackground(
				Void... args) {
			try {
				GooglePlaceHelper googleHelper = new GooglePlaceHelper();
				Log.d("fileds ********", radius + " : " + places);
				result = googleHelper
						.getPlaces(
								"https://maps.googleapis.com/maps/api/place/search/json?location="
										+ currentLocation.getLatitude()
										+ ","
										+ currentLocation.getLongitude()
										+ "&radius="
										+ radius
										+ "&types="
										+ places
										+ "&sensor=false&key=AIzaSyB-woBUCzRaqmobjam1LjWOFNixRAO4VhY",
								1);
				// 19.036113,72.844076
			} catch (Exception e) {
				return null;
			}
			return result;
		}

		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
			searchDialog.dismiss();
			if (result != null) {
				extractPlaces(result);
				mapView.invalidate();
			}else{
				Toast.makeText(getApplicationContext(),
						"Server error, Please try again later.",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
