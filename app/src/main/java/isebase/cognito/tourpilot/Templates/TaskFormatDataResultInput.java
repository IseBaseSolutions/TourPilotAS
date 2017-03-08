package isebase.cognito.tourpilot.Templates;

import isebase.cognito.tourpilot.StaticResources.StaticResources;
import android.graphics.Color;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskFormatDataResultInput {
	
	private TextView tvLabel;
	private EditText etInputValue;
	private LinearLayout linearRow;
	
	private int iLabelValue = 0;
	
	private int iTextSize = 20;
	
	private int iDataType = 0;
	
	public String getstrInputValue(){
		return this.etInputValue.getText().toString();
	}
	public String getstrLabelName(){
		return this.tvLabel.getText().toString();
	}
	
	private InputFilter[] FilterArray = new InputFilter[1];

	private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT,
			LayoutParams.MATCH_PARENT, 1.0f);
	
	public TaskFormatDataResultInput(int iLabelValue, int iLengthSymbol, int[] iDataTypes){
		this.iLabelValue = iLabelValue;
		
		for(int i : iDataTypes){
			iDataType = iDataType | i;
		}
		
		params.leftMargin = params.rightMargin =  10;

		linearRow = new LinearLayout(StaticResources.getBaseContext());
		linearRow.setOrientation(LinearLayout.HORIZONTAL);
		
		FilterArray[0] = new InputFilter.LengthFilter(iLengthSymbol);
	}
	private TextView getValueLabel(){
		
		tvLabel = new TextView(StaticResources.getBaseContext());
		tvLabel.setText(iLabelValue);
		tvLabel.setTextSize(iTextSize);
		tvLabel.setLayoutParams(params);
		tvLabel.setTextColor(Color.BLACK);
		return tvLabel;
	}

	private EditText getEditValue(){
		
		etInputValue = new EditText(StaticResources.getBaseContext());
		etInputValue.setTextSize(iTextSize);
		etInputValue.setInputType(iDataType);
		etInputValue.setLayoutParams(params);
		etInputValue.setFilters(FilterArray);
		etInputValue.setTextColor(Color.BLACK);
		return etInputValue;
	}
	
	public LinearLayout getRowInputData(){
		
		linearRow.addView(getValueLabel());
		linearRow.addView(getEditValue());
		
		return linearRow;
	}
}
