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
import isebase.cognito.tourpilot.StaticResources.StaticResources;
import isebase.cognito.tourpilot.Views.BradenSkalaView;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class BradenSkalaPictureInfoActivity extends BaseActivity implements BaseDialogListener {
	
	BradenSkalaView bradenSkalaView;
	double startDistance;
	double currentDistance;
	
	boolean isSettingPoint;
	
	long startTime;
	
	Vibrator vibrator;
	
	BaseDialog leavingDialog;
	
	Answer bradenSkalaPointAnswer;
	Employment employment;
	
	int patientID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_braden_skala_picture_info);
		initControls();
		reloadData();
		setTitle();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.braden_skala_picture_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_undo:
			if (bradenSkalaView.points.size() > 0 && !employment.isDone()) {
				bradenSkalaView.points.remove(bradenSkalaView.points.size() - 1);
				bradenSkalaView.invalidate();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (!employment.isDone())
			leavingDialog.show(getSupportFragmentManager(), "leavingDialog");
		else
			super.onBackPressed();
	}
	
    private void setTitle() {
    	setTitle("dekubitusgefährdete Stellen");
    }
	
	private void initControls() {
		leavingDialog = new BaseDialog("Schließen", "Möchten sie die letzten Änderungen abspeichern?");
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		bradenSkalaView = (BradenSkalaView) findViewById(R.id.bsView);
		bradenSkalaView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1) {
					if (bradenSkalaView.downPoint.x + bradenSkalaView.pointRadius * 3 < event.getX() || bradenSkalaView.downPoint.x - bradenSkalaView.pointRadius * 3 > event.getX() || bradenSkalaView.downPoint.y + bradenSkalaView.pointRadius * 3 < event.getY() || bradenSkalaView.downPoint.y - bradenSkalaView.pointRadius * 3 > event.getY()) {
						startTime = 0;
					}
					bradenSkalaView.movingPoint.x = isOutFromLeft(event) ? 0 : isOutFromRight(event) ? getRightEdge() : (int)event.getX();
					bradenSkalaView.movingPoint.y = isOutFromTop(event) ? 0 : isOutFromBottom(event) ? getBottomEdge() : (int)event.getY();
					bradenSkalaView.x = bradenSkalaView.movingPoint.x - bradenSkalaView.downPoint.x + bradenSkalaView.startImagePoint.x;
					bradenSkalaView.y = bradenSkalaView.movingPoint.y - bradenSkalaView.downPoint.y + bradenSkalaView.startImagePoint.y;
					if (isOutFromLeft(event))
						bradenSkalaView.x = 0;
					if (isOutFromTop(event))
						bradenSkalaView.y = 0;
					if (isOutFromRight(event))
						bradenSkalaView.x = getRightEdge();
					if (isOutFromBottom(event))
						bradenSkalaView.y = getBottomEdge();
			    	bradenSkalaView.invalidate();
				}
			    if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    	bradenSkalaView.startImagePoint.set(bradenSkalaView
			    			.startImageX, bradenSkalaView.startImageY);
			    	bradenSkalaView.downPoint.set((int)event.getX(), (int)event.getY());

			    	startTime = System.nanoTime();
			    	gestureDetector.onTouchEvent(event);
			    }
			    if (event.getAction() == MotionEvent.ACTION_UP) {
					bradenSkalaView.startImageX = isOutFromLeft(event) ? 0 : isOutFromRight(event) ? getRightEdge() : getStartImageX(event);
					bradenSkalaView.startImageY = isOutFromTop(event) ? 0 : isOutFromBottom(event) ? getBottomEdge() : getStartImageY(event);
			    }
				return true;
			}
		});
	}
	
	private void reloadData() {
		employment = HelperFactory.getHelper().getEmploymentDAO().load((int)Option.Instance().getEmploymentID());
		patientID = employment.getPatientID();
		bradenSkalaPointAnswer = HelperFactory.getHelper().getAnswerDAO().loadByQuestionIDAndType(6, Category.type.braden.ordinal());
		if (bradenSkalaPointAnswer == null)
			return;
		for (String point : bradenSkalaPointAnswer.getAnswerKey().split(":")) {
			if (point.equals(""))
				continue;
			bradenSkalaView.points.add(new Point(Integer.parseInt(point.split(",")[0]), Integer.parseInt(point.split(",")[1])));
		}
		bradenSkalaView.invalidate();
	}
	
	private int getStartImageX(MotionEvent event) {
		return (int)event.getX() - (bradenSkalaView.downPoint.x - bradenSkalaView.startImagePoint.x);
	}
	
	private int getStartImageY(MotionEvent event) {
		return (int)event.getY() - (bradenSkalaView.downPoint.y - bradenSkalaView.startImagePoint.y);
	}
	
	private int getRightEdge() {
		return StaticResources.width * -1;
	}
	
	private int getBottomEdge() {
		return (bradenSkalaView.getResizedImageHeight() - bradenSkalaView.getHeight()) * -1;
	}
	
	private boolean isOutFromLeft(MotionEvent event) {
		return getStartImageX(event) >= 0;
	}
	
	private boolean isOutFromTop(MotionEvent event) {
		return getStartImageY(event) >= 0;
	}
	
	private boolean isOutFromRight(MotionEvent event) {
		return getStartImageX(event) < getRightEdge();
	}
	
	private boolean isOutFromBottom(MotionEvent event) {
		return getStartImageY(event) < getBottomEdge();
	}
	
	private int getXForPoint(float x) {
		return (int)x + bradenSkalaView.startImageX * -1;
	}
	
	private int getYForPoint(float y) {
		return (int)y + bradenSkalaView.startImageY * -1;
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		if (dialog == leavingDialog) {
			if (bradenSkalaPointAnswer == null) {
				bradenSkalaPointAnswer = new Answer(patientID, 6, "", 0, HelperFactory.getHelper().getCategoryDAO().loadByCategoryName(getString(R.string.braden)).getId(), "", Category.type.braden.ordinal());
				bradenSkalaPointAnswer.setAddInfo(String.valueOf(bradenSkalaView.standartMatchIndex));
			}
			String points = "";
			for (Point point : bradenSkalaView.points) {
				points += (points.equals("") ? "" : ":") + String.format("%d,%d", (int)(point.x), (int)(point.y));
			}
			bradenSkalaPointAnswer.setAnswerKey(points);
			HelperFactory.getHelper().getAnswerDAO().save(bradenSkalaPointAnswer);
			super.onBackPressed();
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		if (dialog == leavingDialog) {
			super.onBackPressed();
		}
	}
	
	final GestureDetector gestureDetector = new GestureDetector(getBaseContext(), new GestureDetector.SimpleOnGestureListener() {
	    public void onLongPress(MotionEvent event) {
			if (startTime != 0 && !employment.isDone()) {
				vibrator.vibrate(200);
				bradenSkalaView.points.add(new Point(getXForPoint(bradenSkalaView.downPoint.x), getYForPoint(bradenSkalaView.downPoint.y)));
				bradenSkalaView.invalidate();
			}
	    }
	});
	
}
