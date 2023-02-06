package com.badlogic.drop;

public class Passenger {
    private Location.LocationType type; // destination type
    private void Passenger(Location.LocationType type) {
        this.type = type;
    }

    public Location.LocationType getType() {
        return this.type;
    }
}
