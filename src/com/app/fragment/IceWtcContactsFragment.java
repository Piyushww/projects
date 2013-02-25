package com.app.fragment;

import com.world.nearby.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

public class IceWtcContactsFragment extends DialogFragment{
	
	EditText edtName;
	EditText edtRelation;
	EditText edtPhone;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, 0);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.ice_wtc_contacts_fragment, null);
		init(view);
		return view;
	}

	private void init(View view) {
		edtName = (EditText)view.findViewById(R.id.edtName1);
		edtRelation = (EditText)view.findViewById(R.id.edtRelation1);
		edtPhone = (EditText)view.findViewById(R.id.edtPhone1);
	}

}
