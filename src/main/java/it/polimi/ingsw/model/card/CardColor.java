package it.polimi.ingsw.model.card;

/**
 * Enum CardColor provides a enumeration of all the Card color types in the game.
 * @author Gabriele Lazzarelli
 */
public enum CardColor {
    GREEN,
    YELLOW,
    BLUE,
    PURPLE;

    public int getColumnGrid(){
        return switch (this) {
            case GREEN -> 0;
            case PURPLE -> 1;
            case BLUE -> 2;
            case YELLOW -> 3;
        };
    }
}

