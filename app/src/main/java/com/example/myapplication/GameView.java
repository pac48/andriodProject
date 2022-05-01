package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.mesh.Camera;
import com.example.myapplication.mesh.PlaneMesh;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.BitSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private boolean gameRunning;
    public SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private static final int MAX_FRAME_TIME = (int) (1000.0 / 60.0);


    float posX = 0;
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dots);


    public GameView(Context context) {
        super(context);
        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setEGLContextClientVersion(2);
        setFocusable(true);
        gameRunning = false;
        this.setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Use culling to remove back faces.
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -1.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Camera camera = GameActivity.camera;
        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(camera.mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);


        PlaneMesh planeMesh = GameActivity.planeMesh;
        planeMesh.material.compileAndLink();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) height/width;
        final float left = -1;
        final float right = 1;
        final float bottom = -ratio;
        final float top = ratio;
        final float near = 0.1f;
        final float far = 1000.0f;

        Camera camera = GameActivity.camera;
        Matrix.orthoM(camera.mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);		;

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        angleInDegrees = 0;
        PlaneMesh planeMesh = GameActivity.planeMesh;
        Camera camera = GameActivity.camera;

        GLES20.glUseProgram(planeMesh.material.programHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, planeMesh.material.textureHandle);
        GLES20.glUniform1i(planeMesh.material.textureHandle, 0);

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(planeMesh.material.light.mLightModelMatrix, 0);
        Matrix.translateM(planeMesh.material.light.mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
//        Matrix.rotateM(planeMesh.material.light.mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);

        Matrix.multiplyMV(planeMesh.material.light.mLightPosInWorldSpace, 0, planeMesh.material.light.mLightModelMatrix, 0, planeMesh.material.light.mLightPosInModelSpace, 0);
        Matrix.multiplyMV(planeMesh.material.light.mLightPosInEyeSpace, 0, camera.mViewMatrix, 0, planeMesh.material.light.mLightPosInWorldSpace, 0);



        Matrix.setIdentityM(planeMesh.mModelMatrix, 0);
        Matrix.scaleM(planeMesh.mModelMatrix, 0, .1f, .1f,.1f);

        Matrix.translateM(planeMesh.mModelMatrix, 0, 0.0f+posX, 0.0f, -2.0f);
        posX += 0.03;
        Matrix.rotateM(planeMesh.mModelMatrix, 0, angleInDegrees, 1.0f, 1.0f, 1.0f);

        drawPlaneMesh(planeMesh, camera);

//        posX += 0.005;
//        this.setVerticesAndDraw(0.4f+posX,0.0f, gl, (byte) 255);

    }

    void drawPlaneMesh(PlaneMesh planeMesh, Camera camera){
        // Pass in the position information
        planeMesh.positions.position(0);
        GLES20.glVertexAttribPointer(planeMesh.material.mPositionHandle, planeMesh.mPositionDataSize, GLES20.GL_FLOAT, false,
                0, planeMesh.positions);

        GLES20.glEnableVertexAttribArray(planeMesh.material.mPositionHandle);

        // Pass in the color information
        planeMesh.colors.position(0);
        GLES20.glVertexAttribPointer(planeMesh.material.mColorHandle, planeMesh.mColorDataSize, GLES20.GL_FLOAT, false,
                0, planeMesh.colors);

        GLES20.glEnableVertexAttribArray(planeMesh.material.mColorHandle);

        // Pass in the normal information
        planeMesh.normals.position(0);
        GLES20.glVertexAttribPointer(planeMesh.material.mNormalHandle, planeMesh.mNormalDataSize, GLES20.GL_FLOAT, false,
                0, planeMesh.normals);

        GLES20.glEnableVertexAttribArray(planeMesh.material.mNormalHandle);

        // Pass in the texture coordinate information
        planeMesh.textureCoordinates.position(0);
        GLES20.glVertexAttribPointer(planeMesh.material.mTextureCoordinateHandle, planeMesh.mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                0, planeMesh.textureCoordinates);

        GLES20.glEnableVertexAttribArray(planeMesh.material.mTextureCoordinateHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(camera.mMVPMatrix, 0, camera.mViewMatrix, 0, planeMesh.mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(planeMesh.material.mMVMatrixHandle, 1, false, camera.mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(camera.mMVPMatrix, 0, camera.mProjectionMatrix, 0, camera.mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(planeMesh.material.mMVPMatrixHandle, 1, false, camera.mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(planeMesh.material.light.mLightPosHandle, planeMesh.material.light.mLightPosInEyeSpace[0], planeMesh.material.light.mLightPosInEyeSpace[1], planeMesh.material.light.mLightPosInEyeSpace[2]);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

    }


    public void setVerticesAndDraw(Float value, float offset, GL10 gl, byte color) {
        FloatBuffer vertexbuffer;
        ByteBuffer indicesBuffer;
        ByteBuffer mColorBuffer;


        byte indices[] = {0, 1, 2, 0, 2, 3};

        float vetices[] = {//
                -value, value, 0.0f+offset,
                value, value, 0.0f+offset,
                value, -value, 0.0f+offset,
                -value, -value, 0.0f+offset
        };

        byte colors[] = //3
                {color, color, 0, color,
                        0, color, color, color,
                        0, 0, 0, color,
                        color, 0, color, (byte) 255
                };


        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vetices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexbuffer = byteBuffer.asFloatBuffer();
        vertexbuffer.put(vetices);
        vertexbuffer.position(0);

        indicesBuffer = ByteBuffer.allocateDirect(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.position(0);

        mColorBuffer = ByteBuffer.allocateDirect(colors.length);
        mColorBuffer.put(colors);
        mColorBuffer.position(0);


        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);


        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexbuffer);
        gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, mColorBuffer);

//        // texture non-sense
//        IntBuffer texture = IntBuffer.allocate(1);
//        gl.glGenTextures(1, texture);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.get(0));
//        int texHeight = 100;
//        int texWidth = 100;
//        IntBuffer dataBuffer = IntBuffer.allocate(texWidth*texHeight);
//        int[] data = dataBuffer.array();
//        bitmap.getPixels(data,0,1,0,0,texWidth,texHeight);
//        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGB, texWidth, texHeight, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, dataBuffer);
////        gl.Mip(GL10.GL_TEXTURE_2D);
//        bitmap.recycle();


        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indicesBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

    }


}
