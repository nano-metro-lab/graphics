package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import static com.badlogic.drop.Drop.world;



public class Sensor {
    private Body sensorBody;
    private Vector2 sensorPosition;

    public Sensor(Vector2 v) {
        BodyDef sensorBodyDef = new BodyDef();
        sensorBodyDef.type = BodyDef.BodyType.StaticBody;
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(0.5f);
        this.sensorBody = world.createBody(sensorBodyDef);
        this.sensorBody.createFixture(sensorShape, 0.0f);
        this.sensorBody.setTransform(v.x, v.y, 0);
        this.sensorBody.setUserData(this);
        sensorShape.dispose();
        this.sensorPosition = v;
    }

    public Vector2 getSensorPosition() {
        return this.sensorPosition;
    }

    public void destroy() {
        world.destroyBody(this.sensorBody);
    }

}
