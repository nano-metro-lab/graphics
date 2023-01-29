package com.badlogic.drop;

import com.badlogic.gdx.math.Vector2;

public class SectionPreview extends Section {
    public SectionPreview(Station startStation, Station endStation, Section previousSection) {
        super(startStation, endStation, previousSection);
    }

    public SectionPreview(Station startStation, Station endStation, Vector2 p1, Vector2 p2) {
        super(startStation, endStation, p1, p2);
    }

    public SectionPreview(Station startStation, Station endStation, Vector2 p1) {
        super(startStation, endStation, p1);
    }

    public SectionPreview(Station startStation, Station endStation) {
        super(startStation, endStation);
    }

    @Override
    public void update() {
        super.generateBezier();
        super.generateSamples();
    }
}
