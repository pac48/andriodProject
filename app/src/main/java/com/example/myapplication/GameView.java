package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.BitSet;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private boolean gameRunning;
    public SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    Thread drawThread;
    private static final int MAX_FRAME_TIME = (int) (1000.0 / 60.0);


    int posX = 0;
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dots);


    public GameView(Context context, SurfaceView surfaceViewIn) {
        super(context);
//        getHolder().addCallback(this);
        // Focus will give us the possibility to get focus and catch touch
        // events.
        setFocusable(true);
        gameRunning = false;

        surfaceView = surfaceViewIn;
        if (surfaceView != null) {
                setupSurfaceHolder();
        }
    }



    //...Other code related to permissions
    //...Other code related to view setup
    //...etc.

    private void setupSurfaceHolder() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
//        Utils.debug(this, "Surface created");

        if (!gameRunning) {
            drawThread = new Thread(this, "Draw thread");
            drawThread.start();
            gameRunning = true;
        }


    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
    public void drawFrame(){
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawARGB(255,255,255,255);
        canvas.drawBitmap(bitmap, new Rect(0,0,467*2*2,467*2*2), new Rect(posX,0,200+posX,200), null);
        posX += 2;
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void run() {
        // this method is the main game draw loop

        long frameStartTime;
        long frameTime;

        try
        {
            while (gameRunning)
            {
                if (surfaceHolder == null)
                {
                    return;
                }

                frameStartTime = System.nanoTime();

                drawFrame();

                // calculate the time required to draw the frame in ms
                frameTime = (System.nanoTime() - frameStartTime) / 1000000;

                if (frameTime < MAX_FRAME_TIME) // faster than the max fps - limit the FPS
                {
                    try
                    {
                        Thread.sleep(MAX_FRAME_TIME - frameTime);
                    } catch (InterruptedException e)
                    {
                        // ignore
                    }
                }
            }
        } catch (Exception e) {
        // ignore
        }

    }


}
