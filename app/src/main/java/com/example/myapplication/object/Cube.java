package com.example.myapplication.object;

import com.example.myapplication.geometry.MeshGeometry;
import com.example.myapplication.material.Material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube extends GameObject{


    public Cube(Material materialIn) {
        super();

        MeshGeometry cubeGeometry = new MeshGeometry("meshes/cube.obj");
        numVerts = cubeGeometry.numVerts;

        // Initialize the buffers.
        FloatBuffer positions = ByteBuffer.allocateDirect(cubeGeometry.positionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        positions.put(cubeGeometry.positionData).position(0);

        FloatBuffer colors = ByteBuffer.allocateDirect(cubeGeometry.colorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        colors.put(cubeGeometry.colorData).position(0);

        FloatBuffer normals = ByteBuffer.allocateDirect(cubeGeometry.normalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        normals.put(cubeGeometry.normalData).position(0);

        FloatBuffer textureCoordinates = ByteBuffer.allocateDirect(cubeGeometry.textureCoordinateData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureCoordinates.put(cubeGeometry.textureCoordinateData).position(0);

        setProperties(materialIn, positions, normals, textureCoordinates, colors);


    }

//    @Override
//    public void step(float dt){
//        super.step(dt);
//        axisX+=.05;
//        axisY+=.05;
//        angle+=1;
//
//    }
}
