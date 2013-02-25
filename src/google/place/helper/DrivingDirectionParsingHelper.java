package google.place.helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.helper.ui.JSONParser;

import android.util.Log;

public class DrivingDirectionParsingHelper {

	HashMap<String, Object> result, singleSteps;
	JSONParser jsonParser;
	ArrayList<HashMap<String, Object>> stepsList;

	public DrivingDirectionParsingHelper() {
		result = new HashMap<String, Object>();
		jsonParser = new JSONParser();
		stepsList = new ArrayList<HashMap<String,Object>>();
	}

	public HashMap<String, Object> getDrivingDirectoins(String url) {

		try {

			JSONObject mainObj = jsonParser.getJSONFromUrl(url);

			JSONArray routesJsonArray = mainObj.getJSONArray("routes");
			
			for (int routesCount = 0; routesCount < routesJsonArray.length(); routesCount++) {
				
				JSONObject singleJsonObj = routesJsonArray.getJSONObject(routesCount);
				JSONArray legsJsonArray = singleJsonObj.getJSONArray("legs");
				
				for(int legsCount = 0; legsCount < legsJsonArray.length(); legsCount++){
					
					JSONObject singleLegsObj = legsJsonArray.getJSONObject(legsCount);
					
					JSONObject distanceJsonObj = singleLegsObj.getJSONObject("distance");
					String distanceValue = distanceJsonObj.getString("text");
					result.put("distance", distanceValue);
					Log.d("distance",distanceValue);
					
					JSONObject durationJsonObj = singleLegsObj.getJSONObject("duration");
					String durationValue = durationJsonObj.getString("text");
					result.put("duration", durationValue);
					Log.d("duration",durationValue);
					
					String endAddress = singleLegsObj.getString("end_address");
					result.put("endAddress", endAddress);
					Log.d("endAddress",endAddress);
					
					JSONObject endLocationJsonObj = singleLegsObj.getJSONObject("end_location");
					Double endLatitude = endLocationJsonObj.getDouble("lat");
					Double endLongitude = endLocationJsonObj.getDouble("lng");
					result.put("endLatitude", endLatitude);
					result.put("endLongitude", endLongitude);
					Log.d("end lat lng",endLatitude+","+endLongitude);
					
					String startAddress = singleLegsObj.getString("start_address");
					result.put("startAddress", startAddress);
					Log.d("startAddress",startAddress);
					
					JSONObject startLocationJsonObj = singleLegsObj.getJSONObject("start_location");
					Double startLatitude = startLocationJsonObj.getDouble("lat");
					Double startLongitude = startLocationJsonObj.getDouble("lng");
					result.put("startLatitude", startLatitude);
					result.put("startLongitude", startLongitude);
					Log.d("start lat lng",startLatitude+","+startLongitude);
					
					JSONArray stepsJsonArray = singleLegsObj.getJSONArray("steps");
					for(int stepsCount = 0; stepsCount < stepsJsonArray.length(); stepsCount++){
						
						singleSteps = new HashMap<String, Object>();
						
						JSONObject singleStepsObj = stepsJsonArray.getJSONObject(stepsCount);
						
						JSONObject singleDistanceJsonObj = singleStepsObj.getJSONObject("distance");
						String singleDistanceValue = singleDistanceJsonObj.getString("text");
						singleSteps.put("distance", singleDistanceValue);
						
						JSONObject singleDurationJsonObj = singleStepsObj.getJSONObject("duration");
						String singleDurationValue = singleDurationJsonObj.getString("text");
						singleSteps.put("duration", singleDurationValue);
						
						JSONObject singleEndLocationJsonObj = singleStepsObj.getJSONObject("end_location");
						Double singleEndLatitude = singleEndLocationJsonObj.getDouble("lat");
						Double singleEndLongitude = singleEndLocationJsonObj.getDouble("lng");
						singleSteps.put("endlat", singleEndLatitude);
						singleSteps.put("endlng", singleEndLongitude);
						Log.d("on the way end",singleEndLatitude+","+singleEndLongitude);
						
						JSONObject singleStartLocationJsonObj = singleStepsObj.getJSONObject("start_location");
						Double singleStartLatitude = singleStartLocationJsonObj.getDouble("lat");
						Double singleStratLongitude = singleStartLocationJsonObj.getDouble("lng");
						singleSteps.put("startlat", singleStartLatitude);
						singleSteps.put("startlng", singleStratLongitude);
						Log.d("on the way start",singleStartLatitude+","+singleStratLongitude);
						
						String html_instructions = singleStepsObj.getString("html_instructions");
						singleSteps.put("html_instructions", html_instructions);
						Log.d("html_instructions",html_instructions);
						
						stepsList.add(singleSteps);
					}
					result.put("steps", stepsList);
				}
			}

		} catch (Exception e) {
		}

		return result;
	}
}
