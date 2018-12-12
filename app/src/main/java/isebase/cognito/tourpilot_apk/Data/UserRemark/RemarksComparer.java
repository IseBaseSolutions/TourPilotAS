package isebase.cognito.tourpilot_apk.Data.UserRemark;

import isebase.cognito.tourpilot_apk.Data.CustomRemark.CustomRemark;

import java.util.Comparator;

public class RemarksComparer implements Comparator<CustomRemark> {

	@Override
	public int compare(CustomRemark lhs, CustomRemark rhs) {
		// TODO Auto-generated method stub
		return ((Integer)lhs.getPosNumber()).compareTo((Integer)rhs.getPosNumber());
	}	

}
