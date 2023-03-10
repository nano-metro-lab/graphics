package ucc.team9.nanometro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class NanoMetro extends Game {
    SpriteBatch batch;
    ShapeRenderer shape;
    Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        debugRenderer = new Box2DDebugRenderer();

        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        shape.dispose();
        debugRenderer.dispose();
    }
}
