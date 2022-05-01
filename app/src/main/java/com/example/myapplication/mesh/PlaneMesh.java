package com.example.myapplication.mesh;
import com.example.myapplication.geometry.PlanGeometry;
import com.example.myapplication.material.Material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class PlaneMesh {
    public PlanGeometry planGeometry;
    public Material material;
    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    public float[] mModelMatrix;

    /** How many bytes per float. */
    public final int mBytesPerFloat = 4;
    /** Size of the position data in elements. */
    public final int mPositionDataSize = 3;
    /** Size of the color data in elements. */
    public final int mColorDataSize = 4;
    /** Size of the normal data in elements. */
    public final int mNormalDataSize = 3;
    /** Size of the texture coordinate data in elements. */
    public final int mTextureCoordinateDataSize = 2;
    /** Store our model data in a float buffer. */
    public final FloatBuffer positions;
    public final FloatBuffer colors;
    public final FloatBuffer normals;
    public final FloatBuffer textureCoordinates;


    public PlaneMesh(Material materialIn){
        planGeometry = new PlanGeometry();
//        material = new Material();
        material = materialIn;
        mModelMatrix = new float[16];

        // Initialize the buffers.
        positions = ByteBuffer.allocateDirect(planGeometry.positionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        positions.put(planGeometry.positionData).position(0);

        colors = ByteBuffer.allocateDirect(planGeometry.colorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        colors.put(planGeometry.colorData).position(0);

        normals = ByteBuffer.allocateDirect(planGeometry.normalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        normals.put(planGeometry.normalData).position(0);

        textureCoordinates = ByteBuffer.allocateDirect(planGeometry.textureCoordinateData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureCoordinates.put(planGeometry.textureCoordinateData).position(0);

    }

}
