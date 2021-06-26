package it.polimi.ingsw.model.card;

import it.polimi.ingsw.constants.Colors;

/**
 * Enum CardColor provides an enumeration of all the Card color types in the game.
 * @author Gabriele Lazzarelli
 */
public enum CardColor {
    GREEN,
    YELLOW,
    BLUE,
    PURPLE;

    /**
     * Method getColumnGrid returns the column index based on the card color.
     * @return the columns index.
     */
    public int getColumnGrid(){
        return switch (this) {
            case GREEN -> 0;
            case PURPLE -> 1;
            case BLUE -> 2;
            case YELLOW -> 3;
        };
    }

    /**
     * Method toString returns a string representation with ANSI color/characters for the card color.
     * @return the string representation.
     */
    @Override
    public String toString() {
        return switch (this) {
            case GREEN -> Colors.ANSI_GREEN + "█" + Colors.ANSI_RESET;
            case YELLOW -> Colors.ANSI_YELLOW + "█" + Colors.ANSI_RESET;
            case BLUE -> Colors.ANSI_BLUE + "█" + Colors.ANSI_RESET;
            case PURPLE -> Colors.ANSI_PURPLE + "█" + Colors.ANSI_RESET;
        };
    }
}

