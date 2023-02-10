package ucc.team9.nanometro.gfx;

import com.badlogic.gdx.math.Vector2;


public class Station {

    Section upper;
    Section lower;
    Location location;
    Line line;


    public Vector2 getPosition() {
        return this.location.getPosition();
    }

    public Station(Line line, Location location) {
        setStation(location.getPosition());
        this.location = location;
        this.line = line;
    }


    public void destroy() {
    }

    public void setStation(Vector2 position) {
//        stationBody.setTransform(position.x, position.y, 0);
    }


    @Override
    public String toString() {
        return "No." + this.hashCode();
    }


}
