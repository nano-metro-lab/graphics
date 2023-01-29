package com.badlogic.drop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final List<Station> stationList;
    private final List<Section> sectionList;
    private static final World world = Drop.world;


    public Line() {
        this.sectionList = new ArrayList<Section>(20);
        this.stationList = new ArrayList<Station>(21);
    }

    public Section getFirstSection() {
        if (this.sectionList == null) {
            return null;
        } return this.sectionList.get(0);
    }

    public Section getNextSection(Section currentSection) {
        if (this.stationList.indexOf(currentSection.getStartStation()) < this.stationList.indexOf(currentSection.getEndStation())) {
            Station i = currentSection.getEndStation();
            if (this.stationList.indexOf(i) == this.stationList.size() - 1 ) {
                return null;
            } else {
                Station j = this.stationList.get(this.stationList.indexOf(i) + 1);
                return this.getSection(i.getLocation(), j.getLocation());
            }
        } else {
            Station i = currentSection.getStartStation();
            if (this.stationList.indexOf(i) == this.stationList.size() - 1 ) {
                return null;
            } else {
                Station j = this.stationList.get(this.stationList.indexOf(i) + 1);
                return this.getSection(i.getLocation(), j.getLocation());
            }
        }
    }

    public Section getPreviousSection(Section currentSection) {
        if (this.stationList.indexOf(currentSection.getStartStation()) < this.stationList.indexOf(currentSection.getEndStation())) {
            Station i = currentSection.getStartStation();
            if (this.stationList.indexOf(i) == 0 ) {
                return null;
            } else {
                Station j = this.stationList.get(this.stationList.indexOf(i) - 1);
                return this.getSection(i.getLocation(), j.getLocation());
            }
        } else {
            Station i = currentSection.getEndStation();
            if (this.stationList.indexOf(i) == 0 ) {
                return null;
            } else {
                Station j = this.stationList.get(this.stationList.indexOf(i) - 1);
                return this.getSection(i.getLocation(), j.getLocation());
            }
        }
    }


    public void updateSections() {
        if (this.sectionList.isEmpty()) return;
        for (Section i : this.sectionList) {
            i.update();
        }

        for (Section i : this.sectionList) {
            if (this.stationList.indexOf(i.getStartStation()) < this.stationList.indexOf(i.getEndStation())) {
                i.reverse = false;
            } else {
                i.reverse = true;
            }
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

    public void addTail(Location location) {

        Station endStation = new Station(location);
        if (location.getType() != Location.LocationType.PREVIEW) {
            this.stationList.add(endStation);
        }

        if (this.stationList.size() == 1) {
            return;
        } else if (this.stationList.size() == 2) {
            Station startStation = this.stationList.get(this.stationList.size()-2);
            this.sectionList.add(new Section(startStation, endStation));
        } else {
            Station startStation = this.stationList.get(this.stationList.size()-2);
            Section previousSection = null;
            for (Section i : this.sectionList) {
                if (i.hasStation(startStation)) {
                    previousSection = i;
                }
            }
            if (previousSection == null) System.out.println("Errrror");
            this.sectionList.add(new Section(startStation, endStation, previousSection));
        }
        this.updateSections();
    }

    public void removeTail() {
        Station s1 = this.stationList.get(this.stationList.size()-1);
        Station s2 = this.stationList.get(this.stationList.size()-2);
        Section section = this.getSection(s1.getLocation(), s2.getLocation());
        section.destroy();
        s1.destroy();
        this.stationList.remove(s1);
        this.sectionList.remove(section);
        this.updateSections();
    }

    public void addHead(Location location) {
        if (this.stationList.size() >= 2) {
            Station startStation = this.stationList.get(0);
            Station endStation = new Station(location);
            Section previousSection = null;
            for (Section i : this.sectionList) {
                if (i.hasStation(startStation)) {
                    previousSection = i;
                }
            }
            if (previousSection == null) System.out.println("Errrror");
            this.sectionList.add(new Section(startStation, endStation, previousSection));
        } else {
            System.out.println("should not happen");
        }
    }
    public void addMiddle(Location location, Section existingSection) {
        Station middleStation = new Station(location);
        Station stationA = existingSection.getStartStation();
        Station stationB = existingSection.getEndStation();
        int index = this.stationList.indexOf(stationA) > this.stationList.indexOf(stationB) ?
                this.stationList.indexOf(stationB) + 1 : this.stationList.indexOf(stationA) + 1;
        this.stationList.add(index, middleStation);
        // gen control points
        Vector2[] middleControlPoints = this.getMiddleControlPoints(stationA.getPosition(),
                stationB.getPosition(), middleStation.getPosition());
        Vector2 controlPointA = middleControlPoints[0];
        Vector2 controlPointB = middleControlPoints[1];
        if (stationA == this.stationList.get(0) || stationA == this.stationList.get(this.stationList.size()-1)) {
            this.sectionList.add(new Section(middleStation, stationA, controlPointA));
        } else {
            this.sectionList.add(new Section(stationA, middleStation,
                    existingSection.getControlPoint(stationA), controlPointA));
        }

        if (stationB == this.stationList.get(0) || stationB == this.stationList.get(this.stationList.size()-1)) {
            this.sectionList.add(new Section(middleStation, stationB, controlPointB));
        } else {
            this.sectionList.add(new Section(stationB, middleStation,
                    existingSection.getControlPoint(stationB), controlPointB));
        }

//
        existingSection.destroy();
        this.sectionList.remove(existingSection);
        this.updateSections();
    }

    public void removeMiddle(Station station) {
        Vector2 p1 = null, p2 = null;
        Station a = null, b = null;
        for (Section s : this.sectionList) {
            if (s.hasStation(station)) {
                a = s.getOppositeStation(station);
                p1 = s.getControlPoint(s.getOppositeStation(station));
                s.destroy();
                this.sectionList.remove(s);
                break;
            }
        }

        for (Section s : this.sectionList) {
            if (s.hasStation(station)) {
                b = s.getOppositeStation(station);
                p2 = s.getControlPoint(s.getOppositeStation(station));
                s.destroy();
                this.sectionList.remove(s);
                break;
            }
        }
        station.destroy();
        this.stationList.remove(station);
        this.sectionList.add(new Section(a, b, p1, p2));
        this.updateSections();
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
