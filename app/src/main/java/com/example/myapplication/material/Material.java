package com.example.myapplication.material;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.example.myapplication.R;
import com.example.myapplication.object.Light;
import com.example.myapplication.shader.ShaderHelper;
import com.example.myapplication.utils.RawResourceReader;

import java.util.ArrayList;
import java.util.HashMap;


public class Material {
    public static String[] attributes;
    public static String vertexShader;
    public static String fragmentShader;
    public static HashMap<Integer, Material> instances = new HashMap<>();
    public static HashMap<ArrayList<Bitmap>, Material> animatedInstances = new HashMap<>();

    public int programHandle;
    public ArrayList<Integer> textureHandles;
    public int mMVPMatrixHandle;
    public int mMVMatrixHandle;
    public int mPositionHandle;
    public int mColorHandle ;
    public int mNormalHandle;
    public int mTextureCoordinateHandle;
    public int mLightPosHandle;

    public int bitmapId;
    public Context context;
    public final ArrayList<Bitmap> bitmaps;
    public int animationFrame = 0;
    public boolean compiled;


    public Material(Context contextIn, final int bitmapIdIn) {
        bitmapId = bitmapIdIn;
        context = contextIn;
        compiled = false;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId, options);
        bitmaps = new ArrayList<>();
        bitmaps.add(bitmap);

        textureHandles = new ArrayList<>();

         vertexShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.per_pixel_vertex_shader);
         fragmentShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.per_pixel_fragment_shader);
    }

    public Material(Context contextIn, final ArrayList<Bitmap>  bitmapsIn) {
        bitmaps = bitmapsIn;
        context = contextIn;
        textureHandles = new ArrayList<>();

        vertexShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.per_pixel_vertex_shader);
        fragmentShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.per_pixel_fragment_shader);
    }

    public static Material getInstance(Context contextIn, final int bitmapId){
        Material out;
        if(instances.containsKey(bitmapId)){
            out = instances.get(bitmapId);
        } else{
            out = new Material(contextIn, bitmapId);
        }
        instances.put(bitmapId, out);
        return out;
    }

    public static Material getInstance(Context contextIn, final ArrayList<Bitmap> bitmapsIn){
        Material out;
        if(animatedInstances.containsKey(bitmapsIn)){
            out = animatedInstances.get(bitmapsIn);
        } else{
            out = new Material(contextIn, bitmapsIn);
        }
        animatedInstances.put(bitmapsIn, out);
        return out;

    }

    public void compileAndLink(){
        if (compiled) return;

        // compile shaders
        int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        attributes = new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate", "u_LightPos"};

        programHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, attributes);

        for (Bitmap bitmap : bitmaps){
            textureHandles.add(TextureHelper.loadTexture(context, bitmap));
        }

        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(programHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");
        mLightPosHandle = GLES20.glGetUniformLocation(programHandle, "u_LightPos");



        compiled = true;

    }

    public int getTextureHandle(){
        return textureHandles.get(animationFrame);
    }



}


