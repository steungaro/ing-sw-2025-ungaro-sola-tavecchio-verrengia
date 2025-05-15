package it.polimi.ingsw.gc20.server.controller.states;

public enum StatePhase {
        CANNONS_PHASE("Select cannons and batteries for this phase"),
        ACCEPT_PHASE("Accept or discard the card"),
        LOSE_CREW_PHASE("Select the crew to lose"),
        AUTOMATIC_ACTION("Automatic action will be performed, no action needed"),
        REMOVE_CARGO("select the cargo to remove, if there are not enough cargoes, you will be prompted to remove batteries"),
        ADD_CARGO("add cargo to the ship, you can reorganize your cargo"),
        GET_HIT("your ship will be shot"),
        LAND_ON_PLANET("Select the planet to land on"),
        ENGINES_PHASE("Select engines and batteries for this phase"),
        SELECT_SHIELD("Select the shield to use"),
        TAKE_COMPONENT("can select the component you want to take, or a deck to view, or turn the hourglass"),
        PLACE_COMPONENT("can place the component you took in the previous phase"),
        BATTERY_PHASE ("finished their cargo, Select batteries for this phase"),
        STANDBY_PHASE("wait for your turn"),
        ROLL_DICE_PHASE("roll the dice"),
        VALIDATE_SHIP_PHASE("validate your ship"),
        ADD_ALIEN_PHASE("add alien to the ship");
        private final String description;

        StatePhase(String description) {
            this.description = description;
        }

        /**
         * Restituisce una descrizione di ciò che il giocatore deve fare durante questa fase
         * @return stringa di istruzioni per questa fase
         */
        public String getDescription() {
            return description;
        }
    }

