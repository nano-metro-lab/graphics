package ucc.team9.nanometro.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import ucc.team9.nanometro.Main;

import java.util.ArrayList;
import java.util.List;

public class Location {

    public enum LocationType {
        CIRCLE, TRIANGLE, SQUARE, PREVIEW
    }
    private Vector2 position;
    Body locationBody;
    BitmapFont debugFont;
    List<Passenger> passengerList = new ArrayList<>(30);
    LocationType type;

    public void addPassenger(Passenger p) {
        this.passengerList.add(p);
    }

    public void removePassenger(Passenger p) {
        this.passengerList.remove(p);
    }


    public Location(float x, float y, LocationType type) {
        this.position = new Vector2(x, y);
        this.type = type;

        BodyDef locationBodyDef = new BodyDef();
        this.locationBody = Main.world.createBody(locationBodyDef);
        CircleShape locationShape = new CircleShape();
        locationShape.setRadius(2f);
        this.locationBody.createFixture(locationShape, 0.0f);
        locationShape.dispose();
        this.locationBody.setUserData(this);
        this.locationBody.setTransform(position.x, position.y, 0);


        // debug font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        this.debugFont = font;

    }

    public void draw(SpriteBatch batch) {
        batch.begin();

        Vector3 p = new Vector3(this.locationBody.getWorldCenter().x, this.locationBody.getWorldCenter().y, 0);
        Main.camera.project(p);
        debugFont.draw(batch, type.toString() + passengerList.toString(), p.x,p.y);

        batch.end();
    }

    public void destroy() {
        Main.world.destroyBody(this.locationBody);
    }

    public Vector2 getPosition() {
        return position;
    }
}

