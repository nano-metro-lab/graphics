package ucc.team9.nanometro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class NanoMetro extends Game {
    SpriteBatch batch;
    ShapeRenderer shape;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        shape.dispose();
    }
}
