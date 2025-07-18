package it.polimi.ingsw.gc20.server.controller.states;

import it.polimi.ingsw.gc20.common.message_protocol.Message;
import it.polimi.ingsw.gc20.common.message_protocol.toclient.*;
import it.polimi.ingsw.gc20.server.model.cards.Planet;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum representing the different phases of the game state.
 * Each phase corresponds to a specific action or decision point in the game.
 * The enum provides a method to create the appropriate message for each phase.
 */
public enum StatePhase {
        CANNONS_PHASE {
            @Override
            public Message createMessage(State state) {
                return new CannonPhaseMessage(state.createsCannonsMessage());
            }
        },
        ACCEPT_PHASE {
            @Override
            public Message createMessage(State state) {
                return new AcceptPhaseMessage ("Do you want to accept the card?");
            }
        },
        LOSE_CREW_PHASE {
            @Override
            public Message createMessage(State state) {
                return new LoseCrewMessage(state.getCrew());
            }
        },
        AUTOMATIC_ACTION
        {
            @Override
            public Message createMessage(State state) {
                return new AutomaticActionMessage(state.getAutomaticActionMessage());
            }
        },
        REMOVE_CARGO {
            @Override
            public Message createMessage(State state) {
                return new RemoveCargoMessage(state.cargoToRemove());
            }
        },
        ADD_CARGO {
            @Override
            public Message createMessage(State state) {
                return new AddCargoMessage(new ArrayList<>(state.cargoReward()));
            }
        },
        LAND_ON_PLANET{
            @Override
            public Message createMessage(State state) {
                List<Planet> planets = new ArrayList<>();
                for (Planet planet : state.getPlanets()) {
                    Planet newPlanet = new Planet();
                    newPlanet.setReward(new ArrayList<>(planet.getReward()));
                    newPlanet.setAvailable(planet.getAvailable());
                    newPlanet.setPlayer(planet.getPlayer());
                    planets.add(newPlanet);
                }
                return new LandOnPlanetPhase(planets);}
        },
        ENGINES_PHASE
        {
            @Override
            public Message createMessage(State state) {
                return new EnginePhaseMessage(state.createsEnginesMessage());
            }
        },
        SELECT_SHIELD
        {
            @Override
            public Message createMessage(State state) {
                return new ShieldPhaseMessage(state.createsShieldMessage());
            }
        },
        BATTERY_PHASE {
            @Override
            public Message createMessage(State state) {
                return new RemoveBatteryMessage(state.cargoToRemove());
            }
        },
        STANDBY_PHASE{
            @Override
            public Message createMessage(State state) {
                return new StandbyMessage("Waiting for the other players");
            }
        },
        ROLL_DICE_PHASE{
            @Override
            public Message createMessage(State state) {
                return new RollDiceMessage(state.createsRollDiceMessage());
            }
        },
        VALIDATE_SHIP_PHASE{
            @Override
            public Message createMessage(State state) {
                return new ChooseBranchMessage();
            }
        },
        ADD_ALIEN_PHASE{ //not usable is in a concurrent state I would need to send 4 messages or have a player
            @Override
            public Message createMessage(State state) {
                return new AlienPlacementePhaseMessage();
            }
        },
        DRAW_CARD_PHASE {
            @Override
            public Message createMessage(State state) {
                return new DrawCardPhaseMessage();
            }
        },
        ASSEMBLING_PHASE { //not usable is in a concurrent state I would need to send 4 messages or have a player
            @Override
            public Message createMessage(State state) {
                return new AssemblingMessage(null);
            }
        };

    /**
     * Creates a message corresponding to the current state phase.
     *
     * @param state the current game state
     * @return a message that represents the current phase of the game
     */
    public abstract Message createMessage(State state);
}

