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


    public Line(Location a, Location b) {
        this.sectionList = new ArrayList<Section>(20);
        this.stationList = new ArrayList<Station>(21);
        this.stationList.add(new Station(this, a));
        this.stationList.add(new Station(this, b));
        this.sectionList.add(new Section(this, this.stationList.get(0), this.stationList.get(1)));
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
        for (Section s : sectionList) {
            if ((s.upper.getLocation() == locationA && s.lower.getLocation() == locationB) || (s.upper.getLocation() == locationB && s.lower.getLocation() == locationA)) {
                return s;
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
                s.draw(shape);
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

    public void addMiddle(Location l, Section s) {
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


}
