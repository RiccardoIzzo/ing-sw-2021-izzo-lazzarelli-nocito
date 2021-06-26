package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Colors;

/**
 * Enum Resource provides an enumeration of all the resources.
 */
public enum Resource {
    STONE,
    COIN,
    SHIELD,
    SERVANT;

    /**
     * Method toString returns a string representation with ANSI color/characters for the resource.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return switch (this) {
            case STONE -> Colors.ANSI_WHITE + "♠" + Colors.ANSI_RESET;
            case SERVANT -> Colors.ANSI_PURPLE + "♥" + Colors.ANSI_RESET;
            case SHIELD -> Colors.ANSI_CYAN + "♣" + Colors.ANSI_RESET;
            case COIN -> Colors.ANSI_YELLOW + "♦" + Colors.ANSI_RESET;
        };
    }
}


