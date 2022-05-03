package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.myapplication.component.GravityComponent;
import com.example.myapplication.component.SpriteAnimatorComponent;
import com.example.myapplication.material.Material;
import com.example.myapplication.object.Camera;
import com.example.myapplication.object.GameObject;
import com.example.myapplication.object.Plane;
import com.example.myapplication.view.GameView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        assetManager = this.getAssets();

        Scene scene = new Scene();

        GameObject tank1 = makeTank();
        GameObject tank2 = makeGuy();
        GameObject background = makeBackground();
        tank2.posY += .4;
        tank2.posX += .2;
        tank2.posZ -= .5;
        ((GravityComponent) tank2.gameComponents.get(0)).xDir = .2f;


        scene.addObject(tank1);
        scene.addObject(tank2);
        scene.addObject(background);

        scene.addCamera(new Camera());

        GameView gameView = findViewById(R.id.GameView);
        gameView.setScene(scene);
        gameView.setControlView(findViewById(R.id.JoyView));

    }

    private GameObject makeGuy(){

        String[] files;
        InputStream istr;
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        try {
            files = getAssets().list("guy");
            for (String file : files){
                istr = assetManager.open("guy/"+file);
                Bitmap bitmap = BitmapFactory.decodeStream(istr);
                bitmaps.add(bitmap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Material material = new Material(this, bitmaps);
        GameObject tank = new Plane(material);
        tank.posZ = -2;
        tank.scaleX = .6f;
        tank.scaleY = .6f;
        tank.scaleZ = .6f;
        tank.addComponent(new GravityComponent(0.001f,0,-1,0));
        tank.addComponent(new SpriteAnimatorComponent(60.0f));

        return tank;
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