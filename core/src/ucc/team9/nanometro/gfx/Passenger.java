package ucc.team9.nanometro.gfx;

public class Passenger {
    private Location.LocationType type; // destination type
    public Passenger(Location.LocationType type) {
        this.type = type;
    }

    public Location.LocationType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public void destroy() {
        return;
    }
}
