package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Colors;

/**
 * Enum MarbleColor provides an enumeration of all the marble colors in the market.
 */
public enum MarbleColor {
    WHITE,
    BLUE,
    GRAY,
    YELLOW,
    PURPLE,
    RED;

    /**
     * Method toString returns a string representation with ANSI color/characters for the marble color.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return switch (this) {
            case WHITE -> Colors.WHITE_BRIGHT + "●" + Colors.ANSI_RESET;
            case BLUE -> Colors.CYAN_BRIGHT + "●" + Colors.ANSI_RESET;
            case GRAY -> Colors.BLACK_BRIGHT + "●" + Colors.ANSI_RESET;
            case YELLOW -> Colors.ANSI_YELLOW + "●" + Colors.ANSI_RESET;
            case PURPLE -> Colors.ANSI_PURPLE + "●" + Colors.ANSI_RESET;
            case RED -> Colors.ANSI_RED + "●" + Colors.ANSI_RESET;
        };
    }
}
