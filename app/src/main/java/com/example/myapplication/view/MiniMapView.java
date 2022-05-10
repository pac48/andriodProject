package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.object.Cube;
import com.example.myapplication.object.GameObject;

import java.util.ArrayList;


public class MiniMapView extends SurfaceView implements SurfaceHolder.Callback {
    public SurfaceHolder surfaceHolder;
    public SurfaceView surfaceView;
    public boolean surfaceActive = false;
    Context context;
    Paint paint = new Paint();
    GameObject player;



    public MiniMapView(Context contextIn) {
        super(contextIn);
        context = contextIn;
        init();
    }
    public MiniMapView(Context contextIn, AttributeSet attrs) {
        super(contextIn, attrs);
        context = contextIn;
        init();

    }
    public void init(){
        setZOrderOnTop(true);
        surfaceView = findViewById(R.id.MiniMapView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        Cube.allCubes = new ArrayList<>();

    }

    public void addPlayer(GameObject playerIn){
    player = playerIn;
    }



    public void drawFrame(){
        if (!surfaceActive){
            return;
        }

        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawARGB(100,128,128,128);
        paint.setColor(0x90404040);

        int height = surfaceView.getHeight();
        int width = surfaceView.getWidth();
        int size = 38;

        for (Cube cube: Cube.allCubes) {
            Rect rectBase = new Rect((int) (width*(cube.posX+size/2)/size),(int)  (height*(-cube.posY+size/2)/size),
                    (int) (width*(cube.posX+size/2)/size+width/size), (int) (height*(-cube.posY+size/2)/size+height/size));
            canvas.drawRect(rectBase, paint);
        }

    if (player != null){
        paint.setColor(0x9000FF00);
            Rect rectBase = new Rect((int) (width*(player.posX+size/2)/size),(int)  (height*(-player.posY+size/2)/size),
                    (int) (width*(player.posX+size/2)/size+width/size), (int) (height*(-player.posY+size/2)/size+height/size));
            canvas.drawRect(rectBase, paint);
    }



//
//        Rect rectStick = new Rect(posX-scale/2, posY-scale/2, scale/2+posX, scale/2+posY);
//

        surfaceHolder.unlockCanvasAndPost(canvas);


    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        surfaceActive = true;
        drawFrame();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        drawFrame();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        surfaceActive = false;

    }
}
