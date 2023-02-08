package ucc.team9.nanometro;


/*
Path-like class
 */

import com.badlogic.gdx.math.Vector2;

public class Boxy {

    Vector2 startingPoint;
    Vector2 endingPoint;
    public Boxy(Station upper, Station lower) {
        startingPoint = upper.getPosition();
        endingPoint = lower.getPosition();
    }

    public void valueAt(Vector2 v, float t) {
        v.set(startingPoint.cpy().lerp(endingPoint, t));
    }

    public float approxLength() {
        return (float) Math.sqrt((startingPoint.x - endingPoint.x) * (startingPoint.x - endingPoint.x) +
                (startingPoint.y - endingPoint.y) * (startingPoint.y - endingPoint.y));
    }

//    public static void main(String[] args) {
//        Boxy b = new Boxy(new Station(new Line(), new Location(0, 0, Location.LocationType.SQUARE)), new Station(new Line(), new Location(10, 10, Location.LocationType.SQUARE)));
//        System.out.println(b.approxLength());
//        Vector2 v = new Vector2();
//        b.valueAt(v, 0.5f);
//        System.out.println(v);
//
//    }

}

