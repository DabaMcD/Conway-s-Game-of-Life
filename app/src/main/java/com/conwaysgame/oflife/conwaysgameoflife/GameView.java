package com.conwaysgame.oflife.conwaysgameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
    // For now, this game is only screen wide and high. I may change that later.

    public GameView(Context context) {
        super(context);
    }
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    void constructor() {

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    void draw() {
        invalidate();
        requestLayout();
    }
}
