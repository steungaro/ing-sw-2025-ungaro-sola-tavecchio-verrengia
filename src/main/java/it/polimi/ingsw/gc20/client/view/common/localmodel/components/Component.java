package it.polimi.ingsw.gc20.client.view.common.localmodel.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Battery.class, name = "Battery"),
        @JsonSubTypes.Type(value = Cannon.class, name = "Cannon"),
        @JsonSubTypes.Type(value = Cabin.class, name = "Cabin"),
        @JsonSubTypes.Type(value = Engine.class, name = "Engine"),
        @JsonSubTypes.Type(value = CargoHold.class, name = "CargoHold"),
        @JsonSubTypes.Type(value = Pipes.class, name = "Pipes"),
        @JsonSubTypes.Type(value = SpecialCargoHold.class, name = "SpecialCargoHold"),
        @JsonSubTypes.Type(value = LifeSupport.class, name = "LifeSupport"),
        @JsonSubTypes.Type(value = Shield.class, name = "Shield")
        // Add other component types as needed
})
public class Component {
    public int id;
    public int rotation; // 0 = up, 1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees
    public int upConnectors;
    public int downConnectors;
    public int leftConnectors;
    public int rightConnectors;

    protected static final String EMPTY_ROW = "             ";

    private static final String UP3 =    "╭──↑───↑───↑──╮";
    private static final String UP2 =    "╭──↑───────↑──╮";
    private static final String UP1 =    "╭──────↑──────╮";
    private static final String UP0 =    "╭─────────────╮";

    private static final String DOWN3 =  "╰──↓───↓───↓──╯";
    private static final String DOWN2 =  "╰──↓───────↓──╯";
    private static final String DOWN1 =  "╰──────↓──────╯";
    private static final String DOWN0 =  "╰─────────────╯";

    private static final String[] LEFT0 = {
            "│",
            "│",
            "│"
    };
    private static final String[] LEFT1 = {
            "│",
            "←",
            "│"
    };
    private static final String[] LEFT2 = {
            "←",
            "│",
            "←"
    };
    private static final String[] LEFT3 = {
            "←",
            "←",
            "←"
    };

    private static final String[] RIGHT0 = {
            "│",
            "│",
            "│"
    };
    private static final String[] RIGHT1 = {
            "│",
            "→",
            "│"
    };
    private static final String[] RIGHT2 = {
            "→",
            "│",
            "→"
    };
    private static final String[] RIGHT3 = {
            "→",
            "→",
            "→"
    };

    protected String firstRow() {
        return upConnectors == 0 ? UP0 : upConnectors == 1 ? UP1 : upConnectors == 2 ? UP2 : UP3;
    }

    protected String lastRow() {
        return downConnectors == 0 ? DOWN0 : downConnectors == 1 ? DOWN1 : downConnectors == 2 ? DOWN2 : DOWN3;
    }

    public String toLine(int i) {
        return coveredLine(i);
    }

    public String toString() {
        return covered();
    }

    protected String leftCol(int row) {
        return switch (leftConnectors) {
            case 0 -> LEFT0[row];
            case 1 -> LEFT1[row];
            case 2 -> LEFT2[row];
            case 3 -> LEFT3[row];
            default -> null;
        };
    }

    protected String rightCol(int row) {
        return switch (rightConnectors) {
            case 0 -> RIGHT0[row];
            case 1 -> RIGHT1[row];
            case 2 -> RIGHT2[row];
            case 3 -> RIGHT3[row];
            default -> null;
        };
    }

    public static String covered() {
        return
                """
                        ╭─────────────╮
                        │             │
                        │             │
                        │             │
                        ╰─────────────╯""";
    }

    public static String coveredLine(int i) {
        return switch (i) {
            case 0 ->       "╭─────────────╮";
            case 1, 2, 3 ->    "│             │";
            case 4 ->       "╰─────────────╯";
            default -> null;
        };
    }

    public static void main(String[] args) {
        List<Component> allComponents = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Logger logger = Logger.getLogger(Component.class.getName());
        try {
            allComponents = Arrays.asList(mapper.readValue(Component.class.getResourceAsStream("/components.json"), Component[].class));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reading components.json", e);
        }
        for (int i = 0; i < allComponents.size()/4; i++) { // 4 comps per row
            for (int j = 0; j < 5; j++) {
                for (Component component : allComponents.subList(i*4, (i+1)*4)) {
                    System.out.print(component.toLine(j));
                    System.out.print("   ");
                }
                System.out.println();
            }
        }
    }

    public boolean isCabin() {
        return false;
    }

    public boolean isEngine() {
        return false;
    }

    public boolean isBattery() {
        return false;
    }

    public boolean isCannon() {
        return false;
    }

    public boolean isCargoHold() {
        return false;
    }

    public boolean isPipes() {
        return false;
    }

    public boolean isSpecialCargoHold() {
        return false;
    }

    public boolean isLifeSupport() {
        return false;
    }

    public boolean isShield() {
        return false;
    }

    public boolean isStartingCabin() {
        return false;
    }
}
