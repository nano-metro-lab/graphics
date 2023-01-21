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
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private World world;
	private Box2DDebugRenderer debugRenderer;

	private Body MouseBox;

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

		world = new World(new Vector2(0, 0), true); // Todo
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				System.out.println("111");
			}

			@Override
			public void endContact(Contact contact) {
				System.out.println("222");
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
		batch = new SpriteBatch();

		createBox();
	}

	private void createBox() {
		BodyDef station1Def = new BodyDef();
		BodyDef station2Def = new BodyDef();
		BodyDef station3Def = new BodyDef();

		station1Def.position.set(new Vector2(50, 300));
		station2Def.position.set(new Vector2(350, 200));
		station3Def.position.set(new Vector2(700, 400));

		Body station1 = world.createBody(station1Def);
		Body station2 = world.createBody(station2Def);
		Body station3 = world.createBody(station3Def);

		PolygonShape generalBox = new PolygonShape();

		generalBox.setAsBox(20f, 20f);

		station1.createFixture(generalBox, 0.0f);
		station2.createFixture(generalBox, 0.0f);
		station3.createFixture(generalBox, 0.0f);

		station1.setTransform(new Vector2(60, 60), 0);


		BodyDef mouseBoxDef = new BodyDef();
		mouseBoxDef.position.set(new Vector2(0,0));
		MouseBox = world.createBody(mouseBoxDef);
		MouseBox.createFixture(generalBox, 0.0f);

		generalBox.dispose();


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
		batch.dispose();
	}
}
