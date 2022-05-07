package com.example.myapplication.component;

import com.example.myapplication.object.GameObject;

public abstract class GameComponent {
    public GameObject gameObject;
    public abstract void apply(float dt);

    public void postAddComponent() {

    }
}
