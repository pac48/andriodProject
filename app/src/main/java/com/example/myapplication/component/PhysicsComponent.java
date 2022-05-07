package com.example.myapplication.component;

import com.example.myapplication.object.GameObject;

public class PhysicsComponent {
    public GameObject gameObject;

    public void preStep(float dt){
    }

    public void step(float dt){
        gameObject.posX += gameObject.velX*dt;
        gameObject.posY += gameObject.velY*dt;
        gameObject.posZ += gameObject.velZ*dt;
    }

    public void postAddComponent() {

    }
}
