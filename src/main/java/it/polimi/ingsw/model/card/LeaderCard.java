package it.polimi.ingsw.model.card;

/**
 * Class LeaderCard represents the leader card type in the game.
 * @author Gabriele Lazzarelli
 */
public abstract class LeaderCard extends Card {
    private boolean active;

    /**
     * Constructor of the superclass Card takes three parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * @param cardID is the card's id.'
     * @param victoryPoints are the card's victory points.
     * @param requirement   is the card's requirement.
     */
    public LeaderCard(int cardID, int victoryPoints, Requirement requirement) {
        super(cardID, victoryPoints, requirement);
        active = false;
    }

    /**
     * Method isActive returns true if the LeaderCard has been activated, false if not.
     * @return true if the selected LeaderCard is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method setActive is called when a LeaderCard is activated.
     * It is called once and sets active=true.
     * @param active is set true for active LeaderCard.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
