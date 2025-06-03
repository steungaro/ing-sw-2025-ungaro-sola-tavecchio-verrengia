package it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.gc20.client.view.TUI.TUI;
import it.polimi.ingsw.gc20.client.view.common.localmodel.ClientGameModel;
import it.polimi.ingsw.gc20.server.model.cards.AdventureCard;
import it.polimi.ingsw.gc20.server.model.cards.Projectile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.gc20.server.model.components.Direction;
import it.polimi.ingsw.gc20.server.model.cards.FireType;

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

public abstract class ViewAdventureCard {

    public abstract String toLine(int i);

    public static ViewAdventureCard createFrom(AdventureCard adventureCard) {
        String type = adventureCard.getName();
        if (type.equals("CombatZone")) {
            type = "CombatZone" + adventureCard.combatType();
        }

        try {
            String className = "it.polimi.ingsw.gc20.client.view.common.localmodel.adventureCards.View" + type;

            Class<?> clazz = Class.forName(className);

            ViewAdventureCard viewCard = (ViewAdventureCard) clazz.getConstructor(AdventureCard.class).newInstance(adventureCard);

            viewCard.type = type;

            return viewCard;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating view card for " + type);
        }
    }

    // Nuovo metodo da aggiungere
    protected void initialize(AdventureCard adventureCard) {
        // Inizializzazione comune per tutti i tipi di carte
        this.type = adventureCard.getName();
    }


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

    protected static final String UP = "╭──────────────────────╮";

    protected static final String DOWN = "╰──────────────────────╯";

    protected static final String EMPTY_ROW = "                      ";

    protected static final String LATERAL = "│";

    protected String spaces(int n){
        String result = "";
        for(int i = 0; i < n; i++){
            result += " ";
        }
        return result;
    }

