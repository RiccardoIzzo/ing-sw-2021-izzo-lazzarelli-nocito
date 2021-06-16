package it.polimi.ingsw.model.card;

/**
 * Class Card is the superclass to both types of cards in the game, provides a
 * requirement attribute which counts as a cost for the DevelopmentCard
 * or as requirement for the LeaderCard.
 * @author Gabriele Lazzarelli
 */
public class Card {
    private final int cardID;
    private final int victoryPoints;
    private final Requirement requirement;

    /**
     * Constructor Card takes three parameters which are shared among all of the subclasses.
     * @param cardID is this Card id.
     * @param victoryPoints are this Card victory points.
     * @param requirement is this Card requirement.
     */
    public Card(int cardID, int victoryPoints, Requirement requirement) {
        this.cardID = cardID;
        this.victoryPoints = victoryPoints;
        this.requirement = requirement;
    }

    /**
     * Method getVictoryPoints returns the victory points of this Card.
     * @return the value of victoryPoints.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Method getRequirement gets the Requirements of this Card.
     * @return the Card's Requirement.
     */
    public Requirement getRequirement() {
        return requirement;
    }

    /**
     * Method getCardID returns the id of this Card.
     * @return the Card's id.
     */
    public int getCardID() {
        return cardID;
    }
}
