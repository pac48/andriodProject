package com.example.myapplication.object;

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

    public Camera(){
        mViewMatrix = new float[16];
        mProjectionMatrix = new float[16];
        mMVPMatrix = new float[16];


    }


}
