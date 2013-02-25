package com.app.helper.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.Toast;

import com.world.nearby.R;

public class MyBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Your Car parking time is up!!!!",
				Toast.LENGTH_LONG).show();
//		 Vibrator the mobile phone
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
		
		MediaPlayer mPlayer = MediaPlayer.create(context,R.raw.alarmclock);
		mPlayer.start();
	}

}