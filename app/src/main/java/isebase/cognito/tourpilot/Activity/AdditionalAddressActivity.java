package isebase.cognito.tourpilot.Activity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.Data.PatientAdditionalAddress.PatientAdditionalAddress;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Templates.AdditionalAddressAdapter;

/**
 * Created by Kostya on 15.02.2018.
 */

public class AdditionalAddressActivity extends BaseActivity {

    private List<PatientAdditionalAddress> addresses;
    private Employment employment;
    AdditionalAddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_additional_address);
            reloadData();
            fillUp();
            fillUpTitle();
        }
        catch(Exception ex){
            ex.printStackTrace();
            criticalClose();
        }
    }

    private void fillUp(){
        adapter = new AdditionalAddressAdapter(this, R.layout.row_additional_address_template, addresses);
        ListView addressListView = (ListView) findViewById(R.id.lvListAdditionalAddress);
        addressListView.setAdapter(adapter);
    }

    public void reloadData() {
        employment = HelperFactory.getHelper().getEmploymentDAO().loadAll((int) Option.Instance().getEmploymentID());
        addresses = HelperFactory.getHelper().getPatientAdditionalAddressDAO().loadByPatientID(employment.getPatientID());
    }

    private void fillUpTitle(){
        setTitle(getString(R.string.menu_additional_address_short) + ", " + employment.getPatient().getFullName());
    }

}
