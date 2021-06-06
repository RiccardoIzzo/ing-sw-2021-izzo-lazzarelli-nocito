package it.polimi.ingsw.events.servermessages;

import it.polimi.ingsw.model.token.SoloActionToken;

/**
 * TokenDrawn message is used to notify the player that the token at the top of the stack has been activated.
 */
public class TokenDrawn implements ServerMessage{
    private final SoloActionToken token;

    /**
     * Constructor TokenDrawn creates a new TokenDrawn instance.
     * @param token SoloActionToken that has been activated.
     */
    public TokenDrawn(SoloActionToken token) {
        this.token = token;
    }

    /**
     * Method getToken returns the token.
     * @return the token.
     */
    public SoloActionToken getToken() {
        return token;
    }
}
