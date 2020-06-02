package com.example.balance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements SensorEventListener {
    private static final String TAG = "AnimationStarter";

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_layout);

        Button bounceBallButton = (Button) findViewById(R.id.bounceBallButton);
        final ImageView bounceBallImage =(ImageView) findViewById(R.id.bounceBallImage);

        bounceBallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bounceBallImage.clearAnimation();
                TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0, getDisplayHeight() / 2);
                transAnim.setStartOffset(500);
                transAnim.setDuration(3000);
                transAnim.setFillAfter(true);
                transAnim.setInterpolator(new BounceInterpolator());
                transAnim.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.i(TAG, "Starting button dropdown animation");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.i(TAG, "Ending button dropdown animation. Clearing animation and setting layout");
                        bounceBallImage.clearAnimation();
                        final int left = bounceBallImage.getLeft();
                        final int top = bounceBallImage.getTop();
                        final int right = bounceBallImage.getRight();
                        final int bottom = bounceBallImage.getBottom();
                        bounceBallImage.layout(left, top, right, bottom);
                    }
                });
                bounceBallImage.startAnimation(transAnim);
            }
        });
    }

    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
      }*/


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    private AnimatedView mAnimatedView = null;
    public static AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAnimatedView = new AnimatedView(this);

        setMenuLayout();

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("GAME OVER");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setMenuLayout();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mAnimatedView.reset();

//                        mAnimatedView.resume();
                        setContentView(mAnimatedView);
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAnimatedView.onSensorEvent(event);
        }
    }
    public void setMenuLayout(){
        mAnimatedView = new AnimatedView(this);
        setContentView(R.layout.menu_layout);

        Button playButton = (Button) findViewById(R.id.playButton);
        Button exitButton = (Button) findViewById(R.id.exitButton);
        ImageView image = (ImageView) findViewById(R.id.imageView3);
        image.setImageResource(R.drawable.logo);
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimatedView.start();
                setContentView(mAnimatedView);
            }
        });
        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }

}
