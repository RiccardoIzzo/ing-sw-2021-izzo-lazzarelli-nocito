package it.polimi.ingsw.events.clientmessages;

import java.util.List;

/**
 * ActivateProduction message is used to activate the productions associated with the card ids.
 */
public class ActivateProduction implements ClientMessage{
    private final List<Integer> cardsID;

    /**
     * Constructor ActivateProduction creates a new ActivateProduction instance.
     * @param ids list of card id associated with the productions to activate.
     */
    public ActivateProduction(List<Integer> ids){
        this.cardsID = ids;
    }

    /**
     * Method getCardsID returns the list of card ids.
     * @return list of card ids.
     */
    public List<Integer> getCardsID(){
        return cardsID;
    }
}
