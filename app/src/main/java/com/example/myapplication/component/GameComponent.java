package com.example.myapplication.component;

import com.example.myapplication.object.GameObject;

public abstract class GameComponent {
    public abstract void step(GameObject gameObject, float dt);
}
