package com.badlogic.drop;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

public class Section {
    private static float controlDistance = 1.0f;


    private Station startStation;


    private Vector2 startStationControlPoint;
    private Station endStation;
    private Vector2 endStationControlPoint;

    private Vector2 startStationPoint;
    private Vector2 endStationPoint;

    private List<Body> sensorList;
    private Bezier<Vector2> bezierPath;
    private Vector2 pathSamples[];

    private static final World world = Drop.world;
    public Vector2 getStartStationControlPoint() {
        return startStationControlPoint;
    }

    public Vector2 getEndStationControlPoint() {
        return endStationControlPoint;
    }

    public Vector2 getStartStationPoint() {
        return startStationPoint;
    }

    public Vector2 getEndStationPoint() {
        return endStationPoint;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }


    public Section(Station startStation, Station endStation, Section previousSection) {
        this.startStation = startStation;
        this.endStation = endStation;
        generateControlPoints(previousSection);
    }
    public Section(Station startStation, Station endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
        generateControlPoints();
    }

    public void generateControlPoints() {
        this.startStationPoint = this.startStation.getPosition();
        this.endStationPoint = this.endStation.getPosition();

        float distance = controlDistance / (float)Math.hypot(this.startStationPoint.x - this.endStationPoint.x,
                this.startStationPoint.y - this.endStationPoint.y);
        this.endStationControlPoint = this.endStationPoint.cpy().lerp(this.startStationPoint, distance);
        this.startStationControlPoint = this.startStationPoint.cpy().lerp(this.endStationPoint, distance);

    }
    public void generateControlPoints(Section previousSection) {
        this.startStationPoint = this.startStation.getPosition();
        this.endStationPoint = this.endStation.getPosition();

        float distance = controlDistance / (float)Math.hypot(this.startStationPoint.x - this.endStationPoint.x,
                this.startStationPoint.y - this.endStationPoint.y);
        this.endStationControlPoint = this.endStationPoint.cpy().lerp(this.startStationPoint, distance);

        if (this.startStation == previousSection.endStation) {
            this.startStationControlPoint = previousSection.getEndStationOppositeControlPoints();
        } else {
            this.startStationControlPoint = previousSection.getStartStationOppositeControlPoints();
        }
    }


    // self rendering related part

    private Vector2[] getControlPointsArray() {
        return new Vector2[] {this.startStationPoint, this.startStationControlPoint, this.endStationControlPoint, this.endStationPoint};
    }

    public void generateBezier(boolean reverse) { // Todo maybe not needed
        this.bezierPath = new Bezier<>(this.getControlPointsArray());
    }

    private float getSectionLength() {
        return this.bezierPath.approxLength(100);
    }

    public void generateSamples() {
        int k = (int) (this.getSectionLength() / 0.05f);
        this.pathSamples = new Vector2[k];
        for (int i = 0; i < k; i++) {
            this.pathSamples[i] = new Vector2();
            this.bezierPath.valueAt(this.pathSamples[i], ((float)i)/((float)k-1));
        }
    }


    private void generateSensors() {
        // todo
    }

    public void destroySensors() {
        if (this.sensorList != null) {
            for (Body i : this.sensorList) {
                world.destroyBody(i);
            }
            this.sensorList = null;
        }
    }






    public Vector2 getStartStationOppositeControlPoints() {
        return (this.startStationPoint.cpy().sub(this.startStationControlPoint).add(this.startStationPoint));
    }
    public Vector2 getEndStationOppositeControlPoints() {
        return (this.endStationPoint.cpy().sub(this.endStationControlPoint).add(this.endStationPoint));
    }

}
