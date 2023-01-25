package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Station {
    static World world = Drop.world;
    public Body stationBody;


    public Station(Location location) {
        BodyDef stationBodyDef = new BodyDef();
        this.stationBody = world.createBody(stationBodyDef);
        CircleShape stationShape = new CircleShape();
        stationShape.setRadius(2f);
        this.stationBody.createFixture(stationShape, 0.0f);
        setStation(location.getPosition());
        stationShape.dispose();
    }

    public void setStation(Vector2 position) {
        stationBody.setTransform(position.x, position.y, 0);
    }


    @Override
    public String toString() {
        return "No." + this.hashCode();
    }


}
