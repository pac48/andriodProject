package com.example.myapplication.object;
import com.example.myapplication.geometry.PlanGeometry;
import com.example.myapplication.material.Material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Plane extends GameObject{


    public Plane(Material materialIn){
        super();

        PlanGeometry planGeometry = new PlanGeometry();

        // Initialize the buffers.
        FloatBuffer positions = ByteBuffer.allocateDirect(planGeometry.positionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        positions.put(planGeometry.positionData).position(0);

        FloatBuffer colors = ByteBuffer.allocateDirect(planGeometry.colorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        colors.put(planGeometry.colorData).position(0);

        FloatBuffer normals = ByteBuffer.allocateDirect(planGeometry.normalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        normals.put(planGeometry.normalData).position(0);

        FloatBuffer textureCoordinates = ByteBuffer.allocateDirect(planGeometry.textureCoordinateData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureCoordinates.put(planGeometry.textureCoordinateData).position(0);

        setProperties(materialIn, positions, normals, textureCoordinates, colors);





    }

}
