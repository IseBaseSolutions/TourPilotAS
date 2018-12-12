package isebase.cognito.tourpilot_apk.Views;

import android.graphics.Point;

public class KontrakturPoint extends Point {
	
	int state;
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int index;
	
	public int getIndex() {
		return index;
	}

	public KontrakturPoint(int index, int x, int y) {
		set(x, y);
		this.index = index;
	}
	
	public KontrakturPoint( int index, int state, int x, int y) {
		set(x, y);
		this.state = state;
		this.index = index;
	}

}
