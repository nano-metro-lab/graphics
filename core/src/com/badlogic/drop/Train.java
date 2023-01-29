package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class Train {
    static World world = Drop.world;
    private Body trainBody;
    private Line line;
    private List<Vector2> route;


    // train motion control
    private Section currentSection = null;
    private float currentPercentage = 0;
    private boolean currentDirection = false; // 0 normal, 1 reverse
    private boolean currentSectionFinish = false;

    public Train(Line line) {
        this.setUpBody();
        this.line = line;
        this.updateRoute();
    }

    public void updateRoute() {
//        this.route = this.line.getFullTrackSamplesList();
    }

    private void setUpBody() {
        BodyDef trainBodyDef = new BodyDef();
        trainBodyDef.type = BodyDef.BodyType.DynamicBody;
        this.trainBody = world.createBody(trainBodyDef);
        CircleShape trainShape = new CircleShape();
        trainShape.setRadius(2f);
        FixtureDef trainFixtureDef = new FixtureDef();
        trainFixtureDef.isSensor = true;
        trainFixtureDef.shape = trainShape;
        this.trainBody.createFixture(trainFixtureDef);
        trainShape.dispose();
    }


    private float runTime = 0f;
    final float timeLimit = 0.1f;
    private Vector2 trainTargetPosition = new Vector2();

    private void resetTrain() {
        this.currentPercentage = 0;
        this.runTime = 0;
        this.currentSectionFinish = false;
    }

    public void run(Section s, float p, boolean d) {
        if (this.currentSection == null) {
            this.currentSection = s;
            this.resetTrain();
        } else if (this.currentSectionFinish) {
            this.currentSection = this.line.getNextSection(this.currentSection);
            this.resetTrain();
        } else {
            this.runSection(this.currentSection);
        }
    }

    public void runSection(Section s) {
        float sectionTimeLimit = this.timeLimit * s.getLength();
        if (runTime <= sectionTimeLimit) {
            runTime += Gdx.graphics.getDeltaTime();
            float f = runTime / sectionTimeLimit;
            Vector2 bodyPosition = this.trainBody.getWorldCenter();
            Bezier<Vector2> track = s.getBezierPath();
            if (s.reverse) {
                track.valueAt(trainTargetPosition, 1 - f);
            } else {
                track.valueAt(trainTargetPosition, f);
            }
            Vector2 positionDelta = (new Vector2(trainTargetPosition)).sub(bodyPosition);
            this.trainBody.setLinearVelocity(positionDelta.scl(10));
        } else {
            this.currentSectionFinish = true;
        }
    }



//    public void run() {
//        runTime += Gdx.graphics.getDeltaTime();
//        final float maxRunTime = 4.0f; // Hunt will last for 4 seconds, get a fraction value of this between 0 and 1.
//        float f = runTime / maxRunTime;
//        if (f <= 1.0f) {
//            Vector2 bodyPosition = this.trainBody.getWorldCenter();
//            CatmullRomSpline<Vector2> track = this.getTrack();
//            track.valueAt(trainTargetPosition, f);
//            Vector2 positionDelta = (new Vector2(trainTargetPosition)).sub(bodyPosition);
//            this.trainBody.setLinearVelocity(positionDelta.scl(10));
//        } else if (f <= 2.0f) {
//            Vector2 bodyPosition = this.trainBody.getWorldCenter();
//            CatmullRomSpline<Vector2> track = this.getTrack();
//            track.valueAt(trainTargetPosition, 2.0f - f);
//            Vector2 positionDelta = (new Vector2(trainTargetPosition)).sub(bodyPosition);
//            this.trainBody.setLinearVelocity(positionDelta.scl(10));
//        } else {
//            this.runTime = 0;
//            this.trainBody.setLinearVelocity(0, 0);
//        }
//
//    }


}
