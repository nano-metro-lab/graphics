package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Train {
    static World world = Drop.world;
    private Body trainBody;
    private Line ofLine;

    public Train(Line line) {
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
        this.ofLine = line;
    }

//    private CatmullRomSpline<Vector2> getTrack() {
//        return this.ofLine.getTrack();
//    }

    private float runTime = 0f;
    private Vector2 trainTargetPosition = new Vector2();

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
