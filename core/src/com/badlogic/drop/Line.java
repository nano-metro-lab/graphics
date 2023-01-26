package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;

import java.util.ArrayList;
import java.util.List;

public class Line {
    static World world;
    public List<Station> stationList;
    public Color lineColor;
    public CatmullRomSpline<Vector2> track;
    private Vector2 controlPoint;

    public Line(Color lineColor) { // Todo take args?
        this.lineColor = lineColor;
        this.world = Drop.world;
        this.stationList = new ArrayList<Station>(10);

    }

    public void push(Location location) {
        this.add(location, this.stationList.size());
    }

    public void add(Location location, int index) {
        Station s = new Station(location);
        if (index > this.stationList.size()) {
            return;
        } else {
            this.stationList.add(index, s);
        }
        if (this.stationList.size() < 2) return;
        calculateTrack();
    }

    public void remove(Location location) {
        Station stationToRemove = null;
        for (Station s : this.stationList) {
            if (s.location == location) {
                stationToRemove = s;
            }
        }
        this.stationList.remove(stationToRemove);
        if (this.stationList.size() < 2) return;
        calculateTrack();
    }

    // Todo head? middle?
    public void setControlPoint(float x, float y) {
        this.controlPoint = new Vector2(x, y);
        this.calculateTrack();
    }

    public void removeControlPoint() {
        this.controlPoint = null;
    }


    public void addStation(Location location, Station previousStation) {
        Station s = new Station(location);
        if (!this.stationList.contains(previousStation)) {
            return;
        }
        this.stationList.add(this.stationList.indexOf(previousStation) + 1, s);
        if (this.stationList.size() < 2) return;
        calculateTrack();
    }

    private void addConnection(Station station) {
        if (stationList.size() < 2) return;
        Station previousStation = stationList.get(stationList.size() - 2);
//        stationList.add(station);
        addJoint(previousStation, station);
    }

    public CatmullRomSpline<Vector2> getTrack() {
        return this.track;
    }

    int k = 100;
    Vector2[] cachedPoints = new Vector2[k];
    private void calculateTrack() {
        if (this.controlPoint == null) {
            Vector2[] controlPoints = new Vector2[this.stationList.size() + 2];
            controlPoints[0] = this.stationList.get(0).location.getPosition();
            controlPoints[controlPoints.length - 1] = this.stationList.get(this.stationList.size() - 1).location.getPosition();

            for (int i = 0; i < this.stationList.size() ; i++) {
                // Todo should recognize control point
                controlPoints[i + 1] = this.stationList.get(i).location.getPosition();
            }

            this.track = new CatmullRomSpline<>(controlPoints, false);
            for (int i = 0; i < k; i++) {
                cachedPoints[i] = new Vector2();
                this.track.valueAt(cachedPoints[i], ((float)i)/((float)k-1));
            }
            // Cache when created, path will be used by train travel also
        } else {
            Vector2[] controlPoints = new Vector2[this.stationList.size() + 3];
            controlPoints[0] = this.stationList.get(0).location.getPosition();
            controlPoints[controlPoints.length - 1] = this.controlPoint;
            controlPoints[controlPoints.length - 2] = this.controlPoint;

            for (int i = 0; i < this.stationList.size() ; i++) {
                // Todo should recognize control point
                controlPoints[i + 1] = this.stationList.get(i).location.getPosition();
            }

            this.track = new CatmullRomSpline<>(controlPoints, false);
            for (int i = 0; i < k; i++) {
                cachedPoints[i] = new Vector2();
                this.track.valueAt(cachedPoints[i], ((float)i)/((float)k-1));
            }
        }


    }

    // Todo 'Drawable' interface
    public void draw(ShapeRenderer shape) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glLineWidth(25);
        if (stationList.size() < 2) return;
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(this.lineColor);
        for (int i = 0; i < k - 1; i++) {
            shape.line(cachedPoints[i], cachedPoints[i+1]);
        }
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }



    private void addJoint(Station previousStation, Station station) {
        PrismaticJointDef pDef = new PrismaticJointDef();
        pDef.bodyA = previousStation.stationBody;
        pDef.bodyB = station.stationBody;
        world.createJoint(pDef);
    }

    public void popStation() {
        if (stationList.isEmpty()) {
            this.world = null;
            this.stationList = null;
        } else {
            world.destroyBody(this.stationList.get(this.stationList.size() - 1).stationBody);
        }
    }

    public void removeStation() {

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
