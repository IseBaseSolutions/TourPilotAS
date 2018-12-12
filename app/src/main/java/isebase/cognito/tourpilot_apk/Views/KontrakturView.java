package isebase.cognito.tourpilot_apk.Views;

import isebase.cognito.tourpilot_apk.R;
import isebase.cognito.tourpilot_apk.StaticResources.StaticResources;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;

public class KontrakturView extends View {

	ResizedImage bodyImage;
	ResizedImage handsImage;
	ResizedImage feetImage;

	ResizedImage activeImage;

	List<ResizedImage> images = new ArrayList<ResizedImage>();

	Paint paint;
	
	int legendSize;
	
	int zoomIndex = 1;

	public KontrakturView(Context context) {
		super(context);
		initComponents();
	}

	public KontrakturView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initComponents();
	}

	public KontrakturView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initComponents();
	}

	private void initComponents() {
		paint = new Paint();
		legendSize = (int)((double)StaticResources.height / 4.44);
		bodyImage = new ResizedImage(getResources(),
				isebase.cognito.tourpilot_apk.R.drawable.body, 0, legendSize);
		images.add(bodyImage);		
		handsImage = new ResizedImage(getResources(),
				isebase.cognito.tourpilot_apk.R.drawable.arms, 0, legendSize + bodyImage.getMatchHeight());
		images.add(handsImage);
		feetImage = new ResizedImage(getResources(),
				isebase.cognito.tourpilot_apk.R.drawable.feet, 0, legendSize + bodyImage.getMatchHeight() + handsImage.getMatchHeight());
		images.add(feetImage);
		initActivePoints();
	}

	private void initActivePoints() {
		List<KontrakturPoint> bodyActivePoints = new ArrayList<KontrakturPoint>();
		bodyActivePoints.add(new KontrakturPoint(1, 86, 66));
		bodyActivePoints.add(new KontrakturPoint(2, 48, 82));
		bodyActivePoints.add(new KontrakturPoint(3, 122, 82));
		bodyActivePoints.add(new KontrakturPoint(4, 40, 142));		
		bodyActivePoints.add(new KontrakturPoint(5, 132, 142));		
		bodyActivePoints.add(new KontrakturPoint(6, 58, 196));		
		bodyActivePoints.add(new KontrakturPoint(7, 114, 196));		
		bodyActivePoints.add(new KontrakturPoint(8, 66, 296));		
		bodyActivePoints.add(new KontrakturPoint(9, 106, 296));		
		bodyActivePoints.add(new KontrakturPoint(10, 72, 366));
		bodyActivePoints.add(new KontrakturPoint(11, 100, 366));
		
		bodyImage.setActivePoints(bodyActivePoints);
		
		bodyImage.textList.put("Halswirbel", new Point(202, 64));
		bodyImage.textList.put("Schultergelenke", new Point(187, 93));		
		bodyImage.textList.put("Ellenbogen", new Point(208, 131));
		bodyImage.textList.put("Huftgelenke", new Point(198, 226));
		bodyImage.textList.put("Kniegelenke", new Point(196, 277));
		bodyImage.textList.put("Sprunggelenke", new Point(185, 343));		

		List<KontrakturPoint> armsActivePoints = new ArrayList<KontrakturPoint>();
		armsActivePoints.add(new KontrakturPoint(12, 56, 164));
		armsActivePoints.add(new KontrakturPoint(13, 156, 164));
		armsActivePoints.add(new KontrakturPoint(18, 22, 68));
		armsActivePoints.add(new KontrakturPoint(19, 26, 78));
		armsActivePoints.add(new KontrakturPoint(20, 30, 90));
		armsActivePoints.add(new KontrakturPoint(21, 38, 56));
		armsActivePoints.add(new KontrakturPoint(22, 42, 66));
		armsActivePoints.add(new KontrakturPoint(23, 44, 78));
		armsActivePoints.add(new KontrakturPoint(24, 56, 44));
		armsActivePoints.add(new KontrakturPoint(25, 60, 56));
		armsActivePoints.add(new KontrakturPoint(26, 60, 70));
		armsActivePoints.add(new KontrakturPoint(27, 80, 50));
		armsActivePoints.add(new KontrakturPoint(28, 78, 62));
		armsActivePoints.add(new KontrakturPoint(29, 76, 74));
		armsActivePoints.add(new KontrakturPoint(30, 92, 108));
		armsActivePoints.add(new KontrakturPoint(31, 96, 92));
		armsActivePoints.add(new KontrakturPoint(32, 116, 92));
		armsActivePoints.add(new KontrakturPoint(33, 122, 108));
		armsActivePoints.add(new KontrakturPoint(34, 134, 50));
		armsActivePoints.add(new KontrakturPoint(35, 138, 60));
		armsActivePoints.add(new KontrakturPoint(36, 138, 74));
		armsActivePoints.add(new KontrakturPoint(37, 158, 44));
		armsActivePoints.add(new KontrakturPoint(38, 156, 56));
		armsActivePoints.add(new KontrakturPoint(39, 156, 66));
		armsActivePoints.add(new KontrakturPoint(40, 178, 54));
		armsActivePoints.add(new KontrakturPoint(41, 174, 64));
		armsActivePoints.add(new KontrakturPoint(42, 170, 74));
		armsActivePoints.add(new KontrakturPoint(43, 192, 66));
		armsActivePoints.add(new KontrakturPoint(44, 188, 76));
		armsActivePoints.add(new KontrakturPoint(45, 186, 86));

		handsImage.setActivePoints(armsActivePoints);
		
		handsImage.textList.put("Handwurzegelenke", new Point(65, 206));		

		List<KontrakturPoint> feetActivePoints = new ArrayList<KontrakturPoint>();
		feetActivePoints.add(new KontrakturPoint(46, 34, 50));
		feetActivePoints.add(new KontrakturPoint(47, 34, 60));
		feetActivePoints.add(new KontrakturPoint(48, 42, 42));
		feetActivePoints.add(new KontrakturPoint(49, 42, 52));
		feetActivePoints.add(new KontrakturPoint(50, 52, 34));
		feetActivePoints.add(new KontrakturPoint(51, 52, 44));
		feetActivePoints.add(new KontrakturPoint(52, 64, 24));
		feetActivePoints.add(new KontrakturPoint(53, 64, 36));
		feetActivePoints.add(new KontrakturPoint(14, 78, 22));
		feetActivePoints.add(new KontrakturPoint(15, 78, 34));
		feetActivePoints.add(new KontrakturPoint(16, 132, 32));
		feetActivePoints.add(new KontrakturPoint(17, 132, 22));
		feetActivePoints.add(new KontrakturPoint(54, 146, 26));
		feetActivePoints.add(new KontrakturPoint(55, 146, 36));
		feetActivePoints.add(new KontrakturPoint(56, 158, 36));
		feetActivePoints.add(new KontrakturPoint(57, 158, 46));
		feetActivePoints.add(new KontrakturPoint(58, 168, 42));
		feetActivePoints.add(new KontrakturPoint(59, 168, 52));
		feetActivePoints.add(new KontrakturPoint(60, 176, 50));
		feetActivePoints.add(new KontrakturPoint(61, 176, 60));
		feetImage.setActivePoints(feetActivePoints);
		
		feetImage.textList.put("Zehen", new Point(95, 180));	
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		drawLegend(canvas);
		for (ResizedImage image : images)
			image.draw(canvas, zoomIndex, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = StaticResources.width * zoomIndex;
		int desiredHeight = 0;
		for (ResizedImage image : images)
			desiredHeight += image.getMatchHeight() * zoomIndex;
		desiredHeight += legendSize;
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(desiredWidth, widthSize);
		} else {
			width = desiredWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(desiredHeight, heightSize);
		} else {
			height = desiredHeight;
		}
		setMeasuredDimension(width, height);
	}

	public void setActiveImage(int index) {
		switch (index) {
		case 0:
			activeImage = bodyImage;
			break;
		case 1:
			activeImage = handsImage;
			break;
		case 2:
			activeImage = feetImage;
			break;
		}
	}
	
	public void changeZoomIndex(HorizontalScrollView horizontalScrollView) {
		zoomIndex = zoomIndex == 1 ? 2 : 1;
		int desiredHeight = 0;
		for (ResizedImage image : images)
			desiredHeight += image.getMatchHeight() * zoomIndex;
		desiredHeight += legendSize * zoomIndex;
		setMeasuredDimension(StaticResources.width * zoomIndex, desiredHeight);
		LayoutParams layoutParams = horizontalScrollView.getLayoutParams();    
		layoutParams.height = desiredHeight;
		horizontalScrollView.setLayoutParams(layoutParams);
		invalidate();
	}
	
	public KontrakturPoint getPoint(int x, int y){
		for (ResizedImage image : images) { 
			KontrakturPoint point = image.getPoint(x, y, zoomIndex);
				if (point != null) {
					return point;
				}
		}
		return null;
	}
	
	public void setPoints(String pointsStr) {
		if (pointsStr.equals(""))
			return;
		String[] points = pointsStr.split(":");
		List<KontrakturPoint> allPoints = new ArrayList<KontrakturPoint>();
		allPoints.addAll(bodyImage.activePoints);
		allPoints.addAll(handsImage.activePoints);
		allPoints.addAll(feetImage.activePoints);
		for (int i = 0; i < points.length; i++)
			for (KontrakturPoint point : allPoints) {
				if (point.getIndex() == Integer.parseInt(points[i].split(",")[0])) {
					point.setState(Integer.parseInt(points[i].split(",")[1]));
					break;
				}
		}
	}
	
	private void drawLegend(Canvas canvas) {
		paint.setColor(Color.BLACK);
		int textSize = legendSize/10*zoomIndex;
		paint.setTextSize(textSize);
		int radius = (int)(legendSize/10/1.33);
		canvas.drawText("Normale", radius*4*zoomIndex, legendSize/8*zoomIndex + radius*zoomIndex/2, paint);
		canvas.drawText("Gefährdet", radius*4*zoomIndex, (int)(legendSize/2.66)*zoomIndex + radius*zoomIndex/2, paint);
		canvas.drawText("Betroffen(Bewegung eingeschrönkt/Gelenk", radius*4*zoomIndex, (int)(legendSize/1.6)*zoomIndex + radius*zoomIndex/2, paint);
		canvas.drawText("unbeweglich)", radius*4*zoomIndex, (int)(legendSize/1.6)*zoomIndex + radius*2*zoomIndex, paint);
		
		paint.setColor(getResources().getColor(R.color.green));		
		canvas.drawCircle(radius*2*zoomIndex, legendSize/8*zoomIndex, radius*zoomIndex, paint);
		paint.setColor(getResources().getColor(R.color.blue));		
		canvas.drawCircle(radius*2*zoomIndex, (int)(legendSize/2.66)*zoomIndex, radius*zoomIndex, paint);
		paint.setColor(getResources().getColor(R.color.red));		
		canvas.drawCircle(radius*2*zoomIndex, (int)(legendSize/1.6)*zoomIndex, radius*zoomIndex, paint);
	}

}
