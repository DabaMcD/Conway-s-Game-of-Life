package com.conwaysgame.oflife.conwaysgameoflife;

import android.view.MotionEvent;
import android.view.View;

class Touch {
    static void setTouchListener(View view, final GameView gameView) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.actionUp(event.getX(), event.getY());
                        break;
                }
                return true;
            }
        });
    }
}
