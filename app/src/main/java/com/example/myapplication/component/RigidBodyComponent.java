package com.example.myapplication.component;

import androidx.constraintlayout.motion.widget.MotionScene;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;
import com.example.myapplication.PhysicsManager;
import com.example.myapplication.object.GameObject;

public class RigidBodyComponent extends PhysicsComponent{
    public btCollisionShape collisionShape;
    public PhysicsManager.CollisionShape type;
    public float mass;
    btRigidBody body;
    Vector3 localInertia;
    Vector3 collisionSize;

    public RigidBodyComponent(float massIn, PhysicsManager.CollisionShape typeIn, float xScale, float yScale, float zScale){
        collisionSize = new Vector3(xScale, yScale, zScale);
        init(massIn, typeIn);
    }

    public RigidBodyComponent(float massIn, PhysicsManager.CollisionShape typeIn){
        init(massIn, typeIn);
    }
    private void init(float massIn, PhysicsManager.CollisionShape typeIn){
        type = typeIn;
        mass = massIn;
        localInertia = new Vector3(0, 0, 0);
        PhysicsManager.getInstance(); // ensure bullet is initialized
    }

    @Override
    public void postAddComponent(){
        // construct shape
//        PhysicsManager.getInstance().bulletObjects.add(gameObject); // ensure bullet is initialized
if (collisionSize == null){
    collisionSize = new Vector3(gameObject.scaleX, gameObject.scaleY, gameObject.scaleZ);
}

        switch (type) {
            case BOX:
                collisionShape = new btBoxShape(collisionSize);
                break;
            case CONVEX:
//                collisionShape = new btConvexHullShape();
                break;

            default:

        }

        collisionShape.calculateLocalInertia(mass, localInertia);
        //using motionstate is optional, it provides interpolation capabilities, and only synchronizes 'active' objects
        btDefaultMotionState myMotionState = new btDefaultMotionState((new Matrix4()).idt());
        btRigidBody.btRigidBodyConstructionInfo rbInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, myMotionState, collisionShape, localInertia);
        body = new btRigidBody(rbInfo);
        PhysicsManager.getInstance().dynamicsWorld.addRigidBody(body);

        setBulletValues();

    }

    private void setBulletValues(){

//        btDefaultMotionState motionState = new btDefaultMotionState(worldTrans);
//        motionState.setWorldTransform(worldTrans);
//        body.setMotionState(motionState);



        Vector3 vel = new Vector3();

        vel.x = gameObject.velX;
        vel.y = gameObject.velY;
        vel.z = gameObject.velZ;

        body.setLinearVelocity(vel);

        Vector3 pos = new Vector3();

        pos.x = gameObject.posX;
        pos.y = gameObject.posY;
        pos.z = gameObject.posZ;

        Matrix4 worldTrans = new Matrix4();
        worldTrans.idt();
        Matrix3 rotM = new Matrix3();
        rotM.setToRotation(new Vector3(gameObject.axisX, gameObject.axisY, gameObject.axisZ), gameObject.angle);
        worldTrans.set(rotM);
        worldTrans.setTranslation(pos);

        body.setWorldTransform(worldTrans);







    }

    @Override
    public void preStep(float dt){
//        Vector3 vel = new Vector3();
//
//        vel.x = gameObject.velX;
//        vel.y = gameObject.velY;
//        vel.z = gameObject.velZ;
//
//        body.setLinearVelocity(vel);
//
//
//        Matrix4 worldTrans = new Matrix4();
//
//        Vector3 pos = new Vector3();
//
//        pos.x = gameObject.posX;
//        pos.y = gameObject.posY;
//        pos.z = gameObject.posZ;
//
//        worldTrans.setTranslation(pos);
//        body.getMotionState().setWorldTransform(worldTrans);
        setBulletValues();


    }

    @Override
    public void step(float dt){
        Matrix4 worldTrans = new Matrix4();
        body.getWorldTransform(worldTrans);

        Vector3 pos = new Vector3();
        worldTrans.getTranslation(pos);

        gameObject.posX = pos.x;
        gameObject.posY = pos.y;
        gameObject.posZ = pos.z;

        Vector3 vel = body.getLinearVelocity();


        gameObject.velX = vel.x;
        gameObject.velY =  vel.y;
        gameObject.velZ =  vel.z;

    }



}
