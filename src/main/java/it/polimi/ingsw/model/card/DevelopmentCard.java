package it.polimi.ingsw.model.card;

/**
 * Class DevelopmentCard contains a ProductionPower which players can use once they have bought one.
 * @author Gabriele Lazzarelli
 */
public class DevelopmentCard extends Card {
    private final CardColor type;
    private final int level;
    private final ProductionPower production;

    /**
     * Constructor Card takes three parameters which are shared among all of the subclasses.
     * Constructor DevelopmentCard sets instance's type, level and production.
     * @param cardID is the card's id.
     * @param victoryPoints are the card's victory points.
     * @param requirement is the card's requirement.
     * @param type is the CardColor type.
     * @param level is an Integer, can vary from one to three.
     * @param production is the ProductionPower associated.
     */
    public DevelopmentCard(int cardID, int victoryPoints, Requirement requirement, CardColor type, int level, ProductionPower production) {
        super(cardID, victoryPoints, requirement);
        this.type = type;
        this.level = level;
        this.production = production;
    }

    /**
     * Method getType gets the color of the card
     * @return the CardColor value of the DevelopmentCard
     */
    public CardColor getType() {
        return type;
    }

    /**
     * Method getLevel gets the level of DevelopmentCard, can vary from one to three
     * @return the level of the DevelopmentCard
     */
    public int getLevel() {
        return level;
    }

    /**
     * Method getProduction gets the productionPower of this DevelopmentCard
     * @return the productionPower of this DevelopmentCard
     */
    public ProductionPower getProduction() {
        return production;
    }
}