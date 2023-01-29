package com.badlogic.drop;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

import java.util.ArrayList;
import java.util.List;

public class MouseInputProcessor implements InputProcessor {

    public MouseInputProcessor(Body mouseBox) {
        super();
        this.mouseBox = mouseBox;
    }

    private Section startSection = null;
    private Location endLocation = null;

    public MouseInputProcessor() {

    }

    private Body mouseBox;


    public boolean keyDown (int keycode) {
        return false;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        // check mouse box collision
//        Vector3 mousePosition = new Vector3(x, y, 0);
//        Drop.camera.unproject(mousePosition);
//
//        final List<Fixture> fixtureList = new ArrayList<>(10);
//        Drop.world.QueryAABB(new QueryCallback() {
//            @Override
//            public boolean reportFixture(Fixture fixture) {
////                System.out.println(fixture);
//                fixtureList.add(fixture);
////                Section s = (Section) fixture.getBody().getUserData();
////                System.out.println(s);
//                return true;
//            }
//        }, mousePosition.x, mousePosition.y, mousePosition.x, mousePosition.y);
//        for (Fixture f : fixtureList) {
//            if (f.getBody().getUserData() instanceof Sensor) {
//                Sensor o = (Sensor) f.getBody().getUserData();
//                System.out.println(o.getSensorPosition());
//                break;
//            }
//        }
//        return true;
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
//        Vector3 mousePosition = new Vector3(x, y, 0);
//        Drop.camera.unproject(mousePosition);
//
//        final List<Fixture> fixtureList = new ArrayList<>(10);
//        Drop.world.QueryAABB(new QueryCallback() {
//            @Override
//            public boolean reportFixture(Fixture fixture) {
//                fixtureList.add(fixture);
//                return true;
//            }
//        }, mousePosition.x, mousePosition.y, mousePosition.x, mousePosition.y);
//
//        for (Fixture f : fixtureList) {
//            if (f.getBody().getUserData() instanceof Sensor) {
//                Sensor o = (Sensor) f.getBody().getUserData();
//                System.out.println(o.getSensorPosition());
//                break;
//            }
//        }
//
//        System.out.println("end");
        return false;
//        return false;
    }

    public boolean mouseMoved (int x, int y) {

        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        return false;
    }


}