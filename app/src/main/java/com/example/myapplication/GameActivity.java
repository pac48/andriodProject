package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.component.GravityComponent;
import com.example.myapplication.material.Material;
import com.example.myapplication.object.Camera;
import com.example.myapplication.object.GameObject;
import com.example.myapplication.object.Plane;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Scene scene = new Scene();

        GameObject tank1 = makeTank();
        GameObject tank2 = makeTank();
        GameObject background = makeBackground();
        tank2.posY += .4;
        tank2.posX += .2;
        tank2.posZ -= .5;
        ((GravityComponent)tank2.gameComponents.get(0)).xDir = .2f;


        scene.addObject(tank1);
        scene.addObject(tank2);
        scene.addObject(background);

        scene.addCamera(new Camera());

        GameView gameView = findViewById(R.id.GameView);
        gameView.setScene(scene);

    }

    private GameObject makeTank(){
        Material material = new Material(this, R.drawable.tank);
        GameObject tank = new Plane(material);
        tank.posZ = -2;
        tank.scaleX = .2f;
        tank.scaleY = .2f;
        tank.scaleZ = .2f;
        tank.addComponent(new GravityComponent(.5f,0,-1,0));
        return tank;
    }
    private GameObject makeBackground(){
        Material material = new Material(this, R.drawable.background);
        GameObject background = new Plane(material);
        background.posZ = -10;
        background.scaleX = 2f;
        background.scaleY = 2f;
        background.scaleZ = 1f;
        return background;
    }
}