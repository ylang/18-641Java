


package com.ece.smartGallery.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


/*
 * This file is mostly from web:
 * http://stackoverflow.com/questions/7401432/drawing-on-canvas-and-save-image
*/

public class DrawView extends View implements OnTouchListener {
    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 2, paint);  
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        points.add(point);
        invalidate();
        return true;
    }
    
    public void clear(){
        points.clear();
        invalidate();
        System.gc();
    }
}

class Point {
    float x, y;
}