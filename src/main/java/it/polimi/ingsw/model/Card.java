package it.polimi.ingsw.model;

/**
 * Class Card is the superclass to both types of cards in the game, provides a
 * requirement attribute which counts as a cost for the DevelopmentCard
 * or as requirement for the LeaderCard.
 */
public class Card {

    private final int victoryPoints;
    private final Requirement requirement;

    /**
     * Constructor Card takes two parameters which are shared among all of the subclasses.
     * @param victoryPoints are the card's victory points .
     * @param requirement is the card's requirement.
     */
    public Card(int victoryPoints, Requirement requirement) {
        this.victoryPoints = victoryPoints;
        this.requirement = requirement;
    }

    /**
     * Method getVictoryPoints returns the victoryPoints of the associated Card.
     * @return the value of victoryPoints.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }
}
