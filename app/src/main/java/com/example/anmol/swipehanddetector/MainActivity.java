package com.example.anmol.swipehanddetector;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    float xs;
    float xe;
    float ys;
    float ye;
    float height;
    float width;
    float lowerSlopeThreshold = -0.03312292358803986f;
    float higherSlopeThreshold = 0.233201581027668f;
    float lowerDirectionThresholdPercent = 0.65f;
    float higherDirectionThresholdPercent = 0.72f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = (float) size.y;
        width = (float) size.x;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            xs = ev.getX(0);
            ys = height - ev.getY(0);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            xe = ev.getX(0);
            ye = height - ev.getY(0);
            detectHand();
        }
        return true;
    }

    private void detectHand() {
        String text = "Index";
        float d = xe - xs;
        if (d == 0.0f) {
            text = "Index";
        } else {
            float s = (ye - ys) / d;
            double l = Math.sqrt(Math.pow(d, 2) - Math.pow(ye - ys, 2));
            d = direction(d) * xe;

            if (d < 0 || s < lowerSlopeThreshold) {
                text = "Left Thumb";
            } else if (s > higherSlopeThreshold) {
                text = "Right Thumb";
            } else if (s > 0) {
                if (l > 750) {
                    text = "Index";
                }
                else if (d < width * lowerDirectionThresholdPercent) {
                    text = "Left Thumb";
                } else {
                    text = "Right Thumb";
                }
            } else if (s <= 0) {
                if (s > -.1) {
                    if (l > 700) {
                        text = "Index";
                    } else {
                        if (d > width * higherDirectionThresholdPercent) {
                            text = "Right Thumb";
                        } else {
                            text = "Left Thumb";
                        }
                    }
                } else {
                    text = "Left Thumb";
                }
            }
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        showNextScreen(text);
    }

    private float direction(float num) {
        if (num > 0) {
            return 1.0f;
        }
        return -1.0f;
    }

    private void showNextScreen(String screen) {
        if (screen.equals("Right Hand")) {
            startActivity(new Intent(MainActivity.this, RightHandActivity.class));
        } else if (screen.equals("Left Hand")) {
            startActivity(new Intent(MainActivity.this, LeftHandActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, TwoHandActivity.class));
        }
    }
}
