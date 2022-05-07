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
    public void apply(float dt) {
        if (joyView.pressed){
            gameObject.setVelX(joyView.getAxisX()*speed);
            gameObject.setVelY(-joyView.getAxisY()*speed);
        } else{
            gameObject.setVelX(0);
            gameObject.setVelY(0);
        }


    }
}
