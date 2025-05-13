package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ViewAbandonedShip.class, name = "AbandonedShip"),
        @JsonSubTypes.Type(value = ViewAbandonedStation.class, name = "AbandonedStation"),
        @JsonSubTypes.Type(value = ViewCombatZone0.class, name = "CombatZone0"),
        @JsonSubTypes.Type(value = ViewCombatZone1.class, name = "CombatZone1"),
        @JsonSubTypes.Type(value = ViewEpidemic.class, name = "Epidemic"),
        @JsonSubTypes.Type(value = ViewMeteorSwarm.class, name = "MeteorSwarm"),
        @JsonSubTypes.Type(value = ViewOpenSpace.class, name = "OpenSpace"),
        @JsonSubTypes.Type(value = ViewPirates.class, name = "Pirates"),
        @JsonSubTypes.Type(value = ViewPlanets.class, name = "Planets"),
        @JsonSubTypes.Type(value = ViewSlavers.class, name = "Slavers"),
        @JsonSubTypes.Type(value = ViewSmugglers.class, name = "Smugglers"),
        @JsonSubTypes.Type(value = ViewStardust.class, name = "Stardust")
        // Add other adventure cards types as needed
})

public abstract class ViewAdvetnureCard {
    public String type;

    protected String lateral() {
        return LATERAL;
    }

    protected String up() {
        return UP;
    }

    protected String down() {
        return DOWN;
    }

    private static final String UP = "╭────────────────────╮";

    private static final String DOWN = "╰────────────────────╯";

    public static final String EMPTY_ROW = "                    ";

    private static final String LATERAL = "│";

    public static void main(String[] args) {
        ViewSlavers slavers = new ViewSlavers();
        slavers.type = "Slavers";
        slavers.firePower = 6;
        slavers.lostCrew = 3;
        slavers.credits = 5;
        slavers.lostDays = 1;

        System.out.println(slavers);

    }
}
