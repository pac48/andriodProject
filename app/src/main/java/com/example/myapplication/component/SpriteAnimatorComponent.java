package com.example.myapplication.component;

import com.example.myapplication.object.GameObject;

public class SpriteAnimatorComponent extends GameComponent{
    public float playSpeed;
    public float time = 0;

    public SpriteAnimatorComponent(float playSpeedIn){
        playSpeed = playSpeedIn;
    }

    @Override
    public void apply(float dt) {
        time += playSpeed*dt;
        gameObject.material.animationFrame = ((int) time) % gameObject.material.bitmaps.size();
    }
}
