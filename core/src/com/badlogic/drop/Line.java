package com.badlogic.drop;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;

import java.util.ArrayList;
import java.util.List;

public class Line {
    static World world = Drop.world;

    public List<Station> stationList = new ArrayList<Station>(10);

    public void addStation(Location location) {
        Station s = new Station(location);
        this.stationList.add(s);
        addConnection(s);
    }

    private void addConnection(Station station) {
        if (stationList.size() < 2) return;
        Station previousStation = stationList.get(stationList.size() - 2);
        stationList.add(station);
        addJoint(previousStation, station);

    }

    private void addJoint(Station previousStation, Station station) {
        PrismaticJointDef pDef = new PrismaticJointDef();
        pDef.bodyA = previousStation.stationBody;
        pDef.bodyB = station.stationBody;
        world.createJoint(pDef);
    }

    public void removeConnection(Station firstStation, Station secondStation) {
        if (stationList.contains(secondStation)) {
            stationList.remove(secondStation);
        }
        stationList.remove(firstStation);
        if (stationList.isEmpty()) {
            destroySelf();
        }

        // destroy pJoint
//        world.destroyJoint(secondStation.stationBody.getJointList());

    }


    public void destroySelf() {
        world = null;
        stationList = null;
    }

    public void drawSelf() {

    }

    public void drawInside() {

    }

    public void add_train() {

    }

    public void removeTrain() {
    }

    private void innerRemoveTrain() {
        // update train path to next station available

        // all passenger get off

    }

    private void innerRemoveStation() {
        // wait all trains reach next station whose path include this station

        // update station list, notify core component
    }


}
