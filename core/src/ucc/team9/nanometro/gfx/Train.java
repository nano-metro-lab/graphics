package ucc.team9.nanometro.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import ucc.team9.nanometro.Main;

import java.util.ArrayList;
import java.util.List;

public class Train {
    public enum Direction {
        UP, DOWN
    }
    static World world = Main.world;
    Body trainBody;
    List<Passenger> passengerList = new ArrayList<>();
    Line line;
    Section section;
    int stopSignal;
    Direction direction;
    float progress; // 0 - 1
    final float stdTimeLimit = 0.1f;
    float runTime = 0f;
    BitmapFont debugFont;

    public void addPassenger(Passenger p) {
        this.passengerList.add(p);
    }

    public void removePassenger(Passenger p) {
        this.passengerList.remove(p);
    }

    public void stopTrain() {
        stopSignal = 1;
    }

    public void startTrain() {
        stopSignal = 0;
    }


    public Train(Line l, Section s, float p) {
        this.setUpBody();
        this.line = l;
        this.section = s;
        this.stopSignal = 0;
        this.progress = p;
        this.direction = Direction.DOWN; // default go down

        // debug font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        this.debugFont = font;

        //
        passengerList.add(new Passenger(Location.LocationType.SQUARE));
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

    public void draw(SpriteBatch batch) {
        batch.begin();

        Vector3 p = new Vector3(this.trainBody.getWorldCenter().x, this.trainBody.getWorldCenter().y, 0);
        Main.camera.project(p);
        debugFont.draw(batch, passengerList.toString(), p.x,p.y);

        batch.end();
    }

    public void set() {

    }

    public void dumbController() {
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                startTrain();
//            }
//        }, 3);
//
//        stopTrain();

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
                Vector2 bodyPosition = trainBody.getWorldCenter();
                Vector2 positionDelta = null;
                if (direction == Direction.DOWN) {
                    positionDelta = section.lower.getPosition().cpy().sub(bodyPosition);
                } else {
                    positionDelta = section.upper.getPosition().cpy().sub(bodyPosition);
                }
                this.trainBody.setLinearVelocity(positionDelta.scl(10));
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
}
