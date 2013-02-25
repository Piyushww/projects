package com.app.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.util.CallBackListner;
import com.app.util.PreferenceUtils;
import com.app.util.WorldNearbyConstants;
import com.world.nearby.R;

public class IceInsuranceFragment extends DialogFragment implements
		OnClickListener {

	EditText edtUserName, edtAge, edtHeight, edtWeight, edtBloodType,
			edtSpeaks, edtDocName, edtDocPhone, edtInsuranceCo, edtInsuranceNo,
			edtAgentName, edtAgentPhone;
	Button btnSave, btnCancel;
	
	CallBackListner callBackListner;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callBackListner = (CallBackListner)activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, 0);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.ice_id_insurance_fragment, null);

		init(view);

		return view;
	}

	private void init(View view) {
		edtUserName = (EditText) view.findViewById(R.id.edtUserName);
		edtUserName.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_USERNAME));
		edtAge = (EditText) view.findViewById(R.id.edtAge);
		edtAge.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_AGE));
		edtHeight = (EditText) view.findViewById(R.id.edtHeight);
		edtHeight.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_HEIGHT));
		edtWeight = (EditText) view.findViewById(R.id.edtWeight);
		edtWeight.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_WEIGHT));
		edtBloodType = (EditText) view.findViewById(R.id.edtBooldType);
		edtBloodType.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_BLOOD_TYPE));
		edtSpeaks = (EditText) view.findViewById(R.id.edtSpeaks);
		edtSpeaks.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_SPEAKS));
		edtDocName = (EditText) view.findViewById(R.id.edtDoctorName);
		edtDocName.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_DOC_NAME));
		edtDocPhone = (EditText) view.findViewById(R.id.edtDoctorPhone);
		edtDocPhone.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_DOC_PHONE));
		edtInsuranceCo = (EditText) view.findViewById(R.id.edtInsuranceCompany);
		edtInsuranceCo.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_INSURANCE_CO));
		edtInsuranceNo = (EditText) view.findViewById(R.id.edtInsuranceNumber);
		edtInsuranceNo.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_INSURANCE_NO));
		edtAgentName = (EditText) view.findViewById(R.id.edtAgentName);
		edtAgentName.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_AGENT_NAME));
		edtAgentPhone = (EditText) view.findViewById(R.id.edtAgentPhone);
		edtAgentPhone.setText(PreferenceUtils.getString(getActivity(), PreferenceUtils.KEY_AGENT_PHONE));

		btnSave = (Button) view.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSave:
			new IceSaveAsyncTask(this).execute();
			
			break;
		case R.id.btnCancel:
			dismiss();
			break;

		default:
			break;
		}
	}
	
	private class IceSaveAsyncTask extends AsyncTask<Void, Void, Boolean>{
		Dialog searchDialog;
		IceInsuranceFragment iceInsuranceFragment;
		public IceSaveAsyncTask(IceInsuranceFragment iceInsuranceFragment) {
			this.iceInsuranceFragment = iceInsuranceFragment;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			searchDialog = new Dialog(getActivity());
			searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.wn_map_search_dialog, null);
			TextView txtPrgDialog = (TextView)view.findViewById(R.id.txtProgressDialog);
			txtPrgDialog.setText("Storing Info...");
			searchDialog.setContentView(view);
			searchDialog.setCancelable(false);
			searchDialog.show();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			try{
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_USERNAME, edtUserName.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_AGE, edtAge.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_HEIGHT, edtHeight.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_WEIGHT, edtWeight.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_BLOOD_TYPE, edtBloodType.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_SPEAKS, edtSpeaks.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_DOC_NAME, edtDocName.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_DOC_PHONE, edtDocPhone.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_INSURANCE_CO, edtInsuranceCo.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_INSURANCE_NO, edtInsuranceNo.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_AGENT_NAME, edtAgentName.getText().toString());
			PreferenceUtils.putString(getActivity(), PreferenceUtils.KEY_AGENT_PHONE, edtAgentPhone.getText().toString());
			return true;
			}catch (Exception e) {
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			searchDialog.dismiss();
			if(result == true){
				Toast.makeText(getActivity(), "Info stored successfuly.", Toast.LENGTH_SHORT).show();
				iceInsuranceFragment.dismiss();
			}else{
				Toast.makeText(getActivity(),
						"Error storing info, Please try again.",
						Toast.LENGTH_SHORT).show();
			}
			callBackListner.callBack(WorldNearbyConstants.KEY_ID_INSURANCE);
		}
		
	}
}
