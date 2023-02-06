package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;


public class Station {

    List<Passenger> PassengerList = new ArrayList<>();

    private Location location;
    public Location getLocation() {
        return location;
    }
    public Vector2 getPosition() {
        return this.location.getPosition();
    }

    public Station(Location location) {
        setStation(location.getPosition());
        this.location = location;
    }


    public void destroy() {
    }

    public void setStation(Vector2 position) {
//        stationBody.setTransform(position.x, position.y, 0);
    }


    @Override
    public String toString() {
        return "No." + this.hashCode();
    }


}
