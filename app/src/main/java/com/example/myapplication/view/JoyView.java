package com.example.myapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.Scene;
import com.example.myapplication.object.Camera;
import com.example.myapplication.object.GameObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class JoyView extends SurfaceView implements SurfaceHolder.Callback {
    public SurfaceHolder surfaceHolder;
    public SurfaceView surfaceView;
    public boolean surfaceActive = false;
    Context context;
    Bitmap baseBitmap;
    Bitmap stickBitmap;
    public int posX = 0;
    public int posY = 0;
    public boolean pressed = false;


    public JoyView(Context contextIn) {
        super(contextIn);
        context = contextIn;
        init();
    }
    public JoyView(Context contextIn, AttributeSet attrs) {
        super(contextIn, attrs);
        context = contextIn;
        init();

    }
    public void init(){
        setZOrderOnTop(true);
        surfaceView = findViewById(R.id.JoyView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inScaled = false;
         baseBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.joy_base);
         stickBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.joy_stick);

        posX = surfaceView.getWidth()/2;
        posY = surfaceView.getHeight()/2;

    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // Define your OnTouchEvent actions here
//        // Use "switch" or "if" statement on ev.getActionMasked() or ev.getAction()
//
//        // Dispatch event to the children
//        for (int i = 0; i < getChildCount(); i++) {
//            getChildAt(i).dispatchTouchEvent(event);
//        }
//        return true;
//    }
    public float getAxisX(){
        float scale = surfaceView.getHeight();
        float width = surfaceView.getWidth();
        return (posX - width/2)/scale;
    }
    public float getAxisY(){
        float scale = surfaceView.getHeight();
        return (posY - scale/2)/scale;
    }

@Override
public boolean onTouchEvent(MotionEvent e) {
    posX = (int) e.getX();
    posY = (int) e.getY();
    int size = surfaceView.getHeight();

    int deltaX = posX - surfaceView.getWidth()/2;
    int deltaY = posY - surfaceView.getHeight()/2;
    double mag = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    double scale = Math.min(mag, size*.2);

    posX = (int) (surfaceView.getWidth()/2 + scale*(deltaX/mag));
    posY = (int) ( surfaceView.getHeight()/2 + scale*(deltaY/mag));

//    drawFrame();

    if (e.getAction() == MotionEvent.ACTION_UP){
        pressed = false;
    } else if(e.getAction() == MotionEvent.ACTION_DOWN){
        pressed = true;
    }
    return true;
}

    public void drawFrame(){
        if (!surfaceActive){
            return;
        }
        if (!pressed){
            posX = (int)(.9*posX + .1*surfaceView.getWidth()/2);
            posY = (int)(.9*posY + .1*surfaceView.getHeight()/2);
        }

        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        canvas.drawARGB(100,255,255,255);
        int scale = surfaceView.getHeight();
        int xOffset = surfaceView.getWidth()/2 - scale/2;
        Rect rectBase = new Rect(xOffset, 0, scale+xOffset, scale);

        Rect rectStick = new Rect(posX-scale/2, posY-scale/2, scale/2+posX, scale/2+posY);

        canvas.drawBitmap(baseBitmap, new Rect(0,0,baseBitmap.getWidth(),baseBitmap.getHeight()), rectBase, null);
        canvas.drawBitmap(stickBitmap, new Rect(0,0, stickBitmap.getWidth(), stickBitmap.getHeight()), rectStick, null);
        surfaceHolder.unlockCanvasAndPost(canvas);


    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        surfaceActive = true;
        posX = surfaceView.getWidth()/2;
        posY = surfaceView.getHeight()/2;
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
