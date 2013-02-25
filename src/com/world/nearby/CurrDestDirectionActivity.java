package com.world.nearby;

import google.place.helper.DrivingDirectionParsingHelper;
import google.place.helper.RouteOverlay;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.helper.ui.NetworkHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class CurrDestDirectionActivity extends MapActivity{
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	private MapView mapView;
	private ImageView imgNavi;
	private TextView txtNavi;
	private HashMap<String, Object> result;
	NetworkHelper networkhelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wn_curr_dest_direction);
		
		networkhelp = new NetworkHelper();

		mapView = (MapView) findViewById(R.id.map);
		mapView.displayZoomControls(true);

		result = new HashMap<String, Object>();

		imgNavi = (ImageView) findViewById(R.id.imgNavi);
		imgNavi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (networkhelp.isOnline(CurrDestDirectionActivity.this)) {

					Intent intent = new Intent(CurrDestDirectionActivity.this,
							CurrDestNavigationActivity.class);
					Log.d("", "result.size " + result.size());
					intent.putExtra("result", result);
					CurrDestDirectionActivity.this.startActivity(intent);

				}else{
					Toast.makeText(CurrDestDirectionActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		txtNavi = (TextView) findViewById(R.id.txtNavi);
		txtNavi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (networkhelp.isOnline(CurrDestDirectionActivity.this)) {
					Intent intent = new Intent(CurrDestDirectionActivity.this,
							CurrDestNavigationActivity.class);
					intent.putExtra("result", result);
					CurrDestDirectionActivity.this.startActivity(intent);

				} else {
					Toast.makeText(CurrDestDirectionActivity.this,
							"No Internet Connection!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		if (networkhelp.isOnline(CurrDestDirectionActivity.this)) {
			try {
				Void i = null;
				new ParsingTask(this).execute(i);
			} catch (Exception e) {
				finish();
				Toast.makeText(CurrDestDirectionActivity.this,
						"Cannot Load Route!", Toast.LENGTH_SHORT).show();
				}
		} else {
			Toast.makeText(CurrDestDirectionActivity.this,
					"No Internet Connection!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	public class ParsingTask extends AsyncTask<Void, Void, RouteOverlay> {

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

		protected RouteOverlay doInBackground(Void... args) {

			DrivingDirectionParsingHelper googleDetailedHelper = new DrivingDirectionParsingHelper();
			// GoogleDeatiledPlaceHelper googleHelper = new GooglePlaceHelper();
			result = googleDetailedHelper
					.getDrivingDirectoins("http://maps.googleapis.com/maps/api/directions/json?origin="
							+ getIntent().getExtras().getString("currlat")
							+ ","
							+ getIntent().getExtras().getString("currlng")
							+ "&destination="
							+ getIntent().getExtras().getString("lat")
							+ ","
							+ getIntent().getExtras().getString("lng")
							+ "&sensor=false");

			RouteOverlay routeOverlay = new RouteOverlay();

			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> stepsList = (ArrayList<HashMap<String, Object>>) result
					.get("steps");
			HashMap<String, Object> singleStep;

			for (int stepsListCount = 0; stepsListCount < stepsList.size(); stepsListCount++) {
				singleStep = stepsList.get(stepsListCount);

				Double startlat = (Double) singleStep.get("startlat");
				Double startlng = (Double) singleStep.get("startlng");
				GeoPoint geoPoint = new GeoPoint((int) (startlat * 1E6),
						(int) (startlng * 1E6));
				routeOverlay.addGeoPoint(geoPoint);
				Double lat = (Double) singleStep.get("endlat");
				Double lng = (Double) singleStep.get("endlng");
				GeoPoint geoPointEnd = new GeoPoint((int) (lat * 1E6),
						(int) (lng * 1E6));
				routeOverlay.addGeoPoint(geoPointEnd);
			}

			return routeOverlay;
		}

		protected void onPostExecute(RouteOverlay result) {
			searchDialog.dismiss();
			drawRoute(result);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void drawRoute(RouteOverlay result) {

		mapView.getOverlays().add(result);
		mapView.postInvalidate();
	}
}
