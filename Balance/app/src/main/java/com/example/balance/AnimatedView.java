package com.example.balance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AnimatedView extends View {
    public boolean allow=true;

    public int secondsPassed = 0;
    public int score = 0;
    public int highScore = 0;
    Timer myTimer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            secondsPassed++;
        }
    };

    public void start() {
        myTimer.schedule(task, 1000, 1000);
    }

    public void pause() {
        myTimer.cancel();
        myTimer.purge();
    }
    public void resume() {
        myTimer = new Timer();
        myTimer.schedule( task, 0, 1000 );
    }
    public void reset() {
        speedMultiplier = 10;
        gravityForce = 40;
        xBall = getDisplayWidth() / 2;
        yBall=0;
        plateSize = 450;
        xPlate =  r.nextInt((int) (getDisplayWidth()-plateSize));
        yPlate = getDisplayHeight() / 2;
        plateStatus = true;
        score=0;
        secondsPassed=0;
        allow=true;
        vector=0;
    }


    private static final int CIRCLE_RADIUS = 100; //pixels
    Random r = new Random();
    private int speedMultiplier = 10;
    private int gravityForce = 40;

    private float vector=0;

    private boolean felt=false;

    private Paint mPaint;
    private Paint mPaint2;
    private Paint mPaint3;
    private float xBall = getDisplayWidth() / 2;
    private float yBall;
    private float plateSize = 450;
    private float xPlate = r.nextInt((int) (getDisplayWidth()-plateSize));
    private float yPlate = getDisplayHeight() / 2;
    boolean plateStatus = true;

    private int viewWidth = 999999;
    private int viewHeight = 999999;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.i("MainActivity", "Back button pressed, exiting..");

        }
        return super.onKeyDown(keyCode, event);
    }

    public AnimatedView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLACK);
        mPaint3 = new Paint();
        mPaint3.setColor(Color.BLACK);
        mPaint3.setTextSize(100);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(xBall, yBall, CIRCLE_RADIUS, mPaint);
        canvas.drawRect(xPlate, yPlate, xPlate + plateSize, yPlate + 50, mPaint2);
        if(secondsPassed>3)canvas.drawText("Score: "+ (secondsPassed*3-3*3),100,100,mPaint3);
        else canvas.drawText("Score: "+0,100,100,mPaint3);

        invalidate();
    }


    public void onSensorEvent(SensorEvent event) {
        boolean ballOnPlate = (xBall + (2 * CIRCLE_RADIUS / 3) >= xPlate && xBall - (2 * CIRCLE_RADIUS / 3) <= xPlate + plateSize && yBall + CIRCLE_RADIUS >= yPlate && yBall - CIRCLE_RADIUS <= yPlate + 50);
        if(!allow)secondsPassed=0;

        if(secondsPassed>10) {
            if (plateStatus) {
                xPlate = (float) (xPlate + 0.5*secondsPassed);
            } else {
                xPlate -= 0.5*secondsPassed;
            }
            if (xPlate + plateSize >= getDisplayWidth()) plateStatus = false;
            if (xPlate <= 0) plateStatus = true;
        }
        if(secondsPassed>10&&secondsPassed%11==0){
            vector= r.nextInt(20)+1;

        }


        if (secondsPassed <= 1) {
            xBall = getDisplayWidth() / 2;
            yBall = 0;
        } else
            xBall = xBall - (event.values[0] * speedMultiplier)-vector;
        yBall = yBall + gravityForce;


        if (xBall <= 0 + CIRCLE_RADIUS) {
            xBall = 0 + CIRCLE_RADIUS;
        }

        if (xBall >= viewWidth - CIRCLE_RADIUS) {
            xBall = viewWidth - CIRCLE_RADIUS;
        }
        if (yBall <= 0 + CIRCLE_RADIUS) {
            yBall = 0 + CIRCLE_RADIUS;
        }
        if (ballOnPlate) {
            yBall = yPlate - CIRCLE_RADIUS;
        }
        if (yBall >= viewHeight - CIRCLE_RADIUS) {
            yBall = viewHeight - CIRCLE_RADIUS;

            score = secondsPassed * 3 - 3 * 3;
            if (score >= highScore) {
                highScore = score;
            }

            MainActivity.alertDialog.setMessage("Twój wynik to: " + score + "\nTwój najwyższy wynik to: " + highScore);
            MainActivity.alertDialog.show();
            secondsPassed=0;
            allow=false;
        }
    }

    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }

    private int getDisplayWidth() {
        return this.getResources().getDisplayMetrics().widthPixels;
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }

}