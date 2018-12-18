package com.conwaysgame.oflife.conwaysgameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class GameView extends View {
    // From wikipedia:
    // Rule 1. Any live cell with fewer than two live neighbors dies, as if by underpopulation.
    // Rule 2. Any live cell with two or three live neighbors lives on to the next generation.
    // Rule 3. Any live cell with more than three live neighbors dies, as if by overpopulation.
    // Rule 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

    // For now, this game is only screen wide and high. I may change that later.
    // Also for now, every cell is 10px by 10px
    private final int cellSize = 10;
    private ArrayList<ArrayList<Cell>> cells;
    ArrayList<Cell> temporaryCellList;

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
        temporaryCellList = new ArrayList<>();
        for(int i = 0; i < Screen.height; i += cellSize) {
            temporaryCellList.add(new Cell());
        }
        for(int i = 0; i < Screen.width; i += cellSize) {
            cells.add(temporaryCellList);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    void draw() {
        invalidate();
        requestLayout();
    }
    private void generateGeneration() {
        // Apply the rules to each cell
        for(int x = 0; x < cells.size(); x ++) {
            for(int y = 0; y < cells.get(x).size(); y ++) {
                if(!rule1(x, y)) {
                    if (!rule2(x, y)) {
                        if (!rule3(x, y)) {
                            rule4(x, y);
                        }
                    }
                }
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
    private boolean rule2(int x, int y) {
        // Rule 2. Any live cell with two or three live neighbors lives on to the next generation.

        return false;
    }
    private boolean rule3(int x, int y) {
        // Rule 3. Any live cell with more than three live neighbors dies, as if by overpopulation.

        return false;
    }
    private boolean rule4(int x, int y) {
        // Rule 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

        return false;
    }
    private int numLiveNeighbors(int x, int y) {
        int rtrn = 0;
        if (x == 0) { // If the cell is on the left side of the board
            if (y == 0) { // If the cell is on the top of the board
                rtrn += cells.get(x).get(y + 1).alive ? 1 : 0; //     Middle  bottom
                rtrn += cells.get(x + 1).get(y + 1).alive ? 1 : 0; // Right   bottom
                rtrn += cells.get(x + 1).get(y).alive ? 1 : 0; //     Right   middle
            } else if (y == cells.size()) { // If the cell is on the bottom of the board
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
            } else if (y == cells.size()) { // If the cell is on the bottom of the board
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
            } else if (y == cells.size()) { // If the cell is on the bottom of the board
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
}
