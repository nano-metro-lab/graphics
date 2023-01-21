package com.badlogic.drop;
import java.util.Iterator;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.InputProcessor;


public class Drop extends ApplicationAdapter {
//	private SpriteBatch batch;
	private OrthographicCamera camera;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private Body MouseBox;

	// connection joint array
	private Array<Joint> connectionJoint;

	public Drop() {
	}

	@Override
	public void create() {
		// Input event listener
		Gdx.input.setInputProcessor(new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int x, int y, int pointer, int button) {
				MouseBox.setTransform(new Vector2(x, y * -1 + 480), 0);
				System.out.println("down");
				return true;
			}

			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {
				MouseBox.setTransform(new Vector2(x, y * -1 + 480), 0);
				System.out.println("up");
				return true;
			}

			@Override
			public boolean touchDragged(int x, int y, int pointer) {
				MouseBox.setTransform(new Vector2(x, y * -1 + 480), 0);
				return true;
			}

			@Override
			public boolean mouseMoved(int x, int y) {
				MouseBox.setTransform(new Vector2(x, y * -1 + 480), 0);
				return true;
			}


			@Override
			public boolean scrolled(float amountX, float amountY) {
				return false;
			}
		});

		world = new World(new Vector2(0, 0), false); // non-gravity Todo
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				System.out.println("I'm in");
			}

			@Override
			public void endContact(Contact contact) {
				System.out.println("I'm out");
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				System.out.println("333");
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				System.out.println("444");
			}
		}); // Collision listener

		debugRenderer = new Box2DDebugRenderer();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
//		batch = new SpriteBatch();

		createBox();
	}

	private void dragConnection() {

	} // mouse clicked on body

	private void createBox() {
		BodyDef stationDef = new BodyDef();
		Body station1 = world.createBody(stationDef);
		Body station2 = world.createBody(stationDef);
		Body station3 = world.createBody(stationDef);
		station1.setTransform(50, 300, 0);
		station2.setTransform(350, 200, 0);
		station3.setTransform(700, 400, 0);
		PolygonShape generalBox = new PolygonShape();
		generalBox.setAsBox(20f, 20f);
		station1.createFixture(generalBox, 0.0f);
		station2.createFixture(generalBox, 0.0f);
		station3.createFixture(generalBox, 0.0f);

		BodyDef mouseBoxDef = new BodyDef();
		mouseBoxDef.position.set(new Vector2(0,0));
		mouseBoxDef.type = BodyDef.BodyType.DynamicBody;
		MouseBox = world.createBody(mouseBoxDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(25f);

		FixtureDef MouseBoxFixtureDef = new FixtureDef();
		MouseBoxFixtureDef.isSensor = true;
		MouseBoxFixtureDef.shape = circle;


		MouseBox.createFixture(MouseBoxFixtureDef);


		generalBox.dispose();
		circle.dispose();


	}






	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		ScreenUtils.clear(0, 0, 0.2f, 1);

		// tell the camera to update its matrices.
		camera.update();


		debugRenderer.render(world, camera.combined);

		world.step(1/60f, 6, 2);
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
//		batch.dispose();
	}
}
