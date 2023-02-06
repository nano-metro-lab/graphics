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

    List<Passenger> PassengerList = new ArrayList<>();

    // train motion control
    private Section currentSection = null;
    private float currentPercentage = 0;
    private boolean currentDirection = false; // 0 normal, 1 reverse
    private boolean currentSectionFinish = false;

    public Train(Line line) {
        this.setUpBody();
        this.line = line;
        this.updateRoute();
        this.trainBody.setUserData(this);
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

    public void run() {
        this.run(this.line.getFirstSection(), 0, false);
    }

    public void run(Section s, float p, boolean d) {
        if (this.currentSection == null) {
            this.currentSection = s;
        }

        if (this.currentSectionFinish) {
            this.resetTrain();
            if ((!this.currentDirection && this.line.getNextSection(this.currentSection) == null) || (this.currentDirection && this.line.getPreviousSection(this.currentSection) == null)) {
                this.currentDirection = !this.currentDirection;
            } else {
                this.currentSection = this.currentDirection?
                        this.line.getPreviousSection(this.currentSection) : this.line.getNextSection(this.currentSection);
            }

        }
        this.runSection(this.currentSection);
    }

    public void runSection(Section s) {
        float sectionTimeLimit = this.timeLimit * s.getLength();
        if (runTime < sectionTimeLimit) { // tuned
            runTime += Gdx.graphics.getDeltaTime();
            float f = runTime / sectionTimeLimit;
            Vector2 bodyPosition = this.trainBody.getWorldCenter();
            Bezier<Vector2> track = s.getBezierPath();
            if (s.reverse ^ this.currentDirection) {
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
}
