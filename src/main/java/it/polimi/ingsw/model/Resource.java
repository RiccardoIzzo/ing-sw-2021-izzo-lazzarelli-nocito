package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Colors;

import java.awt.*;

public enum Resource {
    STONE,
    COIN,
    SHIELD,
    SERVANT;

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


