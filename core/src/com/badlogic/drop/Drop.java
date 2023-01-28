/*
This is the main entry point.
It names Drop, because this project uses libGDX's Drop tutorial project as the basis.
 */



package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class Drop extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer shape;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Box2DDebugRenderer debugRenderer;
	private Body MouseBox;

	static World world = new World(new Vector2(0, 0), false); // non-gravity Todo
	private Line testLine;
	private List<Line> lineList = new ArrayList<Line>(5);
	private List<Train> trainList = new ArrayList<Train>(5);

	public Drop() {
	}

	@Override
	public void create() {

		// Todo Input event listener

		// Todo Contact listener
		debugRenderer = new Box2DDebugRenderer();
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 40, 40);
		viewport = new FillViewport(900, 900, camera);
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		setInputProcessor();
		createBox();

	}

	private void setInputProcessor() {
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
				Vector3 mousePosition = new Vector3(x, y, 0);
				camera.unproject(mousePosition);
//				viewport.
				// Todo
				return true;
			}

			@Override
			public boolean touchUp(int x, int y, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int x, int y, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int x, int y) {
				Vector3 mousePosition = new Vector3(x, y, 0);
				camera.unproject(mousePosition);
				return true;
			}


			@Override
			public boolean scrolled(float amountX, float amountY) {
				return false;
			}
		});

	}
	private void createBox() {
		// Todo should wrap station and assign uuid to each obj
		// Todo should implement simple BUS, to broadcast events to other components

		Location l1 = new Location(12, 9, Location.LocationType.SQUARE);
		Location l2 = new Location(29, 20, Location.LocationType.CIRCLE);
		Location l3 = new Location(20, 30, Location.LocationType.TRIANGLE);
		Location l4 = new Location(29, 9, Location.LocationType.CIRCLE);
		Location l5 = new Location(12, 35, Location.LocationType.CIRCLE);
		Location l6 = new Location(5, 9, Location.LocationType.CIRCLE);
		Location l7 = new Location(32, 30, Location.LocationType.CIRCLE);

		Line line1 = new Line();
		line1.addTail(l5);
		line1.addTail(l3);
		line1.addTail(l2);
		line1.addTail(l4);

		this.lineList.add(line1);

		Line line2 = new Line();
		line2.addTail(l6);
//		line2.addTail(l1);
		line2.addTail(l2);
		line2.addTail(l7);
		line2.addMiddle(l1, line2.getSection(l6, l2));


		this.lineList.add(line2);



		shape.setProjectionMatrix(camera.combined);

	}

	private Train testTrain;


	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
//		ScreenUtils.clear(0, 0, 0.2f, 1);
//		ScreenUtils.clear(Color.valueOf("#002B4AFF"));
		ScreenUtils.clear(Color.WHITE);

		// tell the camera to update its matrices.
		camera.update();
		// shape renderer
		for (Line line : lineList) {
			line.draw(shape);
		}

		// libgdx
		debugRenderer.render(world, camera.combined);
//		for (Train train : trainList) {
//			Gdx.gl.glLineWidth(5);
//			train.run();
//		}
		world.step(1/60f, 6, 2);
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		batch.dispose();
		shape.dispose();
	}
}
