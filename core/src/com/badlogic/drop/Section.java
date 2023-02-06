package com.badlogic.drop;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public boolean reverse = false;
    static float controlDistance = 3.0f;
    private Station startStation;
    private Vector2 startStationControlPoint;
    private Station endStation;
    private Vector2 endStationControlPoint;

    private Vector2 startStationPoint;
    private Vector2 endStationPoint;

    public Bezier<Vector2> getBezierPath() {
        return bezierPath;
    }

    private Bezier<Vector2> bezierPath;

    public Vector2[] getPathSamples() {
        return pathSamples;
    }

    private Vector2 pathSamples[];
    private int sampleRate = 0;
    private List<Sensor> sensorList = new ArrayList<>(1000);
    private static final World world = Drop.world;

    public Vector2 getControlPoint(Station station) {
        if (this.startStation == station) {
            return this.startStationControlPoint;
        } else {
            return this.endStationControlPoint;
        }
    }

    public boolean hasStation(Station station) {
        if (this.startStation == station || this.endStation == station) {
            return true;
        } else return false;
    }


    public Station getOppositeStation (Station station) {
        if (this.startStation == station) {
            return this.endStation;
        }
        else if (this.endStation == station) {
            return this.startStation;
        } else {
            return null;
        }
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
    public Section(Station startStation, Station endStation, Vector2 p1, Vector2 p2) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.startStationControlPoint = p1;
        this.endStationControlPoint = p2;
        this.startStationPoint = this.startStation.getPosition();
        this.endStationPoint = this.endStation.getPosition();
    }
    public Section(Station startStation, Station endStation, Vector2 p1) {
        this.startStation = startStation;
        this.endStation = endStation;

        this.startStationPoint = this.startStation.getPosition();
        this.endStationPoint = this.endStation.getPosition();

        this.startStationControlPoint = p1;
        float distance = controlDistance / (float)Math.hypot(this.startStationPoint.x - this.endStationPoint.x,
                this.startStationPoint.y - this.endStationPoint.y);
        this.endStationControlPoint = this.endStationPoint.cpy().lerp(this.startStationPoint, distance);

    }
    public Section(Station startStation, Station endStation) { // Todo
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

    public void update() {
        this.generateBezier();
        this.generateSamples();
        this.generateSensors();
    }

    void generateBezier() { // Todo maybe not needed
        this.bezierPath = new Bezier<>(this.startStationPoint, this.startStationControlPoint,
                this.endStationControlPoint, this.endStationPoint);
    }

    void generateSamples() {
        this.sampleRate = (int) (this.getLength() / 0.5f); // Todo need tune
        this.pathSamples = new Vector2[this.sampleRate];
        for (int i = 0; i < this.sampleRate; i++) {
            this.pathSamples[i] = new Vector2();
            this.bezierPath.valueAt(this.pathSamples[i], ((float)i)/((float)this.sampleRate-1));
        }
    }

    void generateSensors() {
        for (Vector2 v : this.pathSamples) {
            this.sensorList.add(new Sensor(this, v));
        }
    }

    public void destroy() {
        // destroy sensors
        if (this.sensorList != null) {
            for (Sensor i : this.sensorList) {
                i.destroy();
            }
            this.sensorList = null;
        }
    }




    public float getLength() {
        return this.bezierPath.approxLength(100);
    }

    Vector2 getStartStationOppositeControlPoints() {
        return (this.startStationPoint.cpy().sub(this.startStationControlPoint).add(this.startStationPoint));
    }
    Vector2 getEndStationOppositeControlPoints() {
        return (this.endStationPoint.cpy().sub(this.endStationControlPoint).add(this.endStationPoint));
    }

}