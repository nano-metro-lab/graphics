package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class Station {
    static World world = Drop.world;
    public Body stationBody;
    public Location location;
    private Vector2 position;
    private Vector2 controlPoint;
    public boolean isTrackStarter = false;


    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getControlPoint() {
        return controlPoint;
    }
    public void setControlPoint(Vector2 controlPoint) {
        this.controlPoint = controlPoint;
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
        this.position = location.getPosition();
        this.controlPoint = null;
    }


    public void destroy() {
        // Todo
    }

    public void setStation(Vector2 position) {
        stationBody.setTransform(position.x, position.y, 0);
    }


    @Override
    public String toString() {
        return "No." + this.hashCode();
    }


}
