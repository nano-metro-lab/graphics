package com.badlogic.drop;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class TrackPreview extends Track{
    public TrackPreview(Station startStation, Station endStation, Track previousTrack) {
        super(startStation, endStation, previousTrack);
    }

    public TrackPreview(Station startStation, Station endStation, Vector2 p1, Vector2 p2) {
        super(startStation, endStation, p1, p2);
    }

    public TrackPreview(Station startStation, Station endStation, Vector2 p1) {
        super(startStation, endStation, p1);
    }

    public TrackPreview(Station startStation, Station endStation) {
        super(startStation, endStation);
    }

    @Override
    public void update() {
        super.generateBezier();
        super.generateSamples();
    }
}
