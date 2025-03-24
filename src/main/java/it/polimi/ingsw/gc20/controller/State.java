package it.polimi.ingsw.gc20.controller;

/**
 * 
 */
public enum State {
    CREATING,
    ASSEMBLING,
    VALIDATING,
    FLIGHT,

    WAITING_ENGINES, // openspace, combatzone,
    WAITING_ACCEPTANCE, // abandonedStation
    WAITING_CANNONS, // combatzone, pirates, smugglers, meteorswarm, slavers
    WAITING_CARGO_LOST, // smugglers, combatzone,
    WAITING_CARGO_GAIN, // planets, abandonedStation, smugglers
    WAITING_CREW, // slavers, combatzone, abandonedShip
    WAITING_PLANET, // planets
    WAITING_SHIELDS, // meteroswarm, combatzone, pirates
    WAITING_SMUGGLERS,
    FIRING,
    ENDGAME
}