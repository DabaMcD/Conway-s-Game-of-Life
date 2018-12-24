package com.conwaysgame.oflife.conwaysgameoflife;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    private Thread mainThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setScreenDims();

        gameView = findViewById(R.id.gameView);
        gameView.constructor();
        Touch.setTouchListener(gameView, gameView);

        createAndStartMainThread();
    }
    private void createAndStartMainThread() {
        mainThread = new Thread() {
            public void run() {
                while (mainThread.isAlive()) {
                    long prevMillis = System.currentTimeMillis();
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gameView.draw();
                            }
                        });
                        float waitTime = prevMillis - System.currentTimeMillis() + gameView.speed;
                        if(waitTime > 0) {
                            Thread.sleep((long) waitTime);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mainThread.start();
    }
    private void setScreenDims() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Screen.width = size.x;
        Screen.height = size.y - getStatusBarHeight();
    }
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
