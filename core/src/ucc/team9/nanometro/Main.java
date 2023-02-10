/*
This is the main entry point.
It names Main, because this project uses libGDX's Main tutorial project as the basis.
 */



package ucc.team9.nanometro;

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

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private ShapeRenderer shape;
	public static OrthographicCamera camera;
	private Viewport viewport;
	private Box2DDebugRenderer debugRenderer;
	private Body MouseBox;

	public static World world = new World(new Vector2(0, 0), false); // non-gravity Todo
	private Line testLine;
	public static List<Line> lineList = new ArrayList<Line>(5);
	static List<Train> trainList = new ArrayList<Train>(5);
	static List<Location> locationList = new ArrayList<>(10);

	public Main() {
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
//		setInputProcessor();
//		new MouseInputProcessor();
		Gdx.input.setInputProcessor(new MouseInputProcessor());
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
		// Camera 40 x 40

		Location l1 = new Location(4, 20, Location.LocationType.CIRCLE);
		Location l2 = new Location(12, 20, Location.LocationType.CIRCLE);
		Location l3 = new Location(20, 20, Location.LocationType.CIRCLE);
		Location l4 = new Location(28, 20, Location.LocationType.CIRCLE);
		Location l5 = new Location(36, 20, Location.LocationType.TRIANGLE);
		Location l6 = new Location(12, 12, Location.LocationType.CIRCLE);
		Location l7 = new Location(12, 4, Location.LocationType.TRIANGLE);
		Location l8 = new Location(20, 12, Location.LocationType.TRIANGLE);

		locationList = List.of(l1, l2, l3, l4, l5, l6, l7, l8);

		Line line1 = new Line(l1, l2);
		line1.addTail(l3);
		line1.addTail(l4);
		line1.addTail(l5);
//		line1.removeTail();
//		line1.removeMiddle(l4);
		this.lineList.add(line1);
		this.trainList.add(new Train(line1, line1.sectionList.get(0), 0f));

		Line line2 = new Line(l2, l6);
		line2.addTail(l7);
		this.lineList.add(line2);
		this.trainList.add(new Train(line2, line2.sectionList.get(0), 0f));

		Line line3 = new Line(l3, l8);
		this.trainList.add(new Train(line3, line3.sectionList.get(0), 0f));

		shape.setProjectionMatrix(camera.combined);

	}

	private Train testTrain;
	private Section testSection;

	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
//		ScreenUtils.clear(0, 0, 0.2f, 1);
//		ScreenUtils.clear(Color.valueOf("#002B4AFF"));
		ScreenUtils.clear(Color.WHITE);
//		batch.setProjectionMatrix(camera.combined);
		// tell the camera to update its matrices.
		camera.update();
		for (Line line : lineList) {
			line.draw(shape);
		}
		debugRenderer.render(world, camera.combined);
		for (Train train : trainList) {
//			Gdx.gl.glLineWidth(5);
			train.run();
			train.draw(batch);
		}
		for (Location l : locationList) {
			l.draw(batch);
		}


		world.step(1/60f, 6, 2);
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		batch.dispose();
		shape.dispose();
	}
}
