package isebase.cognito.tourpilot_apk.Templates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import isebase.cognito.tourpilot_apk.Data.PatientAdditionalAddress.PatientAdditionalAddress;
import isebase.cognito.tourpilot_apk.R;

/**
 * Created by Kostya on 15.02.2018.
 */

public class AdditionalAddressAdapter extends ArrayAdapter<PatientAdditionalAddress> {

    private List<PatientAdditionalAddress> listAddress;
    private int layoutResourceId;
    private Context context;

    private View row;

    public AdditionalAddressAdapter(@NonNull Context context, @LayoutRes int layoutResourceId, @NonNull List<PatientAdditionalAddress> listAddress) {
        super(context, layoutResourceId, listAddress);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listAddress = listAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;

        AdditionalAddressHolder addressHolder = new AdditionalAddressHolder();
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        addressHolder.address = listAddress.get(position);

        initTextView(addressHolder.tvArtName, row, R.id.tvArtName, addressHolder.address.getArtName());
        initTextView(addressHolder.tvAdditionalAddressName, row, R.id.tvAdditionalAddressName, addressHolder.address.getName());
        initTextView(addressHolder.tvAddressData, row, R.id.tvAddressData, addressHolder.address.getAddressData());
        initTextView(addressHolder.tvFax, row, R.id.tvFax, addressHolder.address.getFax());
        initTextView(addressHolder.tvEmail, row, R.id.tvEmail, addressHolder.address.getEmail());
        initTextView(addressHolder.tvAddressInfo, row, R.id.tvAddressInfo, addressHolder.address.getInfo());

        boolean isAnyphonesAdded = false;
        TableLayout tablePhones = (TableLayout)row.findViewById(R.id.tablePhones);
        if(addressHolder.address.getRealPhone().length() > 0) {

            TableRow rowPhone = showPhone(R.string.phone, addressHolder.address.getPhone(), addressHolder.address.getRealPhone(), tablePhones);
            tablePhones.addView(rowPhone);
            isAnyphonesAdded = true;
        }

        if(addressHolder.address.getRealMobilePhone().length() > 0) {

            TableRow rowMobilePhone = showPhone(R.string.phone_mobile, addressHolder.address.getMobilePhone(), addressHolder.address.getRealMobilePhone(), tablePhones);
            tablePhones.addView(rowMobilePhone);
            isAnyphonesAdded = true;
        }

        if(!isAnyphonesAdded){
            tablePhones.setVisibility(View.INVISIBLE);
        }

        return row;
    }

    private void initTextView(TextView sourceTextView, View row, int tvID, String text){
        sourceTextView = (TextView) row.findViewById(tvID);
        if(text.length() > 0) {
            sourceTextView.setText(text);
        } else{
            sourceTextView.setVisibility(View.INVISIBLE);
        }
    }

    private TableRow showPhone(int PhoneLabel, String strPhoneNumber, String strRealphoneNumber, TableLayout tablePhones) {

        View.OnClickListener imageClickListener;
        imageClickListener = new View.OnClickListener() {

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
        tvPhone.setTypeface(Typeface.MONOSPACE, Typeface.BOLD | Typeface.ITALIC);
        tvPhone.setGravity(Gravity.RIGHT | Gravity.CENTER_HORIZONTAL | Gravity.FILL);
        tvPhone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        llPhone.setOrientation(LinearLayout.VERTICAL);
        llPhone.setWeightSum(1);
        TableRow.LayoutParams paramsLL = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        paramsLL.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        llPhone.setLayoutParams(paramsLL);

        llPhone.addView(tvLabelPhone);
        llPhone.addView(tvPhone);

        TableRow.LayoutParams paramsButton = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
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


    public class AdditionalAddressHolder {

        PatientAdditionalAddress address;

        TextView tvArtName;
        TextView tvAdditionalAddressName;
        TextView tvAddressData;
        TextView tvAddressInfo;
        TextView tvFax;
        TextView tvEmail;
    }

    public void onCallPhone(View view) {
        String realPhone = (String) view.getTag();
        try{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + realPhone));
            getContext().startActivity(callIntent);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
