package google.place.helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.helper.ui.JSONParser;

import android.util.Log;

public class GoogleDetailedPlaceHelper {

	HashMap<String, Object> singleResult;
	HashMap<String, Double> location;
	ArrayList<String> types;
	JSONObject resultJson;
	JSONParser jParser;
	HashMap<String, String> ratings;
	ArrayList<HashMap<String, String>> ratings_lst;
	HashMap<String, Object> reviews;
	ArrayList<HashMap<String, Object>> review_lst;

	// JSON Node names
	private static String TAG_LAT;
	private static String TAG_LONG;
	private static String TAG_ICON;
	private static String TAG_ID;
	private static String TAG_NAME;
	private static String TAG_REFERENCE;
	private static String TAG_TYPE;
	private static String TAG_VICINITY;
	private static String TAG_FORMATTED_ADD;
	private static String TAG_PHONE_NUMBER;
	private static String TAG_URL;
	private static String TAG_WEBSITE;

	public GoogleDetailedPlaceHelper() {
		singleResult = new HashMap<String, Object>();
		location = new HashMap<String, Double>();
		types = new ArrayList<String>();
		resultJson = null;
		jParser = new JSONParser();
		
		review_lst = new ArrayList<HashMap<String, Object>>();

		// JSON Node names
		TAG_LAT = "lat";
		TAG_LONG = "lng";
		TAG_ICON = "icon";
		TAG_ID = "id";
		TAG_NAME = "name";
		TAG_REFERENCE = "reference";
		TAG_TYPE = "types";
		TAG_VICINITY = "vicinity";
		TAG_FORMATTED_ADD = "formatted_address";
		TAG_PHONE_NUMBER = "international_phone_number";
		TAG_URL = "url";
		TAG_WEBSITE = "website";
	}

	public HashMap<String, Object> getPlaces(String url, int activityFlag) {

		// Creating JSON Parser instance

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);

		try {
			// Getting Array of Contacts

			JSONObject c = json.getJSONObject("result");

			// Storing each json item in variable

			JSONObject geometry = c.getJSONObject("geometry");
			JSONObject locationJson = geometry.getJSONObject("location");

			String lat = locationJson.getString(TAG_LAT);
			Log.d("", "lat: " + lat);
			location.put("lat", Double.valueOf(lat));
			String lng = locationJson.getString(TAG_LONG);
			Log.d("", "lng: " + lng);
			location.put("lng", Double.valueOf(lng));

			String id = c.getString(TAG_ID);
			Log.d("", "id: " + id);
			String name = c.getString(TAG_NAME);
			Log.d("", "name: " + name);

			String icon = c.getString(TAG_ICON);
			Log.d("", "icon: " + icon);
			String reference = c.getString(TAG_REFERENCE);
			Log.d("", "reference: " + reference);

			// Phone number is agin JSON Object
			JSONArray typesJson = c.getJSONArray(TAG_TYPE);
			for (int j = 0; j < typesJson.length(); j++) {
				Log.d("", "types : " + typesJson.getString(j));
				types.add("" + typesJson.getString(j));
			}

			String vicinity = c.getString(TAG_VICINITY);
			Log.d("", "vicinity: " + vicinity);

			singleResult.put("location", location);
			singleResult.put("id", id);
			singleResult.put("name", name);
			singleResult.put("icon", icon);
			singleResult.put("reference", reference);
			singleResult.put("type", types);

			if (activityFlag == 2) {

				try {
					String formatted_address = c.getString(TAG_FORMATTED_ADD);
					singleResult.put("formatted_address", formatted_address);

					String international_phone_number = c
							.getString(TAG_PHONE_NUMBER);
					singleResult.put("international_phone_number",
							international_phone_number);

					String url_share = c.getString(TAG_URL);
					singleResult.put("url", url_share);

					String website = c.getString(TAG_WEBSITE);
					singleResult.put("website", website);

					int rating = c.getInt("rating");
					singleResult.put("rating", rating);

					Log.d("", "formatted_address: " + formatted_address);
					Log.d("", "international_phone_number: "
							+ international_phone_number);
					Log.d("", "url: " + url);
					Log.d("", "website: " + website);
					Log.d("", "rating: " + rating);

				} catch (Exception e) {
					// TODO: handle exception
				}

				try {
					JSONObject opening_hours = c.getJSONObject("opening_hours");
					String open_now = "" + opening_hours.getBoolean("open_now");
					singleResult.put("open_now", open_now);
					Log.d("", "open_now: " + open_now);

					JSONArray opening_timings = opening_hours
							.getJSONArray("periods");
					JSONObject opening_time_obj_main = opening_timings
							.getJSONObject(0);
					JSONObject closing_time_obj = opening_time_obj_main
							.getJSONObject("close");
					JSONObject opening_time_obj = opening_time_obj_main
							.getJSONObject("open");
					String opening_time = opening_time_obj.getString("time");
					String closing_time = closing_time_obj.getString("time");
					singleResult.put("opening_time", opening_time);
					singleResult.put("closing_time", closing_time);

					Log.d("", "opening_time: " + opening_time);
					Log.d("", "closing_time: " + closing_time);
				} catch (Exception e) {
					// TODO: handle exception
				}

				try {
					JSONArray reviews_jarray = c.getJSONArray("reviews");
					for (int reviews_length = 0; reviews_length < reviews_jarray
							.length(); reviews_length++) {
						reviews = new HashMap<String, Object>();
						JSONObject single_review = reviews_jarray
								.getJSONObject(reviews_length);
						JSONArray aspects = single_review
								.getJSONArray("aspects");
						ratings_lst = new ArrayList<HashMap<String, String>>();
						for (int aspects_length = 0; aspects_length < aspects
								.length(); aspects_length++) {
							
							JSONObject single_aspects_obj = aspects
									.getJSONObject(aspects_length);
							ratings = new HashMap<String, String>();
							ratings.put("rating"+aspects_length,
									"" + single_aspects_obj.getInt("rating"));
							ratings.put("type"+aspects_length,
									single_aspects_obj.getString("type"));
							ratings_lst.add(ratings);

							Log.d("",
									"rating: "
											+ single_aspects_obj
													.getInt("rating"));
							Log.d("",
									"type: "
											+ single_aspects_obj
													.getString("type"));
						}
						reviews.put("reviews_rating", ratings_lst);
						String author_name = single_review
								.getString("author_name");
						reviews.put("reviews_author_name", author_name);
						String author_url = single_review
								.getString("author_url");
						reviews.put("reviews_author_url", author_url);
						String text = single_review.getString("text");
						reviews.put("reviews_text", text);
						Double time = single_review.getDouble("time");
						reviews.put("reviews_time", time);

						review_lst.add(reviews);

						Log.d("", "author_name: " + author_name);
						Log.d("", "author_url: " + author_url);
						Log.d("", "text: " + text);
						Log.d("", "rating: " + time);
					}
					singleResult.put("reviewList", review_lst);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return singleResult;
	}

}
