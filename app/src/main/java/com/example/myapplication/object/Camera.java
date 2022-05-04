package com.example.myapplication.object;

import android.opengl.Matrix;

public class Camera extends GameObject{
    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    public float[] mViewMatrix;
    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    public float[] mProjectionMatrix;
    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    public float[] mMVPMatrix;
    // Position the eye in front of the origin.

    // We are looking toward the distance
    public float lookX = 0.0f;
    public float lookY = 0.0f;
    public float lookZ = 0.0f;

    // Set our up vector. This is where our head would be pointing were we holding the camera.
    final float upX = 0.0f;
    final float upY = 1.0f;
    final float upZ = 0.0f;

    public float fovy;
    public float aspect;
    public float zNear;
    public float zFar;


    public Camera(){
        mViewMatrix = new float[16];
        mProjectionMatrix = new float[16];
        mMVPMatrix = new float[16];
         posX = 0.0f;
         posY = 0.0f;
         posZ = 10.0f;
        fovy = 12;
        aspect = 2;
        zNear = 0.1f;
        zFar = 100;

    }

        @Override
    public void step(float dt){
        super.step(dt);
        Matrix.setLookAtM(mViewMatrix, 0, posX, posY, posZ, lookX, lookY, lookZ, upX, upY, upZ);


    }


}
