package isebase.cognito.tourpilot.Activity.QuestionActivities;

import isebase.cognito.tourpilot.R;
import isebase.cognito.tourpilot.Activity.BaseActivities.BaseActivity;
import isebase.cognito.tourpilot.Data.Answer.Answer;
import isebase.cognito.tourpilot.Data.Category.Category;
import isebase.cognito.tourpilot.Data.Employment.Employment;
import isebase.cognito.tourpilot.Data.Option.Option;
import isebase.cognito.tourpilot.DataBase.HelperFactory;
import isebase.cognito.tourpilot.Dialogs.BaseDialog;
import isebase.cognito.tourpilot.Dialogs.BaseDialogListener;
import isebase.cognito.tourpilot.Views.KontrakturPoint;
import isebase.cognito.tourpilot.Views.KontrakturView;

import java.util.Hashtable;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;

public class KontrakturPictureInfoActivity extends BaseActivity implements BaseDialogListener {

	KontrakturView view;
	HorizontalScrollView horizontalScrollView;
	Vibrator vibrator;
	long startTime;
	long stopTime;
	
	Hashtable<Integer, KontrakturPoint> changedPoints = new Hashtable<Integer, KontrakturPoint>();
	
	BaseDialog leavingDialog;
	Answer answer;
	Employment employment;
	int patientID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kontraktur_picture_info);
		initComponents();
		reloadData();
		setTitle();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	public void onBackPressed() {
		if (!employment.isDone())
			leavingDialog.show(getSupportFragmentManager(), "leavingDialog");
		else
			super.onBackPressed();
	}
	
    private void setTitle() {
    	setTitle("Lokalisation");
    }
	
	private void initComponents() {
		view = (KontrakturView) findViewById(R.id.kontraktur_view);
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scroll_view);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!employment.isDone())
					gestureDetector.onTouchEvent(event);
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (isDoubleClick())
						view.changeZoomIndex(horizontalScrollView);
					startTime = System.nanoTime();
				}
				
				return true;
			}
		});
		leavingDialog = new BaseDialog("Schließen", "Möchten sie die letzten Änderungen abspeichern?");
	}
	
	private boolean isDoubleClick() {
		return System.nanoTime() - startTime < 170000000;
	}
	
	final GestureDetector gestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
	    public void onLongPress(MotionEvent event) {
	    	KontrakturPoint point = view.getPoint((int)event.getX(), (int)event.getY());
	    	if (point != null) {
	    		if (!changedPoints.containsKey(point.getIndex()))
	    			changedPoints.put(point.getIndex(), point);
	    		else if (changedPoints.containsKey(point.getIndex()) && changedPoints.get(point.getIndex()).getState() == 0)
	    			changedPoints.remove(point.getIndex());
	    		else
	    			changedPoints.get(point.getIndex()).setState(point.getState());
	    		vibrator.vibrate(200);
	    		view.invalidate();
	    	}
	    }
	});
	
	private void reloadData() {
		employment = HelperFactory.getHelper().getEmploymentDAO().load((int)Option.Instance().getEmploymentID());
		patientID = employment.getPatientID();
		answer = HelperFactory.getHelper().getAnswerDAO().loadKontrakturenAnswer();
		if (answer == null)
			return;
		view.setPoints(answer.getAnswerKey());
		if (answer.getAnswerKey().equals(""))
			return;
		String[] points = answer.getAnswerKey().split(":");
		for (int i = 0; i < points.length; i++) {			
			changedPoints.put(Integer.parseInt(points[i].split(",")[0]), new KontrakturPoint(Integer.parseInt(points[i].split(",")[0]), Integer.parseInt(points[i].split(",")[1]), -1, -1));
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog == leavingDialog) {
			if (answer == null) {
				answer = new Answer(patientID, -1, "", 0, HelperFactory.getHelper().getCategoryDAO().loadByCategoryName("Kontrakturrisikoerfassung").getId(), "", Category.type.normal.ordinal());
			}
			String points = "";
			for (KontrakturPoint changedPoint : changedPoints.values()) {
				points += (points.equals("") ? "" : ":") + String.format("%d,%d", changedPoint.getIndex(), changedPoint.getState());
			}
			answer.setAnswerKey(points);
			HelperFactory.getHelper().getAnswerDAO().save(answer);
			super.onBackPressed();
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if (dialog == leavingDialog) {
			super.onBackPressed();
		}
	}

}
