package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Colors;

public enum MarbleColor {
    WHITE,
    BLUE,
    GRAY,
    YELLOW,
    PURPLE,
    RED;

    @Override
    public String toString() {
        return switch (this) {
            case WHITE -> Colors.WHITE_BRIGHT + "●" + Colors.ANSI_RESET;
            case BLUE -> Colors.ANSI_BLUE + "●" + Colors.ANSI_RESET;
            case GRAY -> Colors.ANSI_WHITE + "●" + Colors.ANSI_RESET;
            case YELLOW -> Colors.ANSI_YELLOW + "●" + Colors.ANSI_RESET;
            case PURPLE -> Colors.ANSI_PURPLE + "●" + Colors.ANSI_RESET;
            case RED -> Colors.ANSI_RED + "●" + Colors.ANSI_RESET;
        };
    }
}
