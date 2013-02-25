package com.world.nearby;

import google.place.helper.GooglePlaceHelper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.helper.ui.LocationHelper;
import com.app.helper.ui.NetworkHelper;
import com.app.helper.ui.LocationHelper.LocationResult;
import com.world.nearby.MapSearchActivity.PlacesAdapter;

public class SearchActivity extends Activity implements OnClickListener {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	private ListView lstSearch;
	private NavigationListAdapter mAdapter;
	private Spinner spnrPlaces, spnrDistance;
	private SpinnerAdapter spnrPlacesAdapter;
	private ImageView imgHome, imgMapSearch;
	public static Location currentLocation;
	private String places, radius;
	public static ArrayList<HashMap<String, Object>> result, referenceResult;
	NetworkHelper networkhelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wn_search);
		
		networkhelp = new NetworkHelper();
		
		lstSearch = (ListView)findViewById(R.id.lstSearch);
		mAdapter = new NavigationListAdapter();
		
		referenceResult = new ArrayList<HashMap<String,Object>>();
		
		lstSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if (networkhelp.isOnline(SearchActivity.this)) {

					Intent intent = new Intent(SearchActivity.this,
							DetailedResultActivity.class);
					intent.putExtra("reference",
							result.get(position).get("reference" + position)
									.toString());
					intent.putExtra("activityflag", "seach");
					SearchActivity.this.startActivity(intent);

				} else {
					Toast.makeText(SearchActivity.this,
							"No Internet Connection!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				currentLocation = location;
			}
		};
		LocationHelper myLocation = new LocationHelper();
		myLocation.getLocation(this, locationResult);

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
			lstSearch.setAdapter(null);
			mAdapter = new NavigationListAdapter();

			if (networkhelp.isOnline(SearchActivity.this)) {
				new ParsingTask(this).execute(i);
			} else {
				Toast.makeText(SearchActivity.this,
						"No Internet Connection!", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(this, "Select both the fields.",
					Toast.LENGTH_LONG).show();
		}
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
			try{
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
			}catch (Exception e) {
				return null;
			}
			return result;
		}

		protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
			searchDialog.dismiss();
			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					mAdapter.addItem(result.get(i));
				}
				lstSearch.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				mAdapter.notifyDataSetInvalidated();
			}else{
				Toast.makeText(getApplicationContext(),
						"Server error, Please try again later.",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private class NavigationListAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> mData;
		ArrayList<HashMap<String, Object>> arrayListCopy = new ArrayList<HashMap<String, Object>>();
		private LayoutInflater mInflater;

		public NavigationListAdapter() {
			mData = new ArrayList<HashMap<String, Object>>();
			// filter = new MyFilter(mData);
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		private class ViewHolder {

			TextView txtInstruction, txtDistance, txtDuration;

		}

		public void addItem(HashMap<String, Object> item) {
			mData.add(item);
			notifyDataSetChanged();
		}

		public int getCount() {
			return mData.size();
		}

		public HashMap<String, Object> getItem(int position) {
			return mData.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			arrayListCopy.addAll(mData);
			// Generate the tablular view of User List
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.wn_navigation_inflater, null);
				holder = new ViewHolder();

				holder.txtInstruction = (TextView) convertView
						.findViewById(R.id.txtInstructions);
				holder.txtDistance = (TextView) convertView
						.findViewById(R.id.txtNaviDistance);
				holder.txtDuration = (TextView) convertView
						.findViewById(R.id.txtNaviDuration);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.txtInstruction.setText(mData.get(position).get("name"+position).toString());
			holder.txtDistance.setText(mData.get(position).get("vicinity"+position).toString());
			holder.txtDuration.setVisibility(View.INVISIBLE);
			
			return convertView;
		}
	}
}
