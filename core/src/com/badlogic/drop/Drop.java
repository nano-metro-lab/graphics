package com.badlogic.drop;
import java.util.Iterator;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.InputProcessor;

public class Drop extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Box2DDebugRenderer debugRenderer;
	private Body MouseBox;

	// connection joint array
	private Array<Joint> connectionJoint;
	static World world = new World(new Vector2(0, 0), false); // non-gravity Todo
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
		batch = new SpriteBatch();

		createBox();

	}

	private boolean testCreateJoint = false;

	private void createBox() {
		// Todo should wrap station and assign uuid to each obj
		// Todo should implement simple BUS, to broadcast events to other components

		Location l1 = new Location(12, 9, Location.LocationType.SQUARE);
		Location l2 = new Location(29, 20, Location.LocationType.CIRCLE);
		Location l3 = new Location(20, 30, Location.LocationType.TRIANGLE);

//
//		Station station1 = new Station(l1);
//		Station station2 = new Station(l2);
//		Station station3 = new Station(l3);


		Line line1 = new Line();
		line1.addStation(l1);
		line1.addStation(l2);
		line1.addStation(l3);
		System.out.println(line1.stationList);

//		line1.removeConnection(station2, station3);
//		System.out.println(line1.stationList);
//		line1.removeConnection(station1, station2);
//		System.out.println(line1.stationList);


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
