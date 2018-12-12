package isebase.cognito.tourpilot_apk.Data.BaseObject;


import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class BaseObjectComparer implements Comparator<BaseObject> {

	@Override
	public int compare(BaseObject lhs, BaseObject rhs) {
		
		Collator deCollator = Collator.getInstance(Locale.GERMANY);
		// TODO Auto-generated method stub
		return deCollator.compare(lhs.getName(), rhs.getName());
	}
	
}
