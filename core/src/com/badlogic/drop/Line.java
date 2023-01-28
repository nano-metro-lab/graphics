package com.badlogic.drop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private final List<Station> stationList;
    private final List<Track> trackList;
    private final List<TrackPreview> trackPreviewList;
//    public Color lineColor;
    private static final World world = Drop.world;

    public Line() {
        this.trackList = new ArrayList<Track>(20);
        this.trackPreviewList = new ArrayList<TrackPreview>(5);
        this.stationList = new ArrayList<Station>(21);
    }

    public void updateTracks() {
        if (this.stationList == null) return;
        for (Track i : this.trackList) {
            i.update();
        }
    }

    public Track getTrack(Location locationA, Location locationB) {
        List<Station> t = new ArrayList<Station>(2);
        for (Station j : this.stationList) {
            if (j.getLocation() == locationA || j.getLocation() == locationB) {
                t.add(j);
            }
        }
        for (Track i : this.trackList) {
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

        for (Track s : this.trackList) {
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
        this.stationList.add(endStation);
        if (this.stationList.size() == 1) {
            return;
        } else if (this.stationList.size() == 2) {
            Station startStation = this.stationList.get(this.stationList.size()-2);
            this.trackList.add(new Track(startStation, endStation));
        } else {
            Station startStation = this.stationList.get(this.stationList.size()-2);
            Track previousTrack = null;
            for (Track i : this.trackList) {
                if (i.hasStation(startStation)) {
                    previousTrack = i;
                }
            }
            if (previousTrack == null) System.out.println("Errrror");
            this.trackList.add(new Track(startStation, endStation, previousTrack));
        }
        this.updateTracks();
    }

    public void removeTail() {
        Station station = this.stationList.get(this.stationList.size()-1);
        Track track = null;
        for (Track s : this.trackList) {
            if (s.hasStation(station)) {
                track = s;
            }
        }
        if (track == null) System.out.println("errrr");
        track.destroy();
//        station.destory();
        this.stationList.remove(station);
        this.trackList.remove(track);
    }

    public void addHead(Location location) {
        if (this.stationList.size() >= 2) {
            Station startStation = this.stationList.get(0);
            Station endStation = new Station(location);
            Track previousTrack = null;
            for (Track i : this.trackList) {
                if (i.hasStation(startStation)) {
                    previousTrack = i;
                }
            }
            if (previousTrack == null) System.out.println("Errrror");
            this.trackList.add(new Track(startStation, endStation, previousTrack));
        } else {
            System.out.println("should not happen");
        }
    }
    public void addMiddle(Location location, Track existingTrack) {
        Station middleStation = new Station(location);
        Station stationA = existingTrack.getStartStation();
        Station stationB = existingTrack.getEndStation();
        int index = this.stationList.indexOf(stationA) > this.stationList.indexOf(stationB) ?
                this.stationList.indexOf(stationB) + 1 : this.stationList.indexOf(stationA) + 1;
        this.stationList.add(index, middleStation);
        // gen control points
        Vector2[] middleControlPoints = this.getMiddleControlPoints(stationA.getPosition(),
                stationB.getPosition(), middleStation.getPosition());
        Vector2 controlPointA = middleControlPoints[0];
        Vector2 controlPointB = middleControlPoints[1];
        if (stationA == this.stationList.get(0) || stationA == this.stationList.get(this.stationList.size()-1)) {
            this.trackList.add(new Track(middleStation, stationA, controlPointA));
        } else {
            this.trackList.add(new Track(stationA, middleStation,
                    existingTrack.getControlPoint(stationA), controlPointA));
        }

        if (stationB == this.stationList.get(0) || stationB == this.stationList.get(this.stationList.size()-1)) {
            this.trackList.add(new Track(middleStation, stationB, controlPointB));
        } else {
            this.trackList.add(new Track(stationB, middleStation,
                    existingTrack.getControlPoint(stationB), controlPointB));
        }

//
        existingTrack.destroy();
        this.trackList.remove(existingTrack);
        this.updateTracks();
    }

    public void removeMiddle(Station station) {

        List<Object> t = new ArrayList<>(4);
        for (Track s : this.trackList) {
            if (s.hasStation(station)) {
                t.add(s.getOppositeStation(station));
                t.add(s.getControlPoint(s.getOppositeStation(station)));
                s.destroy();
                this.trackList.remove(s);
            }
        }
        this.stationList.remove(station);
        this.trackList.add(new Track(
                (Station)t.get(0), (Station)t.get(1), (Vector2)t.get(1), (Vector2)t.get(3)));

    }



    private Vector2[] getMiddleControlPoints(Vector2 vA, Vector2 vB, Vector2 vC) {
        Vector2 offsetA = vC.cpy().sub(vB);
        Vector2 controlPointA = vA.cpy().add(offsetA);
        Vector2 offsetB = vC.cpy().sub(vA);
        Vector2 controlPointB = vB.cpy().add(offsetB);
        float controlDistance = Track.controlDistance;
        float distance = controlDistance / (float)Math.hypot(vA.x - vB.x, vA.y - vB.y);
        controlPointA = vC.cpy().lerp(controlPointA, distance);
        controlPointB = vC.cpy().lerp(controlPointB, distance);
        return new Vector2[] {controlPointA, controlPointB};
    }





}
