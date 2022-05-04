package com.example.myapplication.object;

import com.example.myapplication.geometry.MeshGeometry;
import com.example.myapplication.material.Material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Tank3D extends GameObject{


    public Tank3D(Material materialIn) {
        super();

        MeshGeometry cubeGeometry = new MeshGeometry("meshes/tank.obj");
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

    @Override
    public void setVelX(float vel) {
        velX = vel;
//        setTankAngle();
    }
    @Override
    public void setVelY(float vel) {
        velY = vel;
        setTankAngle();
    }


    public void setTankAngle(){
        float newAngle = (float) ((float) 180*Math.atan2(velY, velX)/Math.PI) + 90;
        float offset;
        if (newAngle - angle > 180){ // the negative direction is shorter
            newAngle += 360;
            offset = newAngle-angle;//Math.signum(newAngle-angle)*Math.min(Math.abs(newAngle-angle), 10);
        } else{
            offset = newAngle-angle;//Math.signum(newAngle - angle)*Math.min(Math.abs(newAngle-angle), 10);
        }
        if (offset > 180){
            offset -= 360;
        } else if(offset < -180){
            offset += 360;
        }
        offset = Math.signum(offset)*Math.min(Math.abs(offset), 10);

        angleVel = .5f*offset + .5f*angleVel;
        angle = (angle + angleVel) % 360;

        float mag = (float) Math.sqrt(velX*velX+velY*velY);
        velX = (float) (mag*Math.cos(Math.PI*(angle-90)/180));
        velY = (float) (mag*Math.sin(Math.PI*(angle-90)/180));

    }
}
