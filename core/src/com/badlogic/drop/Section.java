package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;

public class Section {
    private Vector2[] controlPoints = new Vector2[4];

    public Section(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3) {
        this.controlPoints[0] = p0;
        this.controlPoints[1] = p1;
        this.controlPoints[2] = p2;
        this.controlPoints[3] = p3;
    }

    public Vector2 getP1Mirror() {
        return (this.controlPoints[0].cpy().sub(this.controlPoints[1]).add(this.controlPoints[0]));
    }

    public Vector2 getP2Mirror() {
        return (this.controlPoints[3].cpy().sub(this.controlPoints[2]).add(this.controlPoints[3]));
    }


    // test
    public static void main (String[] arg) {
        Section test = new Section(new Vector2(0, 0), new Vector2(-1, -1), new Vector2(0, 0), new Vector2(1, 1));
        System.out.println(test.getP1Mirror().x);
        System.out.println(test.getP1Mirror().y);
        System.out.println(test.getP2Mirror().x);
        System.out.println(test.getP2Mirror().y);
    }

}
