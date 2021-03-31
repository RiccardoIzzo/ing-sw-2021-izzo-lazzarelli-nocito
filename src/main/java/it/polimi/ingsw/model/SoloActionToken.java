package it.polimi.ingsw.model;

/**
 * Interface SoloActionToken has to be implemented by the two types of token, MoveBlackMarker and RemoveCards.
 * It generalizes the concept of token.
 *
 * @author Riccardo Izzo
 */

public interface SoloActionToken {
    /**
     * Method playToken has to be implemented by both types of tokens and represents the activation.
     */
    void playToken(SinglePlayerGame game);
}
