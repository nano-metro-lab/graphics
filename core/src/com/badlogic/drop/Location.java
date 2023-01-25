package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;

public class Location {

    public enum LocationType {
        CIRCLE, TRIANGLE, SQUARE
    }
    private Vector2 position;
    private LocationType type;

    public Location(float x, float y, LocationType type) {
        this.position = new Vector2(x, y);
        this.type = type;
    }

    public Vector2 getPosition() {
        return position;
    }
}

