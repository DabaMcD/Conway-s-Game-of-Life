package com.conwaysgame.oflife.conwaysgameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {
    // From wikipedia:
    // Rule 1. Any live cell with fewer than two live neighbors dies, as if by underpopulation.
    // Rule 2. Any live cell with two or three live neighbors lives on to the next generation. This one is done automatically, by default
    // Rule 3. Any live cell with more than three live neighbors dies, as if by overpopulation.
    // Rule 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

    // For now, this game is only screen wide and high. I may change that later.
    // Also for now, every cell is 20px by 20px
    private final int cellSize = 20;
    private ArrayList<ArrayList<Cell>> cells;
    private Paint paint;
    private boolean generating = false;
    float speed;
    double fps;
    int buttonSize;

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
        cells = new ArrayList<>();
        for(int x = 0; x < Screen.width; x += cellSize) {
            cells.add(new ArrayList<Cell>());
            for(int y = 0; y < Screen.height; y += cellSize) {
                cells.get(cells.size() - 1).add(new Cell());
            }
        }
        speed = 100;
        fps = 1000/speed;
        buttonSize = Math.min(Screen.height, Screen.width) / 10;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(buttonSize * 2 / 3);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(generating) {
            generateGeneration();
        }
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        paint.setColor(Color.BLACK);
        for(int x = 0; x < cells.size(); x ++) {
            for (int y = 0; y < cells.get(x).size(); y++) {
                if(cells.get(x).get(y).alive) {
                    canvas.drawRect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, paint);
                }
            }
        }
        int maxScreenDim = Math.max(Screen.width, Screen.height);
        for(int i = 0; i < maxScreenDim; i += cellSize) {
            canvas.drawLine(i, 0, i, Screen.height, paint);
            canvas.drawLine(0, i, Screen.width, i, paint);
        }
        if(generating) {
            paint.setColor(Color.GREEN);
        } else {
            paint.setColor(Color.LTGRAY);
        }

        canvas.drawRect(0, 0, buttonSize, buttonSize, paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(0, Screen.height - buttonSize, buttonSize, Screen.height, paint);
        paint.setColor(Color.RED);
        canvas.drawRect(Screen.width - buttonSize, Screen.height - buttonSize, Screen.width, Screen.height, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("s/s", buttonSize / 2, paint.getTextSize() / 3 + buttonSize / 2, paint);
        canvas.drawText("s", buttonSize / 2, paint.getTextSize() / 3 + Screen.height - buttonSize, paint);
        canvas.drawText("f", Screen.width - buttonSize, paint.getTextSize() / 3 + Screen.height - buttonSize, paint);

        canvas.drawText("FPS: " + Math.round(fps * 100d) / 100d, Screen.width / 2, (Screen.height - (buttonSize / 2)) + (paint.getTextSize() / 3), paint);

        super.onDraw(canvas);
    }
    void draw() {
        invalidate();
        requestLayout();
    }
    void generateGeneration() {
        // Apply the rules to each cell
        for(int x = 0; x < cells.size(); x ++) {
            for(int y = 0; y < cells.get(x).size(); y ++) {
                if(!rule1(x, y)) {
                    if (!rule3(x, y)) {
                        rule4(x, y);
                    }
                }
            }
        }

        // Kill cells that should be dead, spawn cells that should spawn
        for(int x = 0; x < cells.size(); x ++) {
            for (int y = 0; y < cells.get(x).size(); y++) {
                cells.get(x).get(y).alive = cells.get(x).get(y).shouldBeAlive;
            }
        }
    }
    private boolean rule1(int x, int y) {
        // Rule 1. Any live cell with fewer than two live neighbors dies, as if by underpopulation.

        if(cells.get(x).get(y).alive && numLiveNeighbors(x, y) < 2) {
            cells.get(x).get(y).shouldBeAlive = false;
            return true;
        }
        return false;
    }
    // The 2nd rule is done automatically
    private boolean rule3(int x, int y) {
        // Rule 3. Any live cell with more than three live neighbors dies, as if by overpopulation.

        if(cells.get(x).get(y).alive && numLiveNeighbors(x, y) > 3) {
            cells.get(x).get(y).shouldBeAlive = false;
            return true;
        }
        return false;
    }
    private boolean rule4(int x, int y) {
        // Rule 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

        if(!cells.get(x).get(y).alive && numLiveNeighbors(x, y) == 3) {
            cells.get(x).get(y).shouldBeAlive = true;
            return true;
        }
        return false;
    }
    private int numLiveNeighbors(int x, int y) {
        int rtrn = 0;
        if (x == 0) { // If the cell is on the left side of the board
            if (y == 0) { // If the cell is on the top of the board
                rtrn += cells.get(x).get(y + 1).alive ? 1 : 0; //     Middle  bottom
                rtrn += cells.get(x + 1).get(y + 1).alive ? 1 : 0; // Right   bottom
                rtrn += cells.get(x + 1).get(y).alive ? 1 : 0; //     Right   middle
            } else if (y == cells.get(x).size() - 1) { // If the cell is on the bottom of the board
                rtrn += cells.get(x + 1).get(y).alive ? 1 : 0; //     Right   middle
                rtrn += cells.get(x + 1).get(y - 1).alive ? 1 : 0; // Right   top
                rtrn += cells.get(x).get(y - 1).alive ? 1 : 0; //     Middle  top
            } else { // If the cell is somewhere in the middle
                rtrn += cells.get(x).get(y + 1).alive ? 1 : 0; //     Middle  bottom
                rtrn += cells.get(x + 1).get(y + 1).alive ? 1 : 0; // Right   bottom
                rtrn += cells.get(x + 1).get(y).alive ? 1 : 0; //     Right   middle
                rtrn += cells.get(x + 1).get(y - 1).alive ? 1 : 0; // Right   top
                rtrn += cells.get(x).get(y - 1).alive ? 1 : 0; //     Middle  top
            }
        } else if (x == cells.size() - 1) { // If the cell is on the right side of the board
            if (y == 0) { // If the cell is on the top of the board
                rtrn += cells.get(x - 1).get(y).alive ? 1 : 0; //     Left    middle
                rtrn += cells.get(x - 1).get(y + 1).alive ? 1 : 0; // Left    bottom
                rtrn += cells.get(x).get(y + 1).alive ? 1 : 0; //     Middle  bottom
            } else if (y == cells.get(x).size() - 1) { // If the cell is on the bottom of the board
                rtrn += cells.get(x - 1).get(y - 1).alive ? 1 : 0;//  Left    top
                rtrn += cells.get(x - 1).get(y).alive ? 1 : 0; //     Left    middle
                rtrn += cells.get(x).get(y - 1).alive ? 1 : 0; //     Middle  top
            } else { // If the cell is somewhere in the middle
                rtrn += cells.get(x - 1).get(y - 1).alive ? 1 : 0;//  Left    top
                rtrn += cells.get(x - 1).get(y).alive ? 1 : 0; //     Left    middle
                rtrn += cells.get(x - 1).get(y + 1).alive ? 1 : 0; // Left    bottom
                rtrn += cells.get(x).get(y + 1).alive ? 1 : 0; //     Middle  bottom
                rtrn += cells.get(x).get(y - 1).alive ? 1 : 0; //     Middle  top
            }
        } else { // If the cell is somewhere in the middle
            if (y == 0) { // If the cell is on the top of the board
                rtrn += cells.get(x - 1).get(y).alive ? 1 : 0; //     Left    middle
                rtrn += cells.get(x - 1).get(y + 1).alive ? 1 : 0; // Left    bottom
                rtrn += cells.get(x).get(y + 1).alive ? 1 : 0; //     Middle  bottom
                rtrn += cells.get(x + 1).get(y + 1).alive ? 1 : 0; // Right   bottom
                rtrn += cells.get(x + 1).get(y).alive ? 1 : 0; //     Right   middle
            } else if (y == cells.get(x).size() - 1) { // If the cell is on the bottom of the board
                rtrn += cells.get(x - 1).get(y - 1).alive ? 1 : 0;//  Left    top
                rtrn += cells.get(x - 1).get(y).alive ? 1 : 0; //     Left    middle
                rtrn += cells.get(x + 1).get(y).alive ? 1 : 0; //     Right   middle
                rtrn += cells.get(x + 1).get(y - 1).alive ? 1 : 0; // Right   top
                rtrn += cells.get(x).get(y - 1).alive ? 1 : 0; //     Middle  top
            } else { // If the cell is somewhere in the middle
                rtrn += cells.get(x - 1).get(y - 1).alive ? 1 : 0;//  Left    top
                rtrn += cells.get(x - 1).get(y).alive ? 1 : 0; //     Left    middle
                rtrn += cells.get(x - 1).get(y + 1).alive ? 1 : 0; // Left    bottom
                rtrn += cells.get(x).get(y + 1).alive ? 1 : 0; //     Middle  bottom
                rtrn += cells.get(x + 1).get(y + 1).alive ? 1 : 0; // Right   bottom
                rtrn += cells.get(x + 1).get(y).alive ? 1 : 0; //     Right   middle
                rtrn += cells.get(x + 1).get(y - 1).alive ? 1 : 0; // Right   top
                rtrn += cells.get(x).get(y - 1).alive ? 1 : 0; //     Middle  top
            }
        }
        return rtrn;
    }
    private void setFps(double FPS) {
        fps = FPS;
        speed = (int) (1000 / fps);
    }
    void actionUp(float touchX, float touchY) {
        if(touchX < buttonSize && touchY < buttonSize) {
            generating = !generating;
        } else if(touchX < buttonSize && touchY > Screen.height - buttonSize) {
            setFps(fps * 4 / 5);
        } else if(touchX > Screen.width - buttonSize && touchY > Screen.height - buttonSize) {
            setFps(fps * 5 / 4);
        } else {
            if (cells.get((int) Math.floor(touchX / cellSize)).get((int) Math.floor(touchY / cellSize)).alive) {
                cells.get((int) Math.floor(touchX / cellSize)).get((int) Math.floor(touchY / cellSize)).alive = false;
                cells.get((int) Math.floor(touchX / cellSize)).get((int) Math.floor(touchY / cellSize)).shouldBeAlive = false;
            } else {
                cells.get((int) Math.floor(touchX / cellSize)).get((int) Math.floor(touchY / cellSize)).alive = true;
                cells.get((int) Math.floor(touchX / cellSize)).get((int) Math.floor(touchY / cellSize)).shouldBeAlive = true;
            }
        }
    }
}
