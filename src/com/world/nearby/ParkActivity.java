package com.world.nearby;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.helper.ui.AirPlaneMode;
import com.app.helper.ui.LocationHelper;
import com.app.helper.ui.LocationHelper.LocationResult;
import com.app.helper.ui.MyBroadcastReceiver;
import com.app.helper.ui.NetworkHelper;
import com.google.android.maps.MapActivity;

public class ParkActivity extends MapActivity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	private CountDownTimer cdt;
	private final String MILLIS = "millis";
	private final String SRCLAT = "srclat";
	private final String SRCLONG = "srclong";
	private long seconds;
	private int sec;
	private long minutes;
	private long substract;
	private long sub;
	private int isTimer = 0;
	private String prefName;
	private SharedPreferences myPrefs;
	private SharedPreferences.Editor prefsEditor;
	private SharedPreferences parkinfo;
	TextView txtRemainingTime, txtPreveous;
	EditText edtParkSpace, edtParkMin, edtStartTime;
	Button btnPark, btnDirection;

	static final int TIME_DIALOG_ID = 0;
	private int mHour;
	private int mMinute, mSec;
	private int mDay;
	private int mMonth;
	private int mYear;

	private final String SPACE = "space";
	private final String STARTTIME = "starttime";
	private final String MIN = "min";

	Location loc = null;

	NetworkHelper networkhelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wn_park);

		networkhelp = new NetworkHelper();

		txtRemainingTime = (TextView) findViewById(R.id.txtRemainingTIme);
		txtPreveous = (TextView) findViewById(R.id.txtPreveous);

		edtParkMin = (EditText) findViewById(R.id.edtParkMin);
		edtParkSpace = (EditText) findViewById(R.id.edtParkSpace);
		edtStartTime = (EditText) findViewById(R.id.edtStartTime);

		parkinfo = this.getSharedPreferences("parkinfo", MODE_WORLD_READABLE);
		String prefSpace = parkinfo.getString(SPACE, "00");
		String prefStartTime = parkinfo.getString(STARTTIME, "00:00");

		edtParkSpace.setText(prefSpace);
		txtPreveous.setText("Preveous : " + prefStartTime);

		edtStartTime.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

		if (getPreference()) {
			setTimer();
		}

		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSec = c.get(Calendar.SECOND);
		mDay = c.get(Calendar.DATE);
		mMonth = c.get(Calendar.MONTH);
		mYear = c.get(Calendar.YEAR);

		updateDisplay();

		btnPark = (Button) findViewById(R.id.btnPark);
		btnPark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (!edtParkMin.getText().toString().equals("")) {

				final Dialog searchDialog = new Dialog(ParkActivity.this);
				searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				searchDialog.setContentView(R.layout.wn_map_search_dialog);
				searchDialog.setCancelable(false);
				searchDialog.show();

				final LocationResult locationResult = new LocationResult() {
					@Override
					public void gotLocation(Location location) {
						loc = location;
					}
				};

				cdt = new CountDownTimer(10000, 1000) {

					@Override
					public void onTick(long millisUntilFinished) {
						Log.d("", "onTick " + millisUntilFinished);
						if (loc == null) {
							LocationHelper myLocation = new LocationHelper();
							myLocation.getLocation(ParkActivity.this,
									locationResult);
						} else {
							/*
							 * searchDialog.dismiss(); double lat =
							 * loc.getLatitude(); double longi =
							 * loc.getLongitude();
							 * 
							 * myPrefs = ParkActivity.this.getSharedPreferences(
							 * "myPrefs", MODE_WORLD_READABLE);
							 * SharedPreferences.Editor prefsEditor = myPrefs
							 * .edit(); Log.d("lat", ":" + lat + " Long:" +
							 * longi); prefsEditor.putString(SRCLAT, "" + lat);
							 * prefsEditor.putString(SRCLONG, "" + longi);
							 * prefsEditor.commit();
							 */
							cdt.cancel();
						}
					}

					@Override
					public void onFinish() {
						searchDialog.dismiss();
						if (loc != null) {
							double lat = loc.getLatitude();
							double longi = loc.getLongitude();

							myPrefs = ParkActivity.this.getSharedPreferences(
									"myPrefs", MODE_WORLD_READABLE);
							SharedPreferences.Editor prefsEditor = myPrefs
									.edit();
							Log.d("lat", ":" + lat + " Long:" + longi);
							prefsEditor.putString(SRCLAT, "" + lat);
							prefsEditor.putString(SRCLONG, "" + longi);
							prefsEditor.commit();

							Calendar c = Calendar.getInstance();
							// c.set(mYear, mMonth, mDay, mHour,
							// mMinute,mSec);

							// SharedPreferences myPrefs = ParkActivity.this
							// .getSharedPreferences("myPrefs",
							// MODE_WORLD_READABLE);
							// SharedPreferences.Editor prefsEditor =
							// myPrefs.edit();
							long millis = c.getTimeInMillis();
							Log.d("millis", "" + millis);
							long minutes = 0;
							if (!edtParkMin.getText().toString().equals("")) {
								minutes = Long.parseLong(edtParkMin.getText()
										.toString());
							}
							long mins = minutes * 60000;
							millis = millis + mins;
							Log.d("millis", "" + millis);
							prefsEditor.putString(MILLIS, "" + millis);
							prefsEditor.commit();

							Log.d("Date :", "" + mYear + ":" + mMonth + ":"
									+ mDay + ":" + mHour + ":" + mMinute);
							Log.d("Time in millis :", "" + c.getTimeInMillis()
									+ "  :  " + System.currentTimeMillis());

							Intent intent = new Intent(ParkActivity.this,
									MyBroadcastReceiver.class);
							PendingIntent pendingIntent = PendingIntent
									.getBroadcast(ParkActivity.this
											.getApplicationContext(),
											234324243, intent, 0);
							AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
							alarmManager.set(AlarmManager.RTC_WAKEUP, millis,
									pendingIntent);

							parkinfo = ParkActivity.this.getSharedPreferences(
									"parkinfo", MODE_WORLD_READABLE);
							prefsEditor = parkinfo.edit();
							prefsEditor.putString(SPACE,
									"" + edtParkSpace.getText());
							prefsEditor.putString(STARTTIME,
									"" + edtStartTime.getText());
							prefsEditor.putString(MIN,
									"" + edtParkMin.getText());
							prefsEditor.commit();

							// prefsEditor.putString(SRCLAT, "nothing");
							// prefsEditor.putString(SRCLONG, "nothing");
							// prefsEditor.commit();
							// prefsEditor.clear();

							if (getPreference()) {
								setTimer();
							}
						} else {
							Toast.makeText(ParkActivity.this,
									"Unable to find location!",
									Toast.LENGTH_SHORT).show();
						}
					}
				}.start();

				// }
			}
		});

		btnDirection = (Button) findViewById(R.id.btnDirection);
		btnDirection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// NetworkHelper networkhelp = new NetworkHelper();
				if (networkhelp.isOnline(ParkActivity.this)) {

					final Dialog searchDialog = new Dialog(ParkActivity.this);
					searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					searchDialog.setContentView(R.layout.wn_map_search_dialog);
					searchDialog.setCancelable(false);
					searchDialog.show();

					AirPlaneMode airPlane = new AirPlaneMode();
					if (("false").equalsIgnoreCase(""
							+ airPlane.isAirplaneModeOn(ParkActivity.this))) {

						final LocationResult locationResult = new LocationResult() {
							@Override
							public void gotLocation(Location location) {
								loc = location;
							}
						};

						cdt = new CountDownTimer(10000, 1000) {

							@Override
							public void onTick(long millisUntilFinished) {
								Log.d("", "onTick " + millisUntilFinished);
								if (loc == null) {
									LocationHelper myLocation = new LocationHelper();
									myLocation.getLocation(ParkActivity.this,
											locationResult);
								} else {
									// searchDialog.dismiss();
									// double lat = loc.getLatitude();
									// double longi = loc.getLongitude();
									// cdt.cancel();
								}
							}

							@Override
							public void onFinish() {
								searchDialog.dismiss();
								/*
								 * Toast.makeText(ParkActivity.this,
								 * "Unable to find location.",
								 * Toast.LENGTH_SHORT) .show();
								 */
								if (loc != null) {
									myPrefs = ParkActivity.this
											.getSharedPreferences("myPrefs",
													MODE_WORLD_READABLE);
									prefsEditor = myPrefs.edit();
									String prefName1 = myPrefs.getString(
											SRCLAT, "nothing");
									String prefName2 = myPrefs.getString(
											SRCLONG, "nothing");
									Log.d("prefName", ": " + prefName1 + ": "
											+ prefName2);

									if ((prefName1).equalsIgnoreCase("nothing")
											&& (prefName2)
													.equalsIgnoreCase("nothing")) {
										Log.d("sorry ", "sorry no lat long");
										Toast.makeText(ParkActivity.this,
												"Parking Location Not Found!",
												Toast.LENGTH_LONG).show();
										// prgDialog.dismiss();
									} else {

										/*
										 * String url = "google.navigation:q=" +
										 * Double.valueOf(prefName1) + "," +
										 * Double.valueOf(prefName2); Intent i =
										 * new Intent( Intent.ACTION_VIEW, Uri
										 * .parse(url)); startActivity(i);
										 */
										Intent intent = new Intent(
												ParkActivity.this,
												CurrDestDirectionActivity.class);
										intent.putExtra("lat", prefName1);
										intent.putExtra("lng", prefName2);
										intent.putExtra("currlat",
												"" + loc.getLatitude());
										intent.putExtra("currlng",
												"" + loc.getLongitude());
										ParkActivity.this.startActivity(intent);
									}

								} else {
									Toast.makeText(ParkActivity.this,
											"Unable to find location.",
											Toast.LENGTH_SHORT).show();
								}
							}
						}.start();

					} else {
						new AlertDialog.Builder(ParkActivity.this)
								.setMessage(
										"Phone Service & Internet not available. Unable to check In.")
								.setNeutralButton("Close",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dlg,
													int sumthin) {
												// do nothing – it will close on
												// its
												// own
											}
										}).show();
					}
				} else {
					Toast.makeText(ParkActivity.this,
							"No Internet Connection!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTimer() {
		// TODO Auto-generated method stub
		cdt = new CountDownTimer(sub, 1000) {

			@Override
			public void onTick(long arg0) {
				// TODO Auto-generated method stub
				isTimer = 1;
				if (sec >= 0) {
					if (sec < 10 && sec > 0) {
						if (minutes < 10 && minutes > 0) {
							txtRemainingTime
									.setText("0" + minutes + ":0" + sec);
						} else if(minutes == 0){
							txtRemainingTime.setText("00:" + sec);
						} else {
							txtRemainingTime.setText(minutes + ":0" + sec);
						}
					} else if (minutes < 10 && minutes > 0) {
						txtRemainingTime.setText("0" + minutes + ":" + sec);
					} else if(minutes == 0){
						txtRemainingTime.setText("00:" + sec);
					} else {
						txtRemainingTime.setText(minutes + ":" + sec);
					}
					sec--;
				} else {
					minutes--;
					sec = 59;
					if (minutes < 10 && minutes > 0) {
						txtRemainingTime.setText("0" + minutes + ":" + sec);
					} else if(minutes == 0){
						txtRemainingTime.setText("00:" + sec);
					}else{
						txtRemainingTime.setText(minutes + ":" + sec);
					}
					sec--;
				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				isTimer = 0;
				prefsEditor.putString(MILLIS, "");
				prefsEditor.commit();
				prefsEditor.clear();
				prefName = myPrefs.getString(MILLIS, "nothing");
				Log.d("perfName", "" + prefName);
				seconds = 0;
				minutes = 0;
				prefName = null;
				Log.d("onfinish", "" + prefName);
				txtRemainingTime.setText("0:0");
			}
		}.start();
	}

	public boolean getPreference() {
		// TODO Auto-generated method stub
		myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		prefsEditor = myPrefs.edit();
		String prefName = myPrefs.getString(MILLIS, "nothing");
		Log.d("prefName", "" + prefName);
		if ((prefName).equalsIgnoreCase("nothing")) {
			return false;
		} else {
			if ((prefName).equalsIgnoreCase("")) {
				return false;
			} else {

				Calendar cal = Calendar.getInstance();
				Log.d("current millis",
						cal.getTimeInMillis() + " : "
								+ System.currentTimeMillis() + " :sharedP : "
								+ prefName);
				long l = Long.parseLong(prefName);
				Log.d("sharedP", "" + l);
				if (l > cal.getTimeInMillis()) {
					substract = l - (cal.getTimeInMillis());
					sub = substract + 1000;
					Log.d("substract", "" + substract);
					seconds = substract / 1000;
					sec = (int) (seconds % 60);
					Log.d("sec", "" + sec);
					minutes = seconds / 60;
					Log.d("remaining min", ": " + minutes);
				}
				return true;
			}
		}
	}

	private void updateDisplay() {
		String am_pm;
		if (mHour > 12) {
			mHour = mHour - 12;
			am_pm = "pm";
		} else {
			am_pm = "am";
		}

		edtStartTime.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)).append(am_pm));
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			Log.d("", "" + hourOfDay);
			mMinute = minute;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		}
		return null;
	}
}
