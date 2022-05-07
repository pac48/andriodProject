package com.example.myapplication;

import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.math.Vector3;
import com.example.myapplication.component.RigidBodyComponent;
import com.example.myapplication.object.GameObject;
import java.util.ArrayList;


public class PhysicsManager {
    private static PhysicsManager instance;
    public btDiscreteDynamicsWorld dynamicsWorld;
    public ArrayList<RigidBodyComponent> rigidBodyComponent;

    public PhysicsManager(){
        Bullet.init();
        rigidBodyComponent = new ArrayList<>();

        ///collision configuration contains default setup for memory, collision setup. Advanced users can create their own configuration.
        btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
        ///use the default collision dispatcher. For parallel processing you can use a diffent dispatcher (see Extras/BulletMultiThreaded)
        btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfiguration);
        ///btDbvtBroadphase is a good general purpose broadphase. You can also try out btAxis3Sweep.
        btBroadphaseInterface overlappingPairCache = new btDbvtBroadphase();
        ///the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
        btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver, collisionConfiguration);

        dynamicsWorld.setGravity(new Vector3(0, 0, -10));


    }

    public static PhysicsManager getInstance(){
        if (instance == null){
            instance = new PhysicsManager();
        }
        return instance;
    }

    public void step(float dt, ArrayList<GameObject> objects){

        for (GameObject gameObject : objects) {
            gameObject.physicsComponent.preStep(dt);
        }

        dynamicsWorld.stepSimulation(dt,10);

        for (GameObject gameObject : objects) {
                gameObject.physicsComponent.step(dt);
        }

        for (GameObject gameObject : objects){
            gameObject.postSimPhysics(dt);
        }

    }

    public enum CollisionShape {
        BOX, CONVEX
    }
}
