package isebase.cognito.tourpilot.Data.UserRemark;

import isebase.cognito.tourpilot.Data.CustomRemark.CustomRemark;

import java.util.Comparator;

public class RemarksComparer implements Comparator<CustomRemark> {

	@Override
	public int compare(CustomRemark lhs, CustomRemark rhs) {
		// TODO Auto-generated method stub
		return ((Integer)lhs.getPosNumber()).compareTo((Integer)rhs.getPosNumber());
	}	

}
