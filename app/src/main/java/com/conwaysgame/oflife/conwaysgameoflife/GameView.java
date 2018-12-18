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
    // Also for now, every cell is 10px by 10px
    private final int cellSize = 20;
    private ArrayList<ArrayList<Cell>> cells;
    private Paint paint;

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
        cells.get(30).get(30).alive = true;
        cells.get(30).get(30).shouldBeAlive = true;

        cells.get(32).get(30).alive = true;
        cells.get(32).get(30).shouldBeAlive = true;

        cells.get(30).get(31).alive = true;
        cells.get(30).get(31).shouldBeAlive = true;

        cells.get(31).get(31).alive = true;
        cells.get(31).get(31).shouldBeAlive = true;

        cells.get(29).get(30).alive = true;
        cells.get(29).get(30).shouldBeAlive = true;

        cells.get(28).get(30).alive = true;
        cells.get(28).get(30).shouldBeAlive = true;
        paint = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        paint.setColor(Color.BLACK);
        for(int x = 0; x < cells.size(); x ++) {
            for (int y = 0; y < cells.get(x).size(); y++) {
                if(cells.get(x).get(y).alive) {
                    canvas.drawRect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, paint);
                }
            }
            canvas.drawLine(x * cellSize, 0, x * cellSize, Screen.height, paint);
            canvas.drawLine(0, x * cellSize, Screen.width, x * cellSize, paint);
        }

        canvas.drawRect(0, 0, 50, 50, paint);

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
//                    if (!rule2(x, y)) { // This rule is done automatically.
                        if (!rule3(x, y)) {
                            rule4(x, y);
                        }
//                    }
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
    void actionUp(float touchX, float touchY) {
        if(touchX < 50 && touchY < 50) {
            generateGeneration();
        } else {
            if (cells.get((int) Math.floor(touchX / 10)).get((int) Math.floor(touchY / 10)).alive) {
                cells.get((int) Math.floor(touchX / 10)).get((int) Math.floor(touchY / 10)).alive = false;
                cells.get((int) Math.floor(touchX / 10)).get((int) Math.floor(touchY / 10)).shouldBeAlive = false;
            } else {
                cells.get((int) Math.floor(touchX / 10)).get((int) Math.floor(touchY / 10)).alive = true;
                cells.get((int) Math.floor(touchX / 10)).get((int) Math.floor(touchY / 10)).shouldBeAlive = true;
            }
        }
    }
}
