package it.polimi.ingsw.gc20.server.model.components;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewSpecialCargoHold;
import it.polimi.ingsw.gc20.server.exceptions.CargoFullException;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;


/**
 * The SpecialCargoHold class extends the functionality of the CargoHold class,
 * allowing for modifications to standard cargo management behavior. It provides
 * methods to handle cargo operations specific to a special type of cargo hold.
 */
public class SpecialCargoHold extends CargoHold {

    public SpecialCargoHold() {
        super();
    }


    /**
     * Loads cargo of the specified color into the cargo hold. If the cargo hold
     * is full, an exception is thrown.
     *
     * @param g The color of the cargo to be loaded, represented by the {@code CargoColor} enum.
     * @throws CargoFullException If there are no available slots in the cargo hold.
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