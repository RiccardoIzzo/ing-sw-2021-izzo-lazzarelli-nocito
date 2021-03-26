package it.polimi.ingsw.model;

public abstract class LeaderCard extends Card{
    private boolean active;

    /**
     * Constructor of the superclass Card takes two parameters which are shared among all of the subclasses.
     * Constructor LeaderCard sets active=false since at the beginning there are not active LeaderCard.
     * @param victoryPoints are the card's victory points.
     * @param requirement   is the card's requirement.
     */
    public LeaderCard(int victoryPoints, Requirement requirement) {
        super(victoryPoints, requirement);
        active = false;
    }

    /**
     * Method setAbility is called when a LeaderCard is activated.
     * Has to be implemented by each of the subclasses of LeaderCard.
     */
    public void setAbility() {
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
