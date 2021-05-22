package it.polimi.ingsw.events.servermessages;

/**
 * AskBonusResources message is used to notify the player that he have to choose some bonus resources due to the play order.
 */
public class GetBonusResources implements ServerMessage{
    private final int amount;

    /**
     * Constructor BonusResources creates a new BonusResources instance.
     * @param amount amount of bonus resources.
     */
    public GetBonusResources(int amount){
        this.amount = amount;
    }

    /**
     * Method getAmount returns the amount of resources to choose.
     * @return the amount of resources.
     */
    public int getAmount(){
        return amount;
    }
}
