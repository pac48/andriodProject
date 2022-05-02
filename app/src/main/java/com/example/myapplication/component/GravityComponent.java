package com.example.myapplication.component;

import android.opengl.Matrix;

import com.example.myapplication.object.GameObject;

public class GravityComponent extends GameComponent{
    public float xDir = 0;
    public float yDir = 0;
    public float zDir = -1;
    public float gravity = 1;

    public GravityComponent(){

    }

    public GravityComponent(float gravityIn, float xDirIn, float yDirIn, float zDirIn){
        gravity = gravityIn;
        xDir = xDirIn;
        yDir = yDirIn;
        zDir = zDirIn;
    }

    @Override
    public void step(GameObject gameObject, float dt) {
        gameObject.velX += xDir*gravity*dt;
        gameObject.velY += yDir*gravity*dt;
        gameObject.velZ += zDir*gravity*dt;

    }
}
