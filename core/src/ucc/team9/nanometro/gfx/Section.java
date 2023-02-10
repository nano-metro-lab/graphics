package ucc.team9.nanometro.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ucc.team9.nanometro.Main;

import java.util.ArrayList;
import java.util.List;

public class Section {
    Line line;
    Station upper;
    Station lower;
    Boxy path;
    List<Vector2> sampleList = new ArrayList<>();
    List<Sensor> sensorList = new ArrayList<>();

    public Section(Line line, Station upper, Station lower) {
        this.line = line;
        this.upper = upper;
        this.lower = lower;
        this.path = new Boxy(upper, lower);
        generateSamples();
        generateSensors();
    }

    public void generateSamples() {
        int sampleNum = (int) (path.approxLength() / 0.5f);
        for (int i = 0; i < sampleNum; i++) {
            Vector2 j = new Vector2();
            path.valueAt(j, ((float)i)/((float)sampleNum - 1));
            this.sampleList.add(j);
        }
    }
    public void generateSensors() {
        for (Vector2 v : sampleList) {
            sensorList.add(new Sensor(this, v));
        }
    }

    public void draw(ShapeRenderer shape) {
        int k = this.sampleList.size();
        for (int i = 0; i < k - 1; i++) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.BLUE);
            shape.line(sampleList.get(i), sampleList.get(i + 1));
            shape.end();
        }
    }

    private static final World world = Main.world;

//    public void update() {
//        this.generateSamples();
//        this.generateSensors();
//    }

    public void destroy() {
        // destroy sensors
        for (Sensor i : sensorList) {
            i.destroy();
        }
        sensorList = null;
    }
}
