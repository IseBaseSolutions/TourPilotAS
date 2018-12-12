package isebase.cognito.tourpilot_apk.Views;

import isebase.cognito.tourpilot_apk.Data.Employment.Employment;
import isebase.cognito.tourpilot_apk.Data.Option.Option;
import isebase.cognito.tourpilot_apk.DataBase.HelperFactory;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BradenSkalaView extends View {

	int standartImageWidth = 496;
	int standartImageHeight = 401;

	public Bitmap image;
	Matrix matrix;
	Paint paint;
	Paint pointPaint;
	public float matchIndex;
	public float standartMatchIndex;
	public int zoomIndex = 2;

	public int startImageX;
	public int startImageY;

	public Point movingPoint;
	public Point startImagePoint;
	public Point downPoint;

	public List<Point> points = new ArrayList<Point>();

	public int x;
	public int y;

	public int pointRadius;

	public BradenSkalaView(Context context) {
		super(context);
		initComponents();
		setZoom();
	}

	public BradenSkalaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initComponents();
		setZoom();
	}

	public BradenSkalaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initComponents();
		setZoom();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect r1 = new Rect(0, 0, image.getWidth(), image.getHeight());
		Rect r2 = new Rect(x, y, x + getResizedImageWidth(), y
				+ getResizedImageHeight());
		canvas.drawBitmap(image, r1, r2, paint);
		for (Point point : points)
			if (point != null && point.x != 0 && point.y != 0)
				canvas.drawCircle(point.x + x, point.y + y, pointRadius,
						pointPaint);
	}

	private void initComponents() {
		Employment employment = HelperFactory.getHelper().getEmploymentDAO()
				.loadAll((int) Option.Instance().getEmploymentID());
		image = BitmapFactory
				.decodeResource(
						getResources(),
						employment.getPatient().getSex().equals("Herr") ? isebase.cognito.tourpilot_apk.R.drawable.braden_man
								: isebase.cognito.tourpilot_apk.R.drawable.braden_woman);
		matrix = new Matrix();
		movingPoint = new Point(0, 0);
		startImagePoint = new Point(0, 0);
		downPoint = new Point(0, 0);
		paint = new Paint();
		pointPaint = new Paint();
		pointPaint.setColor(Color.RED);
		pointPaint.setAlpha(200);
		pointPaint.setAntiAlias(true);
		pointRadius = StaticResources.width / 100 * 2 * zoomIndex;
	}

	private void setZoom() {
		double a = (double) image.getWidth() / 100.00;
		double b = StaticResources.width / a;
		matchIndex = (float) (b / 100.00);
		b = StaticResources.width / a;
		standartMatchIndex = (float) standartImageWidth
				/ (StaticResources.width * zoomIndex);
	}

	public int getResizedImageWidth() {
		return (int) (image.getWidth() * matchIndex) * zoomIndex;
	}

	public int getResizedImageHeight() {
		return (int) (image.getHeight() * matchIndex) * zoomIndex;
	}

}
