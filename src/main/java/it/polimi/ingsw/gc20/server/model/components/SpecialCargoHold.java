package it.polimi.ingsw.gc20.server.model.components;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewCargoHold;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewSpecialCargoHold;
import it.polimi.ingsw.gc20.server.exceptions.CargoFullException;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;


public class SpecialCargoHold extends CargoHold {

    public SpecialCargoHold() {
        super();
    }


    /**
     * This method is used to load a cargo in the cargo hold
     * @param g the cargo to be loaded
     * @throws CargoFullException if the cargo hold cannot hold red cargo
     */
    @Override
    public void loadCargo(CargoColor g) throws CargoFullException {
        if (this.availableSlots == 0) {
            throw new CargoFullException("CargoHold is full");
        }
        cargoHeld.put(g, cargoHeld.getOrDefault(g, 0) + 1);
        this.availableSlots--;
    }

    @Override
    public ViewComponent createViewComponent() {
        ViewSpecialCargoHold viewSpecialCargoHold = new ViewSpecialCargoHold();
        viewSpecialCargoHold.red = cargoHeld.getOrDefault(CargoColor.RED, 0);
        viewSpecialCargoHold.green = cargoHeld.getOrDefault(CargoColor.GREEN, 0);
        viewSpecialCargoHold.blue = cargoHeld.getOrDefault(CargoColor.BLUE, 0);
        viewSpecialCargoHold.yellow = cargoHeld.getOrDefault(CargoColor.YELLOW, 0);
        viewSpecialCargoHold.free = availableSlots;
        initializeViewComponent(viewSpecialCargoHold);
        return viewSpecialCargoHold;
    }
}