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
    public void apply(float dt) {
        float signX = Math.signum(gameObject.posX - camera.posX);
        float signY = Math.signum(gameObject.posY - camera.posY);

        float deltaX = Math.abs(gameObject.posX - camera.posX) - .4f;
        float deltaY = Math.abs(gameObject.posY - camera.posY) - .4f;
        deltaY = signY*Math.max(0, deltaY);
        deltaX = signX*Math.max(0, deltaX);
        camera.velX = 10*(deltaX);
        camera.velY = 10*(deltaY);

        camera.lookX = camera.posX;
        camera.lookY = camera.posY;


    }
}
