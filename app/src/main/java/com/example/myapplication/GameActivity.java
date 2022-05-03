package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.myapplication.component.GravityComponent;
import com.example.myapplication.component.JoyControllerComponent;
import com.example.myapplication.component.SpriteAnimatorComponent;
import com.example.myapplication.material.Material;
import com.example.myapplication.object.Camera;
import com.example.myapplication.object.GameObject;
import com.example.myapplication.object.Plane;
import com.example.myapplication.object.Tank;
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
        GameObject guy = makeGuy();
        GameObject background = makeBackground();
        guy.posY += .4;
        guy.posX += .2;
        guy.posZ -= .5;



        scene.addObject(tank1);
        scene.addObject(guy);
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
        GameObject guy = new Plane(material);
        guy.posZ = -2;
        guy.scaleX = .6f;
        guy.scaleY = .6f;
        guy.scaleZ = .6f;
        guy.addComponent(new JoyControllerComponent(findViewById(R.id.JoyView), 10.0f));

        guy.addComponent(new SpriteAnimatorComponent(60.0f));

        return guy;
    }

    private GameObject makeTank(){
        Tank tank = new Tank(this);
//        tank.addComponent(new GravityComponent(.5f,0,-1,0));
        tank.addComponent(new JoyControllerComponent(findViewById(R.id.JoyView), 5.0f));

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