package com.example.myapplication.component;

import com.example.myapplication.object.GameObject;
import com.example.myapplication.view.JoyView;


public class JoyControllerComponent extends GameComponent{
    public JoyView joyView;
    public float speed = 1;

    public JoyControllerComponent(){

    }

    public JoyControllerComponent(JoyView joyViewIn, float speedIn){
        joyView = joyViewIn;
        speed = speedIn;

    }

    @Override
    public void step(GameObject gameObject, float dt) {
        if (joyView.pressed){
            gameObject.setVelX(joyView.getAxisX()*speed);
            gameObject.setVelY(-joyView.getAxisY()*speed);
        } else{
            gameObject.setVelX(gameObject.velX - .1f*gameObject.velX);
            gameObject.setVelY(gameObject.velY -.1f*gameObject.velY);
        }


    }
}
