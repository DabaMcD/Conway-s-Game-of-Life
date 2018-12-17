package com.conwaysgame.oflife.conwaysgameoflife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;

public class MainActivity extends AppCompatActivity {
    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        gameView = findViewById(R.id.gameView);
//        gameView.constructor();
//        Touch.setTouchListener(gameView, gameView);
    }
}
