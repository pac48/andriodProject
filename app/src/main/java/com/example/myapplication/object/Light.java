package com.example.myapplication.object;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Light extends GameObject{
    public int mLightPosHandle;
    public float[] mLightModelMatrix;
    public final float[] mLightPosInModelSpace;
    public final float[] mLightPosInWorldSpace;
    public final float[] mLightPosInEyeSpace;



    public Light(){
        mLightModelMatrix = new float[16];
        mLightPosInModelSpace = new float[] {0.0f, 10.0f, 10.0f, 1.0f};
        mLightPosInWorldSpace = new float[4];
        mLightPosInEyeSpace = new float[4];

    }

}