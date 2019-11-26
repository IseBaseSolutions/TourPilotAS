package isebase.cognito.tourpilot_apk.Templates;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.Data.Address.IAddressable;
import isebase.cognito.tourpilot_apk.Data.Doctor.Doctor;
import isebase.cognito.tourpilot_apk.Data.Patient.Patient;
import isebase.cognito.tourpilot_apk.Data.Relative.Relative;
import isebase.cognito.tourpilot_apk.Data.Worker.Worker;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AddressAdapter<T extends IAddressable> extends ArrayAdapter<T> {

	private List<T> listAddress;
	private int layoutResourceId;
	private Context context;

	private View row;
	
	public AddressAdapter(Context context, int layoutResourceId, List<T> listAddress){
		super(context, layoutResourceId, listAddress);
		
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.listAddress = listAddress;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		row = convertView;

		AddressHolder addressHolder = new AddressHolder();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		addressHolder.address = listAddress.get(position);
		
		String additionalInfo = ""; 
		if (!(addressHolder.address instanceof Patient || addressHolder.address instanceof Worker))
			additionalInfo = addressHolder.address instanceof Relative ? ((Relative)addressHolder.address).getFamilyState() : ((Doctor)addressHolder.address).getSpeciality();
		addressHolder.tvFullname = (TextView) row.findViewById(R.id.tvFullName);		
		addressHolder.tvFullname.setText(String.format("%s%s", addressHolder.address.getFullName(), (additionalInfo.equals("") ? "" : String.format(" (%s)", additionalInfo))));
		
		addressHolder.tvAddressName = (TextView) row.findViewById(R.id.tvAddressName);		
		addressHolder.tvAddressName.setText(addressHolder.address.getAddress().getAddressData());
		if (addressHolder.address instanceof Patient) {
			addressHolder.tvBirthDate = (TextView) row.findViewById(R.id.tvBirthDate);
			addressHolder.tvBirthDate.setVisibility(View.VISIBLE);
			addressHolder.tvBirthDate.setText(((Patient)addressHolder.address).getBirthdate());
		}
		
		TableLayout tablePhones = (TableLayout)row.findViewById(R.id.tablePhones);
		if (addressHolder.address instanceof Doctor && !((Doctor)addressHolder.address).getNote().equals(""))
		{
			TextView tvNote = new TextView(context);
			tvNote.setText(String.format("%s: %s\n", context.getString(R.string.menu_comments), ((Doctor)addressHolder.address).getNote()));
			tvNote.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			tablePhones.addView(tvNote);
		}
		if(addressHolder.address.getAddress().getRealPhone().length() > 0) {
			
			TableRow rowPhone = showPhone(R.string.phone, addressHolder.address.getAddress().getPhone(), addressHolder.address.getAddress().getRealPhone(), tablePhones);
			tablePhones.addView(rowPhone);
		}
		if(addressHolder.address.getAddress().getRealPrivatePhone().length() > 0) {
			
			TableRow rowPrivatePhone = showPhone(R.string.phone_private, addressHolder.address.getAddress().getPrivatePhone(), addressHolder.address.getAddress().getRealPrivatePhone(), tablePhones);
			tablePhones.addView(rowPrivatePhone);
		}
		if(addressHolder.address.getAddress().getRealMobilePhone().length() > 0) {
			
			TableRow rowMobilePhone = showPhone(R.string.phone_mobile, addressHolder.address.getAddress().getMobilePhone(), addressHolder.address.getAddress().getRealMobilePhone(), tablePhones);
			tablePhones.addView(rowMobilePhone);
		}

		if (addressHolder.address instanceof Patient) {
			if(addressHolder.address.getAddress().getAdditionalInfo().length() > 0) {
				addressHolder.tvAddressAdditionalInfo = (TextView) row.findViewById(R.id.tvAddressAdditionalInfo);
				addressHolder.tvAddressAdditionalInfo.setVisibility(View.VISIBLE);
				addressHolder.tvAddressAdditionalInfo.setText(String.format("%s %s", context.getString(R.string.address_additional_info),
						addressHolder.address.getAddress().getAdditionalInfo()));
			}
		}
		return row;
	}
	
	private TableRow showPhone(int PhoneLabel, String strPhoneNumber, String strRealphoneNumber, TableLayout tablePhones) {
		
		OnClickListener imageClickListener;
		imageClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCallPhone(v);           
			}
		};
		int textSizeMiddle = 20;
		
		TableRow rowPhone = new TableRow(tablePhones.getContext());
		LinearLayout llPhone = new LinearLayout(rowPhone.getContext());
		TextView tvLabelPhone = new TextView(llPhone.getContext());
		TextView tvPhone = new TextView(llPhone.getContext());
		ImageButton ibCall = new ImageButton(rowPhone.getContext());
		
		tvLabelPhone.setText(PhoneLabel);
		tvLabelPhone.setTextSize(textSizeMiddle);
		
		tvPhone.setText(strPhoneNumber);
		tvPhone.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textSizeMiddle);
		tvPhone.setTypeface(Typeface.MONOSPACE, Typeface.BOLD_ITALIC);
		tvPhone.setGravity(Gravity.RIGHT | Gravity.CENTER_HORIZONTAL | Gravity.FILL);
		tvPhone.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		llPhone.setOrientation(LinearLayout.VERTICAL);
		llPhone.setWeightSum(1);
		TableRow.LayoutParams paramsLL = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.5f);
		paramsLL.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		llPhone.setLayoutParams(paramsLL);

		llPhone.addView(tvLabelPhone);
		llPhone.addView(tvPhone);

		TableRow.LayoutParams paramsButton = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.5f);
		paramsButton.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		ibCall.setLayoutParams(paramsButton);
		ibCall.setImageResource(android.R.drawable.stat_sys_phone_call);
		ibCall.setClickable(true);
		ibCall.setId(PhoneLabel);
		ibCall.setOnClickListener(imageClickListener);
		ibCall.setTag(strRealphoneNumber);

		rowPhone.addView(llPhone);
		rowPhone.addView(ibCall);

		return rowPhone; 
	}
	
	public class AddressHolder {

		IAddressable address;

		TextView tvFullname;
		
		/*ImageButton imageCallPhone;
		ImageButton imageCallPrivatePhone;
		ImageButton imageCallMobilePhone;

		TextView tvPhone;
		TextView tvPrivatePhone;
		TextView tvMobilePhone;*/
		
		TextView tvAddressName;
		TextView tvBirthDate;
		TextView tvAddressAdditionalInfo;
	}
	
	public void onCallPhone(View view) {		
		String realPhone = (String) view.getTag();
		try{

			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				Intent dialIntent = new Intent(Intent.ACTION_DIAL);
				dialIntent.setData(Uri.parse("tel:" + realPhone));
				getContext().startActivity(dialIntent);
			}else {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + realPhone));
				getContext().startActivity(callIntent);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}