    public static void main(String[] args) throws RemoteException {
        ViewSlavers slavers = new ViewSlavers();
        slavers.type = "Slavers";
        slavers.firePower = 6;
        slavers.lostCrew = 3;
        slavers.credits = 5;
        slavers.lostDays = 1;

        System.out.println(slavers);
        System.out.print("\n");

        ViewSmugglers smugglers = new ViewSmugglers();
        smugglers.type = "Smugglers";
        smugglers.firePower = 4;
        smugglers.lostCargo = 2;
        smugglers.redCargo = 0;
        smugglers.yellowCargo = 1;
        smugglers.greenCargo = 1;
        smugglers.blueCargo = 1;
        smugglers.lostDays = 1;

        System.out.println(smugglers);
        System.out.print("\n");

        ViewPirates pirates = new ViewPirates();
        pirates.type = "Pirates";
        pirates.firePower = 5;
        pirates.credits = 4;
        pirates.lostDays = 1;
        List<Projectile> projectiles = new ArrayList<>();
        Projectile projectile = new Projectile();
        projectile.setFireType(FireType.HEAVY_FIRE);
        projectile.setDirection(Direction.UP);
        Projectile projectile2 = new Projectile();
        projectiles.add(projectile2);
        projectile2.setFireType(FireType.LIGHT_FIRE);
        projectile2.setDirection(Direction.RIGHT);
        projectiles.add(projectile);
        pirates.projectiles = projectiles;

        System.out.println(pirates);
        System.out.print("\n");

        ViewStardust stardust = new ViewStardust();
        stardust.type = "Stardust";

        System.out.println(stardust);
        System.out.print("\n");

        ViewOpenSpace openSpace = new ViewOpenSpace();
        openSpace.type = "OpenSpace";

        System.out.println(openSpace);
        System.out.print("\n");

        ViewMeteorSwarm meteorSwarm = new ViewMeteorSwarm();
        meteorSwarm.type = "MeteorSwarm";
        List<Projectile> projectiles2 = new ArrayList<>();
        Projectile projectile3 = new Projectile();
        projectile3.setFireType(FireType.HEAVY_METEOR);
        projectile3.setDirection(Direction.UP);
        Projectile projectile4 = new Projectile();
        projectile4.setFireType(FireType.LIGHT_METEOR);
        projectile4.setDirection(Direction.RIGHT);
        Projectile projectile5 = new Projectile();
        projectile5.setFireType(FireType.LIGHT_METEOR);
        projectile5.setDirection(Direction.RIGHT);
        Projectile projectile6 = new Projectile();
        projectile6.setFireType(FireType.LIGHT_METEOR);
        projectile6.setDirection(Direction.RIGHT);

        projectiles2.add(projectile3);
        projectiles2.add(projectile4);
        projectiles2.add(projectile5);
        meteorSwarm.projectiles = projectiles2;


        System.out.println(meteorSwarm);
        System.out.print("\n");

        projectiles2.add(projectile6);

        System.out.println(meteorSwarm);
        System.out.print("\n");

        ViewPlanets planets = new ViewPlanets();
        planets.type = "Planets";
        List<int[]> planetsList = new ArrayList<>();
        int[] planet1 = {1, 0, 0, 0};
        int[] planet2 = {0, 1, 0, 0};
        int[] planet3 = {0, 0, 1, 0};
        int[] planet4 = {0, 0, 0, 1};
        planetsList.add(planet1);
        planetsList.add(planet2);
        planetsList.add(planet3);
        planetsList.add(planet4);
        planets.planets = planetsList;
        planets.lostDays = 2;

        System.out.println(planets);
        System.out.print("\n");

        ViewCombatZone0 combatZone0 = new ViewCombatZone0();
        combatZone0.type = "CombatZone0";
        List<Projectile> projectiles3 = new ArrayList<>();
        Projectile projectile7 = new Projectile();
        projectile7.setFireType(FireType.HEAVY_FIRE);
        projectile7.setDirection(Direction.UP);
        Projectile projectile8 = new Projectile();
        projectile8.setFireType(FireType.LIGHT_FIRE);
        projectile8.setDirection(Direction.RIGHT);

        projectiles3.add(projectile7);
        projectiles3.add(projectile8);
        combatZone0.projectiles = projectiles3;

        combatZone0.lostDays = 2;
        combatZone0.lostCrew = 3;

        System.out.println(combatZone0);
        System.out.print("\n");

        ViewAbandonedShip abandonedShip = new ViewAbandonedShip();
        abandonedShip.type = "AbandonedShip";
        abandonedShip.lostDays = 2;
        abandonedShip.lostCrew = 3;
        abandonedShip.credits = 5;

        System.out.println(abandonedShip);
        System.out.print("\n");

        ViewAbandonedStation abandonedStation = new ViewAbandonedStation();
        abandonedStation.type = "AbandonedStation";
        abandonedStation.lostDays = 2;
        abandonedStation.crew = 3;
        abandonedStation.redCargo = 1;
        abandonedStation.yellowCargo = 1;
        abandonedStation.greenCargo = 1;
        abandonedStation.blueCargo = 1;

        System.out.println(abandonedStation);
        System.out.print("\n");

        ViewCombatZone1 combatZone1 = new ViewCombatZone1();
        combatZone1.type = "CombatZone1";
        List<Projectile> projectiles4 = new ArrayList<>();
        Projectile projectile9 = new Projectile();
        projectile9.setFireType(FireType.HEAVY_FIRE);
        projectile9.setDirection(Direction.UP);
        Projectile projectile10 = new Projectile();
        projectile10.setFireType(FireType.LIGHT_FIRE);
        projectile10.setDirection(Direction.RIGHT);

        projectiles4.add(projectile9);
        projectiles4.add(projectile10);

        combatZone1.projectiles = projectiles4;
        combatZone1.lostDays = 2;
        combatZone1.lostCargo = 3;

        System.out.println(combatZone1);
        System.out.print("\n");

        ClientGameModel.setInstance(new TUI());
        ClientGameModel.getInstance().printCardsInLine(List.of(
                slavers,
                smugglers,
                pirates,
                stardust,
                openSpace,
                meteorSwarm,
                planets,
                combatZone0,
                abandonedShip,
                abandonedStation,
                combatZone1
        ));
    }

}
