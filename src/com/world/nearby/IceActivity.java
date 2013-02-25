package com.world.nearby;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fragment.IceInsuranceFragment;
import com.app.fragment.IceWtcContactsFragment;
import com.app.helper.ui.ContactsModel;
import com.app.util.CallBackListner;
import com.app.util.PreferenceUtils;

public class IceActivity extends FragmentActivity implements OnCheckedChangeListener, CallBackListner {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	RadioGroup radioGrpIce;
	ListView lstContacts;
	ContactsAdapter contactsAdapter;
	LinearLayout lytOptions;
	LinearLayout lytWhotocall;
	LayoutInflater layoutInflater;
	
	TextView txtUserName, txtAge, txtHeight, txtWeight, txtBlood, txtSpeaks, txtDocName, txtDocPhone, txtInsuranceName,
	txtInsuranceNo, txtAgentName, txtAgentPhone;
	
	FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wn_ice);

		radioGrpIce = (RadioGroup)findViewById(R.id.radioGroup);
		radioGrpIce.setOnCheckedChangeListener(this);
		
		lytOptions = (LinearLayout)findViewById(R.id.lytOptions);
		
		layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fm = getSupportFragmentManager();
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radioWhotocall:
			if (lytOptions.getChildCount() != 0) {
				lytOptions.removeAllViews();
			}
			lytWhotocall = (LinearLayout) layoutInflater
					.inflate(R.layout.ice_who_to_call, null);
			lytOptions.addView(lytWhotocall);
			lstContacts = (ListView) lytWhotocall
					.findViewById(R.id.lstContacts);
			contactsAdapter = new ContactsAdapter(this, 0);
			lstContacts.setAdapter(contactsAdapter);
