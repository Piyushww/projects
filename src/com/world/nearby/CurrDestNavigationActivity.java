package com.world.nearby;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CurrDestNavigationActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	private ListView lstNews;
	private NavigationListAdapter mAdapter;
	private TextView txtFrom, txtTo, txtDistance, txtDuration, txtDirection;
	private HashMap<String, Object> result;
	private ImageView imgDirection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wn_curr_dest_navigation);
		
		lstNews = (ListView)findViewById(R.id.lstNavi);
		mAdapter = new NavigationListAdapter();

		lstNews.setAdapter(mAdapter);
//		mAdapter.notifyDataSetChanged();
		
		result = (HashMap<String, Object>) getIntent().getSerializableExtra(
				"result");
		Log.d("", "result.size " + result.size());

		ArrayList<HashMap<String, Object>> steps = (ArrayList<HashMap<String, Object>>) result
				.get("steps");
		for (int instructionCount = 0; instructionCount < steps.size(); instructionCount++) {
			mAdapter.addItem(steps.get(instructionCount));
		}

		txtDistance = (TextView) findViewById(R.id.txtDistance);
		txtDuration = (TextView) findViewById(R.id.txtDuration);
		txtFrom = (TextView) findViewById(R.id.txtFrom);
		txtTo = (TextView) findViewById(R.id.txtTo);

		txtDistance.setText("Distance : " + result.get("distance"));
		txtDuration.setText("Duration : " + result.get("duration"));
		txtFrom.setText("From :\n" + result.get("startAddress"));
		txtTo.setText("To :\n" + result.get("endAddress"));
		
		txtDirection = (TextView)findViewById(R.id.txtDirection);
		txtDirection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		imgDirection = (ImageView)findViewById(R.id.imgDirection);
		imgDirection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
			
			holder.txtInstruction.setText(Html.fromHtml(mData.get(position).get("html_instructions").toString()));
			holder.txtDistance.setText(mData.get(position).get("distance").toString());
			holder.txtDuration.setText(mData.get(position).get("duration").toString());
			
			return convertView;
		}
	}
}
