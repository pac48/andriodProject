package com.example.myapplication.object;
import android.content.Context;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.component.GravityComponent;
import com.example.myapplication.geometry.PlanGeometry;
import com.example.myapplication.material.Material;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Tank extends Plane{
    float angleVel;

    public Tank(Context context){
        super(new Material(context, R.drawable.tank));
        posZ = -2;
        scaleX = .2f;
        scaleY = .2f;
        scaleZ = .2f;
        angleVel = 0;

    }

    @Override
    public void setVelX(float vel) {
        velX = vel;
//        setTankAngle();
    }
    @Override
    public void setVelY(float vel) {
        velY = vel;
        setTankAngle();
    }


    public void setTankAngle(){
        float newAngle = (float) ((float) 180*Math.atan2(velY, velX)/Math.PI) + 90;
        float offset;
        if (newAngle - angle > 180){ // the negative direction is shorter
            newAngle -= 360;
            offset = Math.signum(newAngle-angle)*Math.min(Math.abs(newAngle-angle), 10);
        } else{
            offset = Math.signum(newAngle - angle)*Math.min(Math.abs(newAngle-angle), 10);
        }

        angleVel = .5f*offset + .5f*angleVel;
        angle = (angle + angleVel) % 360;

        float mag = (float) Math.sqrt(velX*velX+velY*velY);
        velX = (float) (mag*Math.cos(Math.PI*(angle-90)/180));
        velY = (float) (mag*Math.sin(Math.PI*(angle-90)/180));

    }
}
