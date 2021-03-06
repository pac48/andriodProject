package com.example.myapplication.object;

import android.opengl.Matrix;

import com.example.myapplication.component.GameComponent;
import com.example.myapplication.component.PhysicsComponent;
import com.example.myapplication.component.RigidBodyComponent;
import com.example.myapplication.material.Material;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;

public class GameObject {
    public Material material;
    public HashSet<GameComponent> gameComponents;
    public PhysicsComponent physicsComponent;
    /** Store our model data in a float buffer. */
    public FloatBuffer positions;
    public FloatBuffer colors;
    public FloatBuffer normals;
    public FloatBuffer textureCoordinates;
    public int numVerts;

    public float posX;
    public float posY;
    public float posZ;

    public float velX;
    public float velY;
    public float velZ;

    public float axisX;
    public float axisY;
    public float axisZ = 1.0f;
    public float angle; // degrees
    float angleVel;

    public float scaleX = 1.0f;
    public float scaleY = 1.0f;
    public float scaleZ = 1.0f;

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


    public GameObject(){

        mModelMatrix = new float[16];
        gameComponents = new HashSet<>();
        physicsComponent = new PhysicsComponent();
        physicsComponent.gameObject = this;

    }

    public void addComponent(GameComponent gameComponent){
        gameComponent.gameObject = this;
        gameComponents.add(gameComponent);
        gameComponent.postAddComponent();
    }
    public void addComponent(PhysicsComponent gameComponent){
        gameComponent.gameObject = this;
        physicsComponent = gameComponent;
        gameComponent.postAddComponent();
    }

    public void setProperties(Material materialIn, FloatBuffer positionsIn, FloatBuffer normalsIn, FloatBuffer textureCoordinatesIn, FloatBuffer colorsIn){
        material = materialIn;
        positions = positionsIn;
        normals = normalsIn;
        textureCoordinates = textureCoordinatesIn;
        colors = colorsIn;
    }

    public void postSimPhysics(float dt){
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, posX, posY, posZ);
        Matrix.rotateM(mModelMatrix, 0, angle, axisX, axisY, axisZ);
        Matrix.scaleM(mModelMatrix, 0, scaleX, scaleY,scaleZ);
    }

    public void applyComponents(float dt){
        for(GameComponent gameComponents : gameComponents){
            gameComponents.apply(dt);
        }

    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public void setVelZ(float velZ) {
        this.velZ = velZ;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
