package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceView;

import com.example.myapplication.material.Material;
import com.example.myapplication.mesh.Camera;
import com.example.myapplication.mesh.PlaneMesh;

public class GameActivity extends AppCompatActivity {
    static public PlaneMesh planeMesh;
static public Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Material material = new Material(this, R.drawable.dots);

        planeMesh = new PlaneMesh(material);
        camera = new Camera();

        GameView gameView = findViewById(R.id.GameView);

//        GameView gameView = new GameView(GameActivity.this, s); //findViewById(R.id.surfaceView)




    }
}