package ucc.team9.nanometro.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import ucc.team9.nanometro.GameScreen;

import static ucc.team9.nanometro.GameScreen.camera;
import static ucc.team9.nanometro.GameScreen.world;
import static ucc.team9.nanometro.GameScreen.modelService;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Location {
    public enum LocationType implements ucc.team9.nanometro.model.shared.LocationType {
        CIRCLE, TRIANGLE, SQUARE, PREVIEW
    }

    public Hashtable<LocationType, String> imgs = new Hashtable<>();

    public LocationType getType() {
        return this.type;
    }

    public Vector2 requestPlatform() {
        if (this.platformPool.isEmpty()) {
            return null;
        }
        Vector2 v = this.platformPool.get(0);
        this.platformPool.remove(v);
        return v;
    }


    private List<Vector2> platformPool = new ArrayList<>();
    private Vector2 position;
    Body locationBody;
    BitmapFont debugFont;
    List<Passenger> passengerList = new ArrayList<>(30);
    LocationType type;
    Texture locationImage;

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
        this.locationBody = world.createBody(locationBodyDef);
        CircleShape locationShape = new CircleShape();
        locationShape.setRadius(2f);
        this.locationBody.createFixture(locationShape, 0.0f);
        locationShape.dispose();
        this.locationBody.setUserData(this);
        this.locationBody.setTransform(position.x, position.y, 0);

        Vector2 platformOffset = new Vector2(0f, 0.6f);
        this.platformPool.add(this.position);
        this.platformPool.add(this.position.cpy().add(platformOffset));
        this.platformPool.add(this.position.cpy().add(platformOffset).add(platformOffset));


        // debug font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        this.debugFont = font;

        //
        imgs.put(LocationType.CIRCLE, "./CIRCLE.png");
        imgs.put(LocationType.TRIANGLE, "./TRIANGLE.png");
        imgs.put(LocationType.SQUARE, "./SQUARE.png");
        ///

        locationImage = new Texture(Gdx.files.internal(imgs.get(this.type)));
//        locationImage.

    }

    public void drawDebug(SpriteBatch batch) {
        batch.begin();
        Vector3 p = new Vector3(this.locationBody.getWorldCenter().x, this.locationBody.getWorldCenter().y, 0);
        camera.project(p);
        debugFont.draw(batch, type.toString() + passengerList.toString(), p.x,p.y);
        batch.end();
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.draw(locationImage, this.position.x, this.position.y);
        batch.end();
    }

    public void destroy() {world.destroyBody(this.locationBody);
    }

    public Vector2 getPosition() {
        return position;
    }
}

