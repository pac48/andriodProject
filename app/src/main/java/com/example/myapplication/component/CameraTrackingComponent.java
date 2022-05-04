package com.example.myapplication.component;

import com.example.myapplication.object.Camera;
import com.example.myapplication.object.GameObject;
import com.example.myapplication.view.JoyView;


public class CameraTrackingComponent extends GameComponent{
    public Camera camera;


    public CameraTrackingComponent(Camera cameraIn){
        camera = cameraIn;

    }

    @Override
    public void step(GameObject gameObject, float dt) {
        float signX = Math.signum(gameObject.posX - camera.posX);
        float signY = Math.signum(gameObject.posY - camera.posY);

        float deltaX = Math.abs(gameObject.posX - camera.posX) - .3f;
        float deltaY = Math.abs(gameObject.posY - camera.posY) - .3f;
        deltaY = signY*Math.max(0, deltaY);
        deltaX = signX*Math.max(0, deltaX);
        camera.velX = 5*(deltaX);
        camera.velY = 5*(deltaY);

        camera.lookX = camera.posX;
        camera.lookY = camera.posY;
//        camera.lookZ = gameObject.posZ;


    }
}
