package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class Station {
    static World world = Drop.world;
    public Body stationBody;
    private Location location;
    public Location getLocation() {
        return location;
    }
    public Vector2 getPosition() {
        return this.location.getPosition();
    }

    public Station(Location location) {
        BodyDef stationBodyDef = new BodyDef();
        this.stationBody = world.createBody(stationBodyDef);
        CircleShape stationShape = new CircleShape();
        stationShape.setRadius(2f);
        this.stationBody.createFixture(stationShape, 0.0f);
        setStation(location.getPosition());
        stationShape.dispose();
        this.location = location;
        this.stationBody.setUserData(this);
    }


    public void destroy() {
        world.destroyBody(this.stationBody);
    }

    public void setStation(Vector2 position) {
        stationBody.setTransform(position.x, position.y, 0);
    }


    @Override
    public String toString() {
        return "No." + this.hashCode();
    }


}
