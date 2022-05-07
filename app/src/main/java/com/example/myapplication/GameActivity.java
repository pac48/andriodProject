package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.myapplication.component.JoyControllerComponent;
import com.example.myapplication.component.RigidBodyComponent;
import com.example.myapplication.component.SpriteAnimatorComponent;
import com.example.myapplication.component.CameraTrackingComponent;
import com.example.myapplication.material.Material;
import com.example.myapplication.object.Camera;
import com.example.myapplication.object.Cube;
import com.example.myapplication.object.GameObject;
import com.example.myapplication.object.Grass;
import com.example.myapplication.object.Plane;
import com.example.myapplication.object.Road;
import com.example.myapplication.object.Tank;
import com.example.myapplication.object.Tank3D;
import com.example.myapplication.view.GameView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;










public class GameActivity extends AppCompatActivity {
    public static AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);



        assetManager = this.getAssets();

        Scene scene = new Scene();
        Camera camera = new Camera();

        GameObject tank1 = makeTank();
        GameObject guy = makeGuy();
        GameObject background = makeBackground();
        GameObject road = makeRoad();
        GameObject tank3D = makeTank3D();
        tank3D.addComponent(new CameraTrackingComponent(camera));


        scene.addObject(tank3D);
        scene.addObject(background);
        scene.addObject(road);
        int size = 60;
        Material materialCube = new Material(this, R.drawable.rock);

        for (float y = 0; y < size+1; y+=size) {
            for (float x = 0; x < size+1; x++) {
                GameObject cube = makeCube(.6f*(x  - size/2),.6f* (y- size/2), materialCube);
                scene.addObject(cube);
            }
        }
        for (float x = 0; x < size+1; x+=size) {
            for (float y = 0; y < size+1; y++) {
                GameObject cube = makeCube(.6f*(x - size/2), .6f*(y- size/2), materialCube);
                scene.addObject(cube);
            }
        }

        double boost = 0;
        for (float x = 0; x < size+1; x++) {
            for (float y = 0; y < size+1; y++) {
                if (Math.random() + boost< .99){
                    boost = 0;
                    continue;
                }
                boost = .8;
                GameObject cube = makeCube(.6f*(x - size/2), .6f*(y- size/2), materialCube);
                scene.addObject(cube);
            }
        }



        scene.addCamera(camera);

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
        Material material = new Material(this, R.drawable.grass);
        GameObject background = new Grass(material);
        background.posZ = -1.1f;
        background.scaleX = 80f;
        background.scaleY = 80f;
        background.scaleZ = .0001f;
        background.addComponent(new RigidBodyComponent(0, PhysicsManager.CollisionShape.BOX));

        return background;
    }

    private GameObject makeRoad(){
        Material material = new Material(this, R.drawable.rock);
        GameObject road = new Road(material);
        road.posZ = -1;
        road.scaleX = .5f;
        road.scaleY = .5f;
        road.scaleZ = .01f;
//        road.addComponent(new RigidBodyComponent(0, PhysicsManager.CollisionShape.BOX));

        return road;
    }

    private GameObject makeCube(float x, float y, Material materialCube){
        GameObject cube = new Cube(materialCube);
        cube.posZ = -1;
        cube.posY = y;
        cube.posX = x;
        cube.scaleX = .3f;
        cube.scaleY = .3f;
        cube.scaleZ = .3f;
        cube.addComponent(new RigidBodyComponent(0, PhysicsManager.CollisionShape.BOX));

        return cube;
    }
    private GameObject makeTank3D(){
        Material material = new Material(this, R.drawable.camo);
        GameObject tank3D = new Tank3D(material);
        tank3D.posZ = -.9f;
        tank3D.posY += .2f;
        tank3D.posX -= .4f;
        tank3D.scaleX = .1f;
        tank3D.scaleY = .1f;
        tank3D.scaleZ = .1f;
        tank3D.addComponent(new JoyControllerComponent(findViewById(R.id.JoyView), 15.0f));
        tank3D.addComponent(new RigidBodyComponent(100, PhysicsManager.CollisionShape.BOX, .2f, .3f, .1f));

        return tank3D;
    }
}