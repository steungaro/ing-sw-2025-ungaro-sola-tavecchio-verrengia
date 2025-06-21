package it.polimi.ingsw.gc20.server.model.components;

import it.polimi.ingsw.gc20.client.view.common.localmodel.components.ViewComponent;
import it.polimi.ingsw.gc20.server.exceptions.CargoFullException;
import it.polimi.ingsw.gc20.server.exceptions.CargoNotLoadable;
import it.polimi.ingsw.gc20.server.model.gamesets.CargoColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CargoHoldTest {
    private CargoHold cargoHold;
    private SpecialCargoHold specialCargoHold;
    @BeforeEach
    void setUp() {
        cargoHold = new CargoHold();
        specialCargoHold = new SpecialCargoHold();
    }

    @Test
    void testSetGetAdnAvailableSlots(){
        cargoHold.setSlots(5);
        assertEquals(5, cargoHold.getSlots());
        assertEquals(5, cargoHold.getAvailableSlots());
        cargoHold.setIDComponent(1);
        assertEquals(1, cargoHold.getIDComponent());
        assertFalse (cargoHold.isLifeSupport());
    }


    @Test
    void testSetAndGetCargoHeld() throws CargoNotLoadable, CargoFullException {
        try {
            cargoHold.loadCargo(CargoColor.BLUE);
        } catch (CargoFullException e) {
            assertEquals("CargoHold is full", e.getMessage());
        }
        try{
            specialCargoHold.loadCargo(CargoColor.RED);
        }catch (CargoFullException e){
            assertEquals("CargoHold is full", e.getMessage());
        }
        cargoHold.setSlots(5);
        specialCargoHold.setSlots(2);
        cargoHold.loadCargo(CargoColor.BLUE);
        specialCargoHold.loadCargo(CargoColor.RED);
        assertEquals(1, specialCargoHold.getCargoHeld().get(CargoColor.RED));
        assertEquals(1, specialCargoHold.getCargoHeld(CargoColor.RED));
        cargoHold.loadCargo(CargoColor.BLUE);
        assertEquals(2, cargoHold.getCargoHeld(CargoColor.BLUE));
        assertEquals(2, cargoHold.getCargoHeld().get(CargoColor.BLUE));
    }

    @Test
    void testCreateViewComponent(){
        Map<Direction, ConnectorEnum> connectors = cargoHold.getConnectors();
        connectors.put(Direction.RIGHT, ConnectorEnum.S);
        connectors.put(Direction.LEFT, ConnectorEnum.D);
        connectors.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors.put(Direction.UP, ConnectorEnum.U);
        cargoHold.setConnectors(connectors);
        assertEquals("upper", Direction.UP.getDirection());
        assertEquals("right-hand", Direction.RIGHT.getDirection());
        assertEquals("lower", Direction.DOWN.getDirection());
        assertEquals("left-hand", Direction.LEFT.getDirection());
        ViewComponent viewComponent = cargoHold.createViewComponent();
        assertNotNull(viewComponent);

        Map<Direction, ConnectorEnum> connectors2 = specialCargoHold.getConnectors();
        connectors2.put(Direction.RIGHT, ConnectorEnum.S);
        connectors2.put(Direction.LEFT, ConnectorEnum.D);
        connectors2.put(Direction.DOWN, ConnectorEnum.ZERO);
        connectors2.put(Direction.UP, ConnectorEnum.U);
        specialCargoHold.setConnectors(connectors);
        ViewComponent viewComponent2 = specialCargoHold.createViewComponent();
        assertNotNull(viewComponent2);
    }
}
