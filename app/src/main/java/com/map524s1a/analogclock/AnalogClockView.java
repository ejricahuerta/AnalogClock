package com.map524s1a.analogclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.Calendar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ejricahuerta on 4/18/2018.
 */

public class AnalogClockView extends View {


    private int[] v_clockhr = {12,11,10,9,8,7,6,5,4,3,2,1};

    private int v_height, v_width = 0;


    private float v_padding = 0;

    private int v_numspacing = 0;

    private int v_radius = 0;

    private int v_hand,v_hrhand = 0;

    private Paint v_Paint;

    private Rect v_Rect;


    private boolean v_IsInitialized;

    private  Context v_context;

    private Canvas canvas;

    public AnalogClockView(Context context) {
        super(context);
    }

    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        v_context = context;
    }

    public AnalogClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        v_context = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(!v_IsInitialized){
            Initialize();
        }
        canvas.drawColor(getResources().getColor(R.color.bg_color,v_context.getTheme()));

        drawCircles(canvas);
        drawNumbers(canvas);

        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;

        drawHands(canvas,(hour+ calendar.get(Calendar.MINUTE) /60 ) *5f, true,false);
        drawHands(canvas,calendar.get(Calendar.MINUTE), false,false);
        drawHands(canvas,calendar.get(Calendar.SECOND ), false,true);

        postInvalidateDelayed(500);
        invalidate();

    }

    private void Initialize(){

        v_Rect = new Rect();
        v_Paint = new Paint();
        v_height = (int)(getPivotY()  + getY());
        v_width = (int)(getPivotX() + getX());
        float mid = (v_width + v_height) /2;
        v_padding = v_numspacing + mid ;
        int c_min = Math.min(v_height,v_width);
        v_radius = c_min/ 2;
        v_hand = c_min / 20;
        v_hrhand = c_min /10;
        v_IsInitialized = true;

    }


    private void drawCircles(Canvas canvas){
        v_Paint.reset();
        v_Paint.setColor(getResources().getColor(R.color.clock_color,v_context.getTheme()));
        v_Paint.setStyle(Paint.Style.STROKE);
        v_Paint.setStrokeWidth(10);
        v_Paint.setAntiAlias(true);


        canvas.drawCircle(v_width,v_height,(float)v_radius,v_Paint);
        v_Paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(v_width,v_height,30,v_Paint);

    }
    private  void drawNumbers(Canvas canvas) {
        int fontSize = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        14,
                        getResources().getDisplayMetrics()
                );
        v_Paint.setTextSize(fontSize);
        int r = (int)v_radius + -50;
        for (int hour : v_clockhr)
        {
            String temp = String.valueOf(hour);

            v_Paint.getTextBounds(temp, 0, temp.length(), v_Rect);

            double angle = Math.PI / 6 * (hour - 3);
            int x = (int) (v_width + Math.cos(angle) * r - v_Rect.width() / 2);
            int y = (int) (v_height + Math.sin(angle) * r + v_Rect.height() / 2);

            canvas.drawText(temp, x, y, v_Paint);
        }

    }
    private void drawHands(Canvas canvas, double current, boolean isHr, boolean isSec){
        double angle = Math.PI * current / 30 - Math.PI / 2;
        int handRadius = isHr ? v_radius - v_hand - v_hrhand : v_radius - v_hand;
        if (isSec) v_Paint.setColor(getResources().getColor(R.color.clock_min_hand,v_context.getTheme()));
        canvas.drawLine(v_width, v_height,
                (float) (v_width  + Math.cos(angle) * handRadius),
                (float) (v_height + Math.sin(angle) * handRadius),
                v_Paint);
    }
}


