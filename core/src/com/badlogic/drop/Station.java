package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;


public class Station {

    List<Passenger> PassengerList = new ArrayList<>(6);
    List<Section> SectionList = new ArrayList<>(2);

    Section upper;
    Section lower;



    private Location location;
    Line line;
    public Location getLocation() {
        return location;
    }
    public Vector2 getPosition() {
        return this.location.getPosition();
    }

    public Station(Line line, Location location) {
        setStation(location.getPosition());
        this.location = location;
        this.line = line;
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
