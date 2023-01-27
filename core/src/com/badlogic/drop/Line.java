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
    static World world;
    public List<Station> stationList;

    private List<Object> graph;
    public Color lineColor;
    public CatmullRomSpline<Vector2> track;
    private Vector2 controlPoint;

    public Line(Location location, Color lineColor) { // Todo take args?
        this.lineColor = lineColor;
        this.world = Drop.world;
        Station s = new Station(location);
        this.graph.add(s);
    }
    private float controlDistance = 1f;
    public void addTail(Location location) {
        if (this.graph.size() == 1) {
            Station station = new Station(location);
            Station tailStation = (Station) this.graph.get(this.graph.size()-1);
            Vector2 p0 = tailStation.getPosition();
            Vector2 p3 = station.getPosition();
            float distance = controlDistance / (float)Math.hypot(p0.x - p3.x, p0.y - p3.y);
            Vector2 p2 = p3.cpy().lerp(p0, distance);
            Vector2 p1 = p0.cpy().lerp(p3, distance);
            Section section = new Section(p0, p1, p2, p3);
            this.graph.add(section);
            this.graph.add(station);
        } else {
            Station tailStation = (Station) this.graph.get(this.graph.size()-1);
            Section tailSection = (Section) this.graph.get(this.graph.size()-2);
            Station station = new Station(location);
            Vector2 p0 = tailStation.getPosition();
            Vector2 p1 = tailSection.getP2Mirror();
            Vector2 p3 = station.getPosition();
            float distance = controlDistance / (float)Math.hypot(p0.x - p3.x, p0.y - p3.y);
            Vector2 p2 = p3.cpy().lerp(p0, distance);
            Section section = new Section(p0, p1, p2, p3);
            this.graph.add(section);
            this.graph.add(station);
        }
    }

    public void draw() {
        for (Object obj : this.graph) {
            if (obj instanceof Station) {
                (Station)obj.draw();
            } else {
                (Section)obj.draw();
            }
        }
    }


}
