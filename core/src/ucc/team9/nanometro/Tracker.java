package ucc.team9.nanometro;

import ucc.team9.nanometro.gfx.Location;
import ucc.team9.nanometro.gfx.Passenger;
import ucc.team9.nanometro.model.service.ModelService;

import java.util.List;

public class Tracker {

    public void updateDestination(ModelService modelService, Passenger p) {
        List<Location> destinations = modelService.findDestinations(Location.LocationType.TRIANGLE, p.location, );
    }
}