//			IceWtcContactsFragment iceWtcContactsFragment = new IceWtcContactsFragment();
//			iceWtcContactsFragment.show(fm, "contacts");
			break;
		case R.id.radioIdinsurance:
			if (lytOptions.getChildCount() != 0) {
				lytOptions.removeAllViews();
			}
			LinearLayout lytIdinsurance = (LinearLayout) layoutInflater
					.inflate(R.layout.ice_id_insurance, null);
			lytOptions.addView(lytIdinsurance);
			Button btnEdit = (Button)lytIdinsurance.findViewById(R.id.btnEdit);
			btnEdit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IceInsuranceFragment iceInsuranceFragment = new IceInsuranceFragment();
					iceInsuranceFragment.show(fm, "insurance");
				}
			});
			txtUserName = (TextView) lytIdinsurance.findViewById(R.id.txtUserName);
			txtAge = (TextView) lytIdinsurance.findViewById(R.id.txtAge);
			txtHeight = (TextView) lytIdinsurance.findViewById(R.id.txtHeight);
			txtWeight = (TextView) lytIdinsurance.findViewById(R.id.txtWeight);
			txtBlood = (TextView) lytIdinsurance.findViewById(R.id.txtBlood);
			txtSpeaks = (TextView) lytIdinsurance.findViewById(R.id.txtSpeaks);
			txtDocName = (TextView) lytIdinsurance.findViewById(R.id.txtDoctorName);
			txtDocPhone = (TextView) lytIdinsurance.findViewById(R.id.txtDocPhone);
			txtInsuranceName = (TextView) lytIdinsurance.findViewById(R.id.txtInsurance);
			txtInsuranceNo = (TextView) lytIdinsurance.findViewById(R.id.txtInsuranceNo);
			txtAgentName = (TextView) lytIdinsurance.findViewById(R.id.txtAgent);
			txtAgentPhone = (TextView) lytIdinsurance.findViewById(R.id.txtAgentPhone);
			updateInfo();
			if(txtUserName.getText().toString().equals("")){
				Toast.makeText(this, "Incomplete record, please fill it first.", Toast.LENGTH_SHORT).show();
				IceInsuranceFragment iceInsuranceFragment = new IceInsuranceFragment();
				iceInsuranceFragment.show(fm, "insurance");
			}
			break;
		case R.id.radioAllergies:
			if (lytOptions.getChildCount() != 0) {
				lytOptions.removeAllViews();
			}
			LinearLayout lytAllergies = (LinearLayout) layoutInflater
					.inflate(R.layout.ice_allergies, null);
			lytOptions.addView(lytAllergies);
			break;
		case R.id.radioConditions:
			if (lytOptions.getChildCount() != 0) {
				lytOptions.removeAllViews();
			}
			LinearLayout lytConditions = (LinearLayout) layoutInflater
					.inflate(R.layout.ice_conditions, null);
			lytOptions.addView(lytConditions);
			break;
		case R.id.radioMedications:
			if (lytOptions.getChildCount() != 0) {
				lytOptions.removeAllViews();
			}
			LinearLayout lytMedications = (LinearLayout) layoutInflater
					.inflate(R.layout.ice_medications, null);
			lytOptions.addView(lytMedications);
			break;
		case R.id.radioFamilyinfo:
			if (lytOptions.getChildCount() != 0) {
				lytOptions.removeAllViews();
			}
			lytOptions.addView(lytWhotocall);
			lstContacts.setAdapter(contactsAdapter);
			break;

		default:
			break;
		}
	}
	
	public class ContactsAdapter extends ArrayAdapter<ContactsModel>{

		ArrayList<ContactsModel> contactsModels;
		LayoutInflater mInflater;
		ViewHolder holder = null;
		
		public ContactsAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			contactsModels = new ArrayList<ContactsModel>();
		}
		
		@Override
		public void add(ContactsModel object) {
			super.add(object);
			contactsModels.add(object);
		}
		
		public class ViewHolder{
			TextView txtName, txtRelation, txtNumber;
			ImageView imgEdit;
		}
		
		@Override
		public int getCount() {
			
			return 5;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.ice_who_to_call_inflater, null);
				holder = new ViewHolder();
				holder.txtName = (TextView)convertView.findViewById(R.id.txtWTCName);
				holder.txtRelation = (TextView)convertView.findViewById(R.id.txtWTCRelation);
				holder.txtNumber = (TextView)convertView.findViewById(R.id.txtWTCNumber);
				holder.imgEdit = (ImageView)convertView.findViewById(R.id.imgEdit);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imgEdit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IceWtcContactsFragment iceWtcContactsFragment = new IceWtcContactsFragment();
					iceWtcContactsFragment.show(fm, "contacts");
				}
			});
			
//			ContactsModel cntModel = contactsModels.get(position);
//			holder.txtName.setText(cntModel.getName());
//			holder.txtRelation.setText(cntModel.getRelation());
//			holder.txtNumber.setText(cntModel.getNumber());
			
			return convertView;
		}
		
	}

	@Override
	public void callBack(int token) {
		switch (token) {
		case 1001://WorldNearbyConstants.KEY_ID_INSURANCE
			updateInfo();
			break;

		default:
			break;
		}
	}
	
	private void updateInfo(){
		txtUserName.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_USERNAME));
		txtAge.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_AGE));
		txtHeight.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_HEIGHT));
		txtWeight.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_WEIGHT));
		txtBlood.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_BLOOD_TYPE));
		txtSpeaks.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_SPEAKS));
		txtDocName.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_DOC_NAME));
		txtDocPhone.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_DOC_PHONE));
		txtInsuranceName.setText(" "+PreferenceUtils.getString(this, PreferenceUtils.KEY_INSURANCE_CO));
		txtInsuranceNo.setText(" "+PreferenceUtils.getString(this, PreferenceUtils.KEY_INSURANCE_NO));
		txtAgentName.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_AGENT_NAME));
		txtAgentPhone.setText(PreferenceUtils.getString(this, PreferenceUtils.KEY_AGENT_PHONE));
	}
}
