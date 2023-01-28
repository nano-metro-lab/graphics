package com.badlogic.drop;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import javax.swing.*;
import java.util.List;

public class NewSection {
    private static float controlDistance = 1.0f;

    private Station startStation;
    private Vector2 startStationControlPoint;
    private Station endStation;
    private Vector2 endStationControlPoint;

    private Vector2 startStationPoint;
    private Vector2 endStationPoint;

    private List<Body> sensorList;
    private Bezier<Vector2> bezierPath;
    private Vector2 cachedPoints[];

    private static final World world = Drop.world;


    public NewSection(NewSection previousSection, Station startStation, Station endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
        generateControlPoints(previousSection);
    }

    public void generateControlPoints(NewSection previousSection) {
        this.startStationPoint = this.startStation.getPosition();
        this.endStationPoint = this.endStation.getPosition();

        float distance = controlDistance / (float)Math.hypot(this.startStationPoint.x - this.endStationPoint.x,
                this.startStationPoint.y - this.endStationPoint.y);
        this.endStationControlPoint = this.endStationPoint.cpy().lerp(this.startStationPoint, distance);

        if (this.startStation == previousSection.endStation) {
            this.startStationControlPoint = previousSection.getEndStationOppositeControlPoints();
        } else if (this.startStation == previousSection.startStation) {
            this.startStationControlPoint = previousSection.getStartStationOppositeControlPoints();
        } else {
            System.out.println("Whattttt!? "); //Todo
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

    public void generateCachedPoints() {
        int k = (int) (this.getSectionLength() / 0.05f);
        this.cachedPoints = new Vector2[k];
        for (int i = 0; i < k; i++) {
            this.cachedPoints[i] = new Vector2();
            this.bezierPath.valueAt(this.cachedPoints[i], ((float)i)/((float)k-1));
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
