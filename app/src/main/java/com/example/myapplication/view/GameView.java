package com.example.myapplication.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.AttributeSet;

import com.example.myapplication.PhysicsManager;
import com.example.myapplication.Scene;
import com.example.myapplication.material.Material;
import com.example.myapplication.object.Camera;
import com.example.myapplication.object.GameObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private boolean gameRunning;

    private static final int MAX_FRAME_TIME = (int) (1000.0 / 60.0);
    long time;
    public Scene scene;
    JoyView joyView;
    MiniMapView miniMapView;



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
    public void setControlView(JoyView joyViewIn){
        joyView = joyViewIn;
    }
    public void setMiniMapView(MiniMapView miniMapViewIn){
        miniMapView = miniMapViewIn;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        // Set the background clear color to black.
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

//        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        for(Material material : Material.instances.values()){
            material.compileAndLink();
        }
        for(Material material : Material.animatedInstances.values()){
            material.compileAndLink();
        }

//        for (GameObject gameObject : scene.objects){
//            if (gameObject.material != null && !gameObject.material.compiled){
//                gameObject.material.compileAndLink();
//                gameObject.material.compiled = true;
//            }
//        }


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        Camera camera = scene.camera;
        camera.aspect = ((float) getWidth())/((float) getHeight());
        Matrix.perspectiveM(camera.mProjectionMatrix, 0, camera.fovy, camera.aspect, camera.zNear, camera.zFar);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        float dt = 1.0f/60.0f;
        time = SystemClock.uptimeMillis() % 10000L;

        // apply input from all game components
        for (GameObject gameObject : scene.objects){
            gameObject.applyComponents(dt);
        }

        // apply pos, vel update using physics
        PhysicsManager.getInstance().step(dt, scene.objects);

        // render objects
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Camera camera = scene.camera;



        // need to loop over all shader programs
        // only one current program exist

        GLES20.glUseProgram(scene.objects.get(5).material.programHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, scene.objects.get(5).material.getTextureHandle());
        GLES20.glUniform1i(scene.objects.get(5).material.getTextureHandle(), 0);


        for (GameObject gameObject : scene.objects){

            if (gameObject.material == null) continue;




            // Calculate position of the light. Rotate and then push into the distance.
            Matrix.setIdentityM(scene.light.mLightModelMatrix, 0);
            Matrix.translateM(scene.light.mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
    //        Matrix.rotateM(planeMesh.material.light.mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);

            Matrix.multiplyMV(scene.light.mLightPosInWorldSpace, 0, scene.light.mLightModelMatrix, 0, scene.light.mLightPosInModelSpace, 0);
            Matrix.multiplyMV(scene.light.mLightPosInEyeSpace, 0, camera.mViewMatrix, 0, scene.light.mLightPosInWorldSpace, 0);



            drawMesh(gameObject, camera);

        }

        joyView.drawFrame();
        miniMapView.drawFrame();

    }

    void drawMesh(GameObject gameObject, Camera camera){
        // Pass in the position information
        gameObject.positions.position(0);
        GLES20.glVertexAttribPointer(gameObject.material.mPositionHandle, gameObject.mPositionDataSize, GLES20.GL_FLOAT, false,
                0, gameObject.positions);

        GLES20.glEnableVertexAttribArray(gameObject.material.mPositionHandle);

        // Pass in the color information
        gameObject.colors.position(0);
        GLES20.glVertexAttribPointer(gameObject.material.mColorHandle, gameObject.mColorDataSize, GLES20.GL_FLOAT, false,
                0, gameObject.colors);

        GLES20.glEnableVertexAttribArray(gameObject.material.mColorHandle);

        // Pass in the normal information
        gameObject.normals.position(0);
        GLES20.glVertexAttribPointer(gameObject.material.mNormalHandle, gameObject.mNormalDataSize, GLES20.GL_FLOAT, false,
                0, gameObject.normals);

        GLES20.glEnableVertexAttribArray(gameObject.material.mNormalHandle);

        // Pass in the texture coordinate information
        gameObject.textureCoordinates.position(0);
        GLES20.glVertexAttribPointer(gameObject.material.mTextureCoordinateHandle, gameObject.mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                0, gameObject.textureCoordinates);

        GLES20.glEnableVertexAttribArray(gameObject.material.mTextureCoordinateHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(camera.mMVPMatrix, 0, camera.mViewMatrix, 0, gameObject.mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(gameObject.material.mMVMatrixHandle, 1, false, camera.mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(camera.mMVPMatrix, 0, camera.mProjectionMatrix, 0, camera.mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(gameObject.material.mMVPMatrixHandle, 1, false, camera.mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(scene.light.mLightPosHandle, scene.light.mLightPosInEyeSpace[0], scene.light.mLightPosInEyeSpace[1], scene.light.mLightPosInEyeSpace[2]);

        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, gameObject.numVerts);

    }


//    public void setVerticesAndDraw(Float value, float offset, GL10 gl, byte color) {
//        FloatBuffer vertexbuffer;
//        ByteBuffer indicesBuffer;
//        ByteBuffer mColorBuffer;
//
//
//        byte indices[] = {0, 1, 2, 0, 2, 3};
//
//        float vetices[] = {//
//                -value, value, 0.0f+offset,
//                value, value, 0.0f+offset,
//                value, -value, 0.0f+offset,
//                -value, -value, 0.0f+offset
//        };
//
//        byte colors[] = //3
//                {color, color, 0, color,
//                        0, color, color, color,
//                        0, 0, 0, color,
//                        color, 0, color, (byte) 255
//                };
//
//
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vetices.length * 4);
//        byteBuffer.order(ByteOrder.nativeOrder());
//        vertexbuffer = byteBuffer.asFloatBuffer();
//        vertexbuffer.put(vetices);
//        vertexbuffer.position(0);
//
//        indicesBuffer = ByteBuffer.allocateDirect(indices.length);
//        indicesBuffer.put(indices);
//        indicesBuffer.position(0);
//
//        mColorBuffer = ByteBuffer.allocateDirect(colors.length);
//        mColorBuffer.put(colors);
//        mColorBuffer.position(0);
//
//
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
//
//
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexbuffer);
//        gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, mColorBuffer);
//
////        // texture non-sense
////        IntBuffer texture = IntBuffer.allocate(1);
////        gl.glGenTextures(1, texture);
////        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.get(0));
////        int texHeight = 100;
////        int texWidth = 100;
////        IntBuffer dataBuffer = IntBuffer.allocate(texWidth*texHeight);
////        int[] data = dataBuffer.array();
////        bitmap.getPixels(data,0,1,0,0,texWidth,texHeight);
////        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGB, texWidth, texHeight, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, dataBuffer);
//////        gl.Mip(GL10.GL_TEXTURE_2D);
////        bitmap.recycle();
//
//
//        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indicesBuffer);
//
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
//
//    }


    public void setScene(Scene sceneIn) {
        scene = sceneIn;
    }
}
