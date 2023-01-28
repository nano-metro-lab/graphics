package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import jdk.incubator.vector.VectorOperators;

import java.util.ArrayList;
import java.util.List;

public class Line {
    public List<Station> stationList;
    private List<Section> sectionlist;
//    public Color lineColor;
    public CatmullRomSpline<Vector2> track;
    private Vector2 controlPoint;
    private static final World world = Drop.world;

    public Line() {

    }


    public void addTail(Location location) {
        Station endStation = new Station(location);
        this.stationList.add(endStation);
        if (this.stationList.size() == 1) {
            return;
        } else if (this.stationList.size() == 2) {
            Station startStation = this.stationList.get(this.stationList.size()-2);
            this.sectionlist.add(new Section(startStation, endStation));
        } else {
            Station startStation = this.stationList.get(this.stationList.size()-2);
            Section previousSection = null;
            for (Section i : this.sectionlist) {
                if (i.getStartStation() == startStation || i.getEndStation() == startStation) {
                    previousSection = i;
                }
            }
            if (previousSection == null) System.out.println("Errrror");
            this.sectionlist.add(new Section(startStation, endStation, previousSection));
        }
    }

    public void addHead(Location location) {
        if (this.stationList.size() >= 2) {
            Station startStation = this.stationList.get(0);
            Station endStation = new Station(location);
            Section previousSection = null;
            for (Section i : this.sectionlist) {
                if (i.getStartStation() == startStation || i.getEndStation() == startStation) {
                    previousSection = i;
                }
            }
            if (previousSection == null) System.out.println("Errrror");
            this.sectionlist.add(new Section(startStation, endStation, previousSection));
        } else {
            System.out.println("should not happen");
        }
    }
    public void addMiddle(Location location, Section existingSection) {
        Station middleStation = new Station(location);
        Station stationA = existingSection.getStartStation();
        Station stationB = existingSection.getEndStation();
        int index = this.stationList.indexOf(stationA) > this.stationList.indexOf(stationB) ?
                this.stationList.indexOf(stationB) : this.stationList.indexOf(stationA);
        this.stationList.add(index, middleStation);
        // gen control points
        Vector2[] middleControlPoints = this.getMiddleControlPoints(stationA.getPosition(),
                stationB.getPosition(), middleStation.getPosition());
        Vector2 controlPointA = middleControlPoints[0];
        Vector2 controlPointB = middleControlPoints[1];
        this.sectionlist.add(new Section(stationA, middleStation,
                existingSection.getControlPoint(stationA), controlPointA));
        this.sectionlist.add(new Section(stationB, middleStation,
                existingSection.getControlPoint(stationB), controlPointB));
        this.sectionlist.remove(existingSection);
    }

    public void removeMiddle(Station station) {

        List<Object> t = new ArrayList<>(4);
        for (Section s : this.sectionlist) {
            if (s.hasStation(station)) {
                t.add(s.getOppositeStation(station));
                t.add(s.getControlPoint(s.getOppositeStation(station)));
                s.destroy();
                this.sectionlist.remove(s);
            }
        }
        this.stationList.remove(station);
        this.sectionlist.add(new Section(
                (Station)t.get(0), (Station)t.get(1), (Vector2)t.get(1), (Vector2)t.get(3)));

    }



    private Vector2[] getMiddleControlPoints(Vector2 vA, Vector2 vB, Vector2 vC) {
        Vector2 offsetA = vC.cpy().sub(vB);
        Vector2 controlPointA = vA.cpy().add(offsetA);
        Vector2 offsetB = vC.cpy().sub(vA);
        Vector2 controlPointB = vB.cpy().add(offsetB);
        float controlDistance = Section.controlDistance;
        float distance = controlDistance / (float)Math.hypot(vA.x - vB.x, vA.y - vB.y);
        controlPointA = vC.cpy().lerp(controlPointA, distance);
        controlPointB = vC.cpy().lerp(controlPointB, distance);
        return new Vector2[] {controlPointA, controlPointB};
    }



}
