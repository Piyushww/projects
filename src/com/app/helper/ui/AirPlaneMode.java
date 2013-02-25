package com.app.helper.ui;

import android.content.Context;
import android.provider.Settings;

public class AirPlaneMode {

	public boolean isAirplaneModeOn(Context context) {

		   return Settings.System.getInt(context.getContentResolver(),
		           Settings.System.AIRPLANE_MODE_ON, 0) != 0;

		}
	
}
