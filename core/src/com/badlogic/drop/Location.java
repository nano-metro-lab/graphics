package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import static com.badlogic.drop.Drop.world;

public class Location {

    public enum LocationType {
        CIRCLE, TRIANGLE, SQUARE, PREVIEW
    }
    private Vector2 position;
    private Body locationBody;


    public LocationType getType() {
        return type;
    }

    private LocationType type;

    public Location(float x, float y, LocationType type) {
        this.position = new Vector2(x, y);
        this.type = type;

        BodyDef locationBodyDef = new BodyDef();
        this.locationBody = world.createBody(locationBodyDef);
        CircleShape locationShape = new CircleShape();
        locationShape.setRadius(2f);
        this.locationBody.createFixture(locationShape, 0.0f);
        locationShape.dispose();
        this.locationBody.setUserData(this);
        this.locationBody.setTransform(position.x, position.y, 0);
    }

    public void destroy() {
        world.destroyBody(this.locationBody);
    }

    public Vector2 getPosition() {
        return position;
    }
}

