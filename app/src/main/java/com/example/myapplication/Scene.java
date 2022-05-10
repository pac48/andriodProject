package com.example.myapplication;

import com.example.myapplication.object.Camera;
import com.example.myapplication.object.GameObject;
import com.example.myapplication.object.Light;

import java.util.ArrayList;

public class Scene {
    public ArrayList<GameObject> objects;
    public Camera camera;
    public Light light;

    public Scene(){

        objects = new ArrayList<>();
    }
    public void addObject(GameObject gameObject){
        objects.add(gameObject);

    }
    public void addCamera(Camera cameraIn){
        camera = cameraIn;
        objects.add(camera);

    }

    public void addLight(Light lightIn){
        light = lightIn;
        objects.add(light);
    }

}
