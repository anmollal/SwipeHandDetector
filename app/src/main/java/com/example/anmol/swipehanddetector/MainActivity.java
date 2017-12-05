package com.example.anmol.swipehanddetector;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Display;
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
        String text;
        float d = xe - xs;
        if (d == 0.0f) {
            text = "Can't detect";
        } else {
            float s = (ye - ys) / d;
            d = direction(d) * xe;
            if (d < 0 || s < lowerSlopeThreshold) {
                text = "Left Thumb";
            } else if (s > higherSlopeThreshold) {
                text = "Right Thumb";
            } else if (s >= 0) {
                if (d < width * lowerDirectionThresholdPercent) {
                    text = "Left Thumb";
                } else {
                    text = "Right Thumb";
                }
            } else {
                if (d > width * higherDirectionThresholdPercent) {
                    text = "Right Thumb";
                } else {
                    text = "Left Thumb";
                }
            }
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private float direction(float num) {
        if (num > 0) {
            return 1.0f;
        }
        return -1.0f;
    }
}
