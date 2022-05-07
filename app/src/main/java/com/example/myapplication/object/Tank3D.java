package com.example.myapplication.object;

import com.example.myapplication.object.geometry.MeshGeometry;
import com.example.myapplication.material.Material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Tank3D extends GameObject{
    public float maxAngleVel = 5;
    public float velXOld = 0;
    public float velYOld = 0;

    public Tank3D(Material materialIn) {
        super();
        angle = 90;

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
        velX = .9f*velXOld + .1f*vel;
        velXOld = velX;
//        setTankAngle();
    }
    @Override
    public void setVelY(float vel) {
        velY = .9f*velYOld + .1f*vel;
        velYOld = velY;
        setTankAngle();
    }


    public void setTankAngle(){
        float newAngle = (float) ((float) 180*Math.atan2(velY, velX)/Math.PI) + 90;
        float reverse = 1;

        if (newAngle - angle > 180) { // the negative direction is shorter
            newAngle -= 360;
        }
        if (newAngle - angle < -180) { // the positive direction is shorter
            newAngle += 360;
        }

        float offset = newAngle-angle;
        if (Math.abs(offset) > 90 + 20){ // reverse
            reverse = -1;
            offset = 180 - offset;
            if (offset > 180) { // the negative direction is shorter
                offset -= 360;
            }
            if (offset< -180) { // the positive direction is shorter
                offset += 360;
            }
        }

        angleVel = .5f*offset + .5f*angleVel;
        angleVel = reverse*Math.signum(angleVel)*Math.min(Math.abs(angleVel), maxAngleVel);
        angle = (angle + angleVel) % 360;

        float mag = (float) Math.sqrt(velX*velX+velY*velY);
        velX = (float) ((float) reverse*(mag*Math.cos(Math.PI*(angle-90)/180)));
        velY = (float) ((float) reverse*(mag*Math.sin(Math.PI*(angle-90)/180)));


    }
}
