package com.example.myapplication.material;

import android.content.Context;
import android.opengl.GLES20;

import com.example.myapplication.R;
import com.example.myapplication.material.TextureHelper;
import com.example.myapplication.shader.ShaderHelper;
import com.example.myapplication.utils.RawResourceReader;

public class Material {
    public int textureHandle;
    public int programHandle;
    public String[] attributes;
    public Light light;
    public int mMVPMatrixHandle;
    public int mMVMatrixHandle;
    public int mTextureUniformHandle;
    public int mPositionHandle;
    public int mColorHandle ;
    public int mNormalHandle;
    public int mTextureCoordinateHandle;
    public int bitmapId;
    public Context context;


    public Material(Context contextIn, final int bitmapIdIn) {
        bitmapId = bitmapIdIn;
        context = contextIn;

    }
    public void compileAndLink(){
//        resourceId is the bitmap texture
        int vertexShaderId = R.raw.per_pixel_vertex_shader;
        int fragmentShaderId = R.raw.per_pixel_fragment_shader;

        // compile shaders
        String vertexShader = RawResourceReader.readTextFileFromRawResource(context, vertexShaderId);
        String fragmentShader = RawResourceReader.readTextFileFromRawResource(context, fragmentShaderId);
        int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        attributes = new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"};

        programHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, attributes);

        textureHandle =  TextureHelper.loadTexture(context, bitmapId);


        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(programHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");
        light = new Light();


    }

    public class Light {
        public int mLightPosHandle;
        public float[] mLightModelMatrix;
        public final float[] mLightPosInModelSpace;
        public final float[] mLightPosInWorldSpace;
        public final float[] mLightPosInEyeSpace;



        public Light(){
            mLightPosHandle = GLES20.glGetUniformLocation(programHandle, "u_LightPos");
            mLightModelMatrix = new float[16];
            mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
            mLightPosInWorldSpace = new float[4];
            mLightPosInEyeSpace = new float[4];

        }

    }

}


