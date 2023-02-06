package com.badlogic.drop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Line {
    final List<Station> stationList;
    final List<Section> sectionList;
    private static final World world = Drop.world;


    public Line() {
        this.sectionList = new ArrayList<Section>(20);
        this.stationList = new ArrayList<Station>(21);
    }

    public Section getNextSection(Section s) {
        if (sectionList.indexOf(s) == sectionList.size() - 1) {
            return null;
        } else {
            return sectionList.get(sectionList.indexOf(s) + 1);
        }
    }
    public Section getPreviousSection(Section s) {
        if (sectionList.indexOf(s) == 0) {
            return null;
        } else {
            return sectionList.get(sectionList.indexOf(s) - 1);
        }
    }

    public Section getSection(Location locationA, Location locationB) {
        List<Station> t = new ArrayList<Station>(2);
        for (Station j : this.stationList) {
            if (j.getLocation() == locationA || j.getLocation() == locationB) {
                t.add(j);
            }
        }
        for (Section i : this.sectionList) {
            if (i.hasStation(t.get(0)) && i.hasStation(t.get(1))) {
                return i;
            }
        }
        return null;
    }

    public void draw(ShapeRenderer shape) {
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.gl.glLineWidth(25);
        if (stationList.size() < 2) return;

        for (Section s : this.sectionList) {
            int k = s.getPathSamples().length;
            for (int i = 0; i < k - 1; i++) {
                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(Color.BLUE);
                shape.line(s.getPathSamples()[i], s.getPathSamples()[i+1]);
                shape.end();
            }


        }
//        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void addTail(Location l) {
        Station s = new Station(this, l);
        Station f = stationList.get(stationList.size() - 1);
        stationList.add(s);
        sectionList.add(new Section(this, f, s));
    }

    public void removeTail() {
        if (sectionList.size() == 1) {
            destroy();
        } else {
            stationList.get(stationList.size() - 1).destroy();
            stationList.remove(stationList.size() - 1);
            sectionList.get(sectionList.size() - 1).destroy();
            sectionList.remove(sectionList.size() - 1);
        }
    }
    public void addHead(Location l) {
        Station s = new Station(this, l);
        Station f = stationList.get(stationList.size() - 1);
        stationList.add(0, s);
        sectionList.add(0, new Section(this, s, f));
    }

    public void removeHead() {
        if (sectionList.size() == 1) {
            this.destroy();
        } else {
            stationList.get(0).destroy();
            stationList.remove(0);
            sectionList.get(0).destroy();
            sectionList.remove(0);
        }
    }

    public void addMiddle(Section s, Location l) {
        Station aUpper = s.upper;
        Station middle = new Station(this, l);
        Station bLower = s.lower;
        stationList.add(stationList.indexOf(aUpper) + 1, middle);
        sectionList.add(sectionList.indexOf(s), new Section(this, aUpper, middle));
        sectionList.add(sectionList.indexOf(s), new Section(this, middle, bLower));
        sectionList.remove(s);
        s.destroy();
    }

    public void removeMiddle(Station s) {
        Section a = null;
        Section b = null;
        for (Section i : sectionList) {
            if (i.upper == s) {
                b = i;
            } else if (i.lower == s) {
                a = i;
            }
        }
        stationList.remove(s);
        s.destroy();
        sectionList.add(sectionList.indexOf(a), new Section(this, a.upper, b.lower));
        sectionList.remove(a);
        sectionList.remove(b);
        a.destroy();
        b.destroy();
    }


    public void destroy() {
        for (Station s : stationList) {
            s.destroy();
        }
        stationList.removeAll(stationList);
        for (Section s : sectionList) {
            s.destroy();
        }
        sectionList.removeAll(sectionList);
    }



    public Station getStation (Location l) {
        for (Station s : this.stationList) {
            if (s.getLocation() == l) return s;
//            else return null;
        }
        return null;
    }

    public boolean hasSection(Section s) {
        return this.sectionList.contains(s);
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
