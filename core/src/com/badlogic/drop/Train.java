package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class Train {
    public enum Direction {
        UP, DOWN
    }
    static World world = Drop.world;
    Body trainBody;
    List<Passenger> PassengerList = new ArrayList<>();
    Line line;
    Section section;
    int stopSignal;
    Direction direction;
    float progress; // 0 - 1
    final float stdTimeLimit = 0.1f;
    float runTime = 0f;

    public Train(Line l, Section s, float p) {
        this.setUpBody();
        this.line = l;
        this.section = s;
        this.stopSignal = 0;
        this.progress = p;
        this.direction = Direction.DOWN; // default go down
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
        this.trainBody.setUserData(this);
    }

//    private void resetTrain() {
//        this.currentPercentage = 0;
//        this.runTime = 0;
//        this.currentSectionFinish = false;
//    }

//    public void run() {
//        this.run(this.line.getFirstSection(), 0, false);
//    }

    public void set() {

    }

    public void dumbController() {
        runTime = 0f;
        if (line.getNextSection(section) == null && direction == Direction.DOWN) {
            section = section;
            direction = Direction.UP;
            progress = 0f;
        } else if (line.getPreviousSection(section) == null && direction == Direction.UP) {
            section = section;
            direction = Direction.DOWN;
            progress = 0f;
        } else {
            if (direction == Direction.DOWN) {
                section = line.getNextSection(section);
                progress = 0f;
            } else {
                section = line.getPreviousSection(section);
                progress = 0f;
            }
        }
    }

    public void run() {
        if (progress > 1f) {
            if (stopSignal != 0) {
                // ============== train stop at station ==============
//                Vector2 bodyPosition = trainBody.getWorldCenter();
//                Vector2 positionDelta = null;
//                if (direction == Direction.DOWN) {
//                    positionDelta = section.lower.getPosition().cpy().sub(bodyPosition);
//                } else {
//                    positionDelta = section.upper.getPosition().cpy().sub(bodyPosition);
//                }
//                this.trainBody.setLinearVelocity(positionDelta.scl(10));
//
            } else {
                // ============== train go to next section ==============
                dumbController();
            }
        } else {
            float sectionTimeLimit = stdTimeLimit * section.path.approxLength();

            runTime += Gdx.graphics.getDeltaTime();
            progress = runTime / sectionTimeLimit;
            Vector2 bodyPosition = trainBody.getWorldCenter();
            Vector2 targetPosition = new Vector2();
            if (this.direction == Direction.DOWN) {
                section.path.valueAt(targetPosition, progress);
            } else {
                section.path.valueAt(targetPosition, 1 - progress);
            }
            Vector2 positionDelta = (targetPosition.cpy().sub(bodyPosition));
            this.trainBody.setLinearVelocity(positionDelta.scl(10));

        }
    }

//    public void run(Section s, float p, boolean d) {
//        if (this.currentSection == null) {
//            this.currentSection = s;
//        }
//
//        if (this.currentSectionFinish) {
//            this.resetTrain();
//            if ((!this.currentDirection && this.line.getNextSection(this.currentSection) == null) || (this.currentDirection && this.line.getPreviousSection(this.currentSection) == null)) {
//                this.currentDirection = !this.currentDirection;
//            } else {
//                this.currentSection = this.currentDirection?
//                        this.line.getPreviousSection(this.currentSection) : this.line.getNextSection(this.currentSection);
//            }
//
//        }
//        this.runSection(this.currentSection);
//    }

//    public void runSection(Section s) {
//        float sectionTimeLimit = this.timeLimit * s.getLength();
//        if (runTime < sectionTimeLimit) { // tuned
//            runTime += Gdx.graphics.getDeltaTime();
//            float f = runTime / sectionTimeLimit;
//            Vector2 bodyPosition = this.trainBody.getWorldCenter();
//            Bezier<Vector2> track = s.getBezierPath();
//            if (s.reverse ^ this.currentDirection) {
//                track.valueAt(trainTargetPosition, 1 - f);
//            } else {
//                track.valueAt(trainTargetPosition, f);
//            }
//            Vector2 positionDelta = (new Vector2(trainTargetPosition)).sub(bodyPosition);
//            this.trainBody.setLinearVelocity(positionDelta.scl(10));
//        } else {
//            this.currentSectionFinish = true;
//        }
//    }
}
