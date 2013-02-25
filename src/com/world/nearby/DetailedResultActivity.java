package com.world.nearby;

import google.place.helper.GoogleDetailedPlaceHelper;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.helper.ui.NetworkHelper;
import com.app.helper.ui.SharedPreferencesHelper;

public class DetailedResultActivity extends Activity implements OnClickListener {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	HashMap<String, Object> resultMain;
	private TextView txtName, txtOpenClose, txtOpeningHours, txtType,
			txtAddress;
	private ImageView imgIcon, imgDirections, imgStar1,
			imgStar2, imgStar3, imgStar4, imgStar5, imgCall, imgGoogleplus, imgWebsite;
	String reference;
	NetworkHelper networkhelp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wn_deatiled_result);
		
		networkhelp = new NetworkHelper();
		
		reference = getIntent().getExtras().getString("reference");

		txtName = (TextView) findViewById(R.id.txtName);
		txtOpenClose = (TextView) findViewById(R.id.txtOpenClose);
		txtOpeningHours = (TextView) findViewById(R.id.txtOpeningHours);
		txtType = (TextView) findViewById(R.id.txtType);
		txtAddress = (TextView) findViewById(R.id.txtAddress);

		imgIcon = (ImageView) findViewById(R.id.imgIcon);
		imgDirections = (ImageView) findViewById(R.id.imgDirections);
		imgStar1 = (ImageView) findViewById(R.id.imgStar1);
		imgStar2 = (ImageView) findViewById(R.id.imgStar2);
		imgStar3 = (ImageView) findViewById(R.id.imgStar3);
		imgStar4 = (ImageView) findViewById(R.id.imgStar4);
		imgStar5 = (ImageView) findViewById(R.id.imgStar5);
		imgCall = (ImageView) findViewById(R.id.imgCall);
		imgGoogleplus = (ImageView) findViewById(R.id.imgGoogleplus);
		imgWebsite  = (ImageView) findViewById(R.id.imgWebsite);

		imgDirections.setOnClickListener(this);
		imgCall.setOnClickListener(this);
		imgGoogleplus.setOnClickListener(this);
		imgWebsite.setOnClickListener(this);

		resultMain = new HashMap<String, Object>();
		
		if (networkhelp.isOnline(DetailedResultActivity.this)) {
			Void i = null;

			new ParsingTask(this).execute(i);
		} else {
			Toast.makeText(DetailedResultActivity.this,
					"No Internet Connection!", Toast.LENGTH_SHORT).show();
		}
	}

	public class ParsingTask extends
			AsyncTask<Void, Void, HashMap<String, Object>> {

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

		protected HashMap<String, Object> doInBackground(Void... args) {
			try {
				GoogleDetailedPlaceHelper googleDetailedHelper = new GoogleDetailedPlaceHelper();
				// GoogleDeatiledPlaceHelper googleHelper = new
				// GooglePlaceHelper();
				resultMain = googleDetailedHelper
						.getPlaces(
								"https://maps.googleapis.com/maps/api/place/details/json?reference="
										+ reference
										+ "&sensor=true&key=AIzaSyCM9xzjglx89IbZBzWaPZ-OaXG3wumNKSQ",
								2);
			} catch (Exception e) {
				return null;
			}
			return resultMain;
		}

		protected void onPostExecute(HashMap<String, Object> result) {
			if (searchDialog.isShowing()) {
				searchDialog.dismiss();
			}
			if (result != null) {
				deployValues(result);
			}else{
				Toast.makeText(getApplicationContext(),
						"Response error from server. Please try again later.",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.imgDirections:
			try {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> loc = (HashMap<String, Object>) resultMain
						.get("location");
				Intent intent = new Intent(this,
						CurrDestDirectionActivity.class);
				intent.putExtra("lat", "" + loc.get("lat"));
				intent.putExtra("lng", "" + loc.get("lng"));
				if ((getIntent().getExtras().getString("activityflag"))
						.equals("mapsearch")) {
					intent.putExtra("currlat", ""
							+ MapSearchActivity.currentLocation.getLatitude());
					intent.putExtra("currlng", ""
							+ MapSearchActivity.currentLocation.getLongitude());
				} else {
					intent.putExtra("currlat", ""
							+ SearchActivity.currentLocation.getLatitude());
					intent.putExtra("currlng", ""
							+ SearchActivity.currentLocation.getLongitude());
				}
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"Location error. Cannot retrive directions at this moment.",
						Toast.LENGTH_SHORT).show();
			}
			break;
			/*case R.id.imgCheckin:

			break;
		case R.id.imgSave:
			SharedPreferences counter = this.getSharedPreferences("sharedPreferenceCounter",
					MODE_WORLD_WRITEABLE);
			int count = counter.getInt("COUNTER", 0);
			SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
			ArrayList<String> tempResult = sharedPreferencesHelper.getSingleKeyValues("id", "resultToSave", "empty", count);
			int toastFlag = 0;
			Log.d("","%%%%%%%%%%%% count "+count);
			if(count == 0){
				sharedPreferencesHelper.setSharedPreferences(
						"resultToSave", resultMain);
			}
			for (String temp : tempResult) {
				Log.d("world", resultMain.get("id")+" "+temp);
				if (!resultMain.get("id").equals(temp)) {
					sharedPreferencesHelper.setSharedPreferences(
							"resultToSave", resultMain);
					Log.d("world", "result sved*********");
				} else {
					toastFlag = 1;
					Log.d("world", "result already exists*********");
				}
			}
			if(toastFlag == 1){
				Toast.makeText(this, "Its alredy saved.",
						Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "Place saved. It can be retrived from SAVE section!",
						Toast.LENGTH_LONG).show();
			}
			break;*/
		case R.id.imgCall:
			// Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
			// + sponsornumber));
			// startActivity(intent);
			try {
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ resultMain.get("international_phone_number")
								.toString())));
			} catch (Exception e) {
				Toast.makeText(this, "Service error. Call could not be completed.",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.imgGoogleplus:
			try {
				Uri uriGooglePlus = Uri.parse("" + resultMain.get("url"));
				Intent intentGooglePlus = new Intent(Intent.ACTION_VIEW,
						uriGooglePlus);
				startActivity(intentGooglePlus);
			} catch (Exception e) {
				Toast.makeText(this, "Page not found.",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.imgWebsite:
			try {
				Uri uri = Uri.parse("" + resultMain.get("website"));
				Intent intentWebsite = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intentWebsite);
			} catch (Exception e) {
				Toast.makeText(this, "Page not found.",
						Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	public void deployValues(HashMap<String, Object> result) {
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if ((Drawable) message.obj != null) {
					imgIcon.setImageDrawable((Drawable) message.obj);
					imgIcon.setVisibility(View.VISIBLE);
				}
			}
		};

		Thread thread = new Thread() {
			@Override
			public void run() {
				// TODO : set imageView to a "pending" image
				Drawable drawable = getImageFromUrl(""+resultMain.get("icon"));
				Message message = handler.obtainMessage(1, drawable);
				handler.sendMessage(message);
			}
		};
		
		thread.start();
		
		try {
			txtName.setText(result.get("name").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			txtAddress.setText(result.get("formatted_address").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			if (result.get("open_now").toString().equalsIgnoreCase("true")) {
				txtOpenClose.setText("Now : Open");
			} else {
				txtOpenClose.setText("Now : Closed");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			txtOpeningHours.setText(result.get("opening_time").toString()
					+ " to " + result.get("closing_time").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			@SuppressWarnings("unchecked")
			ArrayList<String> types = (ArrayList<String>) result.get("type");
			String typesValues = "";
			for (int i = 0; i < types.size(); i++) {
				typesValues = typesValues + " " + types.get(i);
			}
			txtType.setText(typesValues);
		} catch (Exception e) {

		}
		int rating = 0;
		try {
			rating = (Integer) result.get("rating");
		} catch (Exception e) {
			// TODO: handle exception
		}
		switch (rating) {
		case 1:
			imgStar1.setVisibility(View.VISIBLE);
			break;
		case 2:
			imgStar1.setVisibility(View.VISIBLE);
			imgStar2.setVisibility(View.VISIBLE);
			break;
		case 3:
			imgStar1.setVisibility(View.VISIBLE);
			imgStar2.setVisibility(View.VISIBLE);
			imgStar3.setVisibility(View.VISIBLE);
			break;
		case 4:
			imgStar1.setVisibility(View.VISIBLE);
			imgStar2.setVisibility(View.VISIBLE);
			imgStar3.setVisibility(View.VISIBLE);
			imgStar4.setVisibility(View.VISIBLE);
			break;
		case 5:
			imgStar1.setVisibility(View.VISIBLE);
			imgStar2.setVisibility(View.VISIBLE);
			imgStar3.setVisibility(View.VISIBLE);
			imgStar4.setVisibility(View.VISIBLE);
			imgStar5.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
		
		try{
			LinearLayout lytReview = (LinearLayout)findViewById(R.id.lytReview);
			
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> review = (ArrayList<HashMap<String, Object>>) result
					.get("reviewList");
			for(int i = 0; i < review.size(); i++){
				
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View reviewLayout = mInflater.inflate(
						R.layout.wn_reviews, null);
				LinearLayout lytMainRating = (LinearLayout)reviewLayout.findViewById(R.id.lytMainRating);
				TextView txtAuthorName = (TextView)reviewLayout.findViewById(R.id.txtAuthor);
				TextView txtAuthorReview = (TextView)reviewLayout.findViewById(R.id.txtAuthorReview);
				
				HashMap<String, Object> singleReview = review.get(i);
				String reviews_author_name = "Author Name : "+singleReview.get("reviews_author_name").toString();
				txtAuthorName.setText(reviews_author_name);
				String reviews_text = singleReview.get("reviews_text").toString();
				txtAuthorReview.setText(reviews_text);
				@SuppressWarnings("unchecked")
				ArrayList<HashMap<String, String>> ratingAuthor = (ArrayList<HashMap<String, String>>) singleReview
						.get("reviews_rating");
				
				for(int j = 0; j < ratingAuthor.size(); j++){
					LinearLayout lytTypeRating = (LinearLayout)reviewLayout.findViewById(R.id.lytTypeRating);
					LinearLayout lytRating = new LinearLayout(this);
					LinearLayout.LayoutParams lytParam = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT, 1);
					lytRating.setGravity(Gravity.CENTER);
					lytRating.setLayoutParams(lytParam);
					lytRating.removeAllViews();
					lytRating.setOrientation(LinearLayout.VERTICAL);
					
					HashMap<String, String> singleRating = ratingAuthor.get(j);
					
					String ratingType = singleRating.get("type"+j);
					TextView txtType = new TextView(this);
					txtType.setTextColor(Color.parseColor("#3371ab"));
					txtType.setText(ratingType);
					lytRating.addView(txtType);
					
					String ratingValue = singleRating.get("rating"+j);
					TextView txtTypeValue = new TextView(this);
					txtTypeValue.setTextColor(Color.parseColor("#3371ab"));
					txtTypeValue.setText(ratingValue);
					lytRating.addView(txtTypeValue);
					
					lytTypeRating.addView(lytRating);
				}
				
				lytReview.addView(lytMainRating);
			}
		}catch (Exception e) {
		}
	}
	
	private Drawable getImageFromUrl(String url){
		Drawable d = null;
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			d = Drawable.createFromStream(is, "src name");
		} catch (Exception e) {
		}
		return d;
	}
}
