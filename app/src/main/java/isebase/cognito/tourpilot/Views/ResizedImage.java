package isebase.cognito.tourpilot.Views;

import isebase.cognito.tourpilot.StaticResources.StaticResources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class ResizedImage {

	Rect defaultRect;
	Rect resizedRect;

	double defaultMatchIndex;
	double matchIndex;

	int matchWidth;

	public int getMatchWidth(int zoomIndex) {
		return matchWidth;
	}

	int matchHeight;

	public int getMatchHeight() {
		return matchHeight;
	}

	Bitmap image;

	Rect r1;
	Rect r2;

	List<KontrakturPoint> activePoints = new ArrayList<KontrakturPoint>();

	public void setActivePoints(List<KontrakturPoint> activePoints) {
		this.activePoints = activePoints;
	}

	public HashMap<String, Point> textList = new HashMap<String, Point>();

	public ResizedImage(Resources resources, int imageId, int left, int top) {
		image = BitmapFactory.decodeResource(resources, imageId);
		final Options dimensions = new BitmapFactory.Options();
		dimensions.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, imageId, dimensions);

		defaultMatchIndex = (double) StaticResources.width
				/ dimensions.outWidth;
		matchIndex = (double) StaticResources.width / image.getWidth();
		matchWidth = StaticResources.width;
		matchHeight = (int) (image.getHeight() * matchIndex);
		r2 = new Rect(left, top, left + matchWidth, top + matchHeight);
	}

	public void draw(Canvas canvas, int zoomIndex, Paint paint) {
		canvas.drawBitmap(image, r1, new Rect(r2.left * zoomIndex, r2.top
				* zoomIndex, r2.right * zoomIndex, r2.bottom * zoomIndex),
				paint);
		for (KontrakturPoint activePoint : activePoints) {
			paint.setColor(StaticResources
					.getBaseContext()
					.getResources()
					.getColor(
							activePoint.state == 0 ? isebase.cognito.tourpilot.R.color.green
									: activePoint.state == 1 ? isebase.cognito.tourpilot.R.color.blue
											: isebase.cognito.tourpilot.R.color.red));
			canvas.drawCircle(getResizedX(activePoint.x, zoomIndex),
					getResizedY(activePoint.y, zoomIndex),
					getRadius(zoomIndex), paint);
		}

		for (Entry<String, Point> text : textList.entrySet()) {
			paint.setColor(Color.BLACK);
			paint.setTextSize(getRadius(zoomIndex) * 2);
			canvas.drawText(text.getKey(),
					getResizedX(text.getValue().x, zoomIndex),
					getResizedY(text.getValue().y, zoomIndex), paint);
		}

	}

	public KontrakturPoint getPoint(int x, int y, int zoomIndex) {
		for (KontrakturPoint point : activePoints) {
			int x1 = getResizedX(point.x, zoomIndex);
			int y1 = getResizedY(point.y, zoomIndex);
			int r = getRadius(zoomIndex);
			if (x1 - r < x && x1 + r > x
					&& y1 - r < y && y1 + r > y) {
				point.state = point.state == 0 ? 1 : point.state == 1 ? 2 : 0;
				return point;
			}
		}
		return null;
	}

	private int getResizedX(int x, int zoomIndex) {
		return ((int) (r2.left + x * defaultMatchIndex)) * zoomIndex;
	}

	private int getResizedY(int y, int zoomIndex) {
		return ((int) (r2.top + y * defaultMatchIndex)) * zoomIndex;
	}

	private int getRadius(int zoomIndex) {
		return ((int) (5 * defaultMatchIndex)) * zoomIndex;
	}

}
