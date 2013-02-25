/***
 * Copyright (c) 2011 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package custome.overlay.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.world.nearby.DetailedResultActivity;
import com.world.nearby.MapSearchActivity;

public class CustomItemizedOverlay<Item extends OverlayItem> extends
		BalloonItemizedOverlay<CustomOverlayItem> {

	private ArrayList<CustomOverlayItem> m_overlays = new ArrayList<CustomOverlayItem>();
	private Context c;
	ArrayList<HashMap<String, Object>> result;

	public CustomItemizedOverlay(Drawable defaultMarker, MapView mapView,
			ArrayList<HashMap<String, Object>> result) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
		this.result = result;
	}

	public void addOverlay(CustomOverlayItem overlay) {
		m_overlays.add(overlay);
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (!shadow) {
			super.draw(canvas, mapView, false);
		}
	}

	@Override
	protected CustomOverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	public boolean onBalloonTap(int index, CustomOverlayItem item) {
		Intent intent = new Intent();
		intent.putExtra("index", index);
		intent.putExtra("activityflag", "mapsearch");
		intent.putExtra("reference", ""+MapSearchActivity.result.get(index).get("reference"+index));
		intent.setClass(c, DetailedResultActivity.class);
		c.startActivity(intent);
		return true;
	}

	@Override
	protected BalloonOverlayView<CustomOverlayItem> createBalloonOverlayView() {
		// use our custom balloon view with our custom overlay item type:
		return new CustomBalloonOverlayView<CustomOverlayItem>(getMapView()
				.getContext(), getBalloonBottomOffset());
	}

}
