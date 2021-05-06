package it.polimi.ingsw.events.clientmessages;

/**
 * SelectLeaderCard message is used by the player at the beginning of the game to select two leader cards out of the four available.
 */
public class SelectLeaderCards implements ClientMessage{
    private final int firstCardIndex;
    private final int secondCardIndex;

    /**
     * Constructor SelectLeaderCards creates a new SelectLeaderCards instance.
     * @param firstCard index of the first leader card selected.
     * @param secondCard index of the second leader card selected.
     */
    public SelectLeaderCards(int firstCard, int secondCard){
        this.firstCardIndex= firstCard;
        this.secondCardIndex = secondCard;
    }

    /**
     * Method getFirstCardIndex returns the index of the first leader card selected.
     * @return first card index.
     */
    public int getFirstCardIndex() {
        return firstCardIndex;
    }

    /**
     * Method getSecondCardIndex returns the index of the second leader card selected.
     * @return second card index.
     */
    public int getSecondCardIndex() {
        return secondCardIndex;
    }
}
