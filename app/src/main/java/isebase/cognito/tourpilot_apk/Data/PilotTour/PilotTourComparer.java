package isebase.cognito.tourpilot_apk.Data.PilotTour;

import java.util.Comparator;

public class PilotTourComparer implements Comparator<PilotTour> {

	@Override
	public int compare(PilotTour obj1, PilotTour obj2) {
		return obj1.getPlanDate().compareTo(obj2.getPlanDate());
	}
}
