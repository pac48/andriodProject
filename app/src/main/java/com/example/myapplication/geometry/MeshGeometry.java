package com.example.myapplication.geometry;

import com.example.myapplication.GameActivity;
import com.example.myapplication.utils.WavefrontLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class MeshGeometry {
    public final float[] positionData;
    public final float[] colorData;
    public final float[] normalData;
    public final float[] textureCoordinateData;
    public int numVerts;


    public MeshGeometry(String meshFilePath){


        WavefrontLoader wavefrontLoader = new WavefrontLoader(meshFilePath);
        InputStream istr = null;
        try {
            istr = GameActivity.assetManager.open(meshFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        wavefrontLoader.analyzeModel(istr);
        try {
            istr.close();
            istr = GameActivity.assetManager.open(meshFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        wavefrontLoader.allocateBuffers();
        wavefrontLoader.loadModel(istr);

        IntBuffer facesVertIdxs = wavefrontLoader.getFaces().facesVertIdxs;
        ArrayList<int[]> facesNormIdxs = wavefrontLoader.getFaces().facesNormIdxs;
        ArrayList<int[]> facesTexIdxs = wavefrontLoader.getFaces().facesTexIdxs;

        numVerts = facesVertIdxs.capacity();
        positionData = new float[numVerts*3];
        colorData = new float[numVerts*3];
        normalData = new float[numVerts*3];
        textureCoordinateData = new float[numVerts*2];

        FloatBuffer verts = wavefrontLoader.getVerts();
        FloatBuffer normals = wavefrontLoader.getNormals();
        ArrayList<WavefrontLoader.Tuple3> textCoords = wavefrontLoader.getTexCoords();


        int i = 0;
        for (int j=0; j < numVerts; j++){
            for (int k = 0; k < 3; k++) {
                positionData[i] = verts.get(3*facesVertIdxs.get(j)+k);
                i++;
            }
        }

        i = 0;
        for (int j = 0; j < facesNormIdxs.size(); j++){
            for (int k = 0; k < 3; k++){
                for (int m = 0; m < 3; m++) {
                    normalData[i] = normals.get(3*facesNormIdxs.get(j)[k] + m);
                    colorData[i] = 1.0f;
                    i++;
                }
            }

        }

        i = 0;
        for (int j=0; j < facesTexIdxs.size(); j++){
            for (int k = 0; k < 3; k++) {
                textureCoordinateData[i] = textCoords.get(facesTexIdxs.get(j)[k]).getX();
                i++;
                textureCoordinateData[i] = textCoords.get(facesTexIdxs.get(j)[k]).getY();
                i++;
            }

        }


    }


}
