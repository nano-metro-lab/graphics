package ucc.team9.nanometro.model;

import ucc.team9.nanometro.gfx.Line;
import ucc.team9.nanometro.gfx.Location;
import ucc.team9.nanometro.model.service.ModelService;
import ucc.team9.nanometro.model.service.ModelServiceImpl;

public class ModelServiceFactory {
    private static ModelService<Location, Line> instance;

    public static ModelService<Location, Line> getInstance() {
        if (instance == null) {
            instance = new ModelServiceImpl<>();
        }
        return instance;
    }
}