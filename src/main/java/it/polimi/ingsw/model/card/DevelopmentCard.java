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
     * Constructor Card takes two parameters which are shared among all of the subclasses.
     * Constructor DevelopmentCard sets instance's type, level and production.
     * @param victoryPoints are the card's victory points .
     * @param requirement is the card's requirement.
     * @param type is the CardColor type
     * @param level is an Integer, can vary from one to three
     * @param production is the ProductionPower associated
     */
    public DevelopmentCard(int victoryPoints, Requirement requirement, CardColor type, int level, ProductionPower production) {
        super(victoryPoints, requirement);
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
     * Method ProductionPower gets the productionPower of DevelopmentCard
     * @return the productionPower of the DevelopmentCard
     */
    public ProductionPower getProduction() {
        return production;
    }
/*

    */
    /*
     * Method equals is overridden, two DevelopmentCard are equals if their attributes are the same.
     * @param o is the Object instance to compare
     * @return true if the DevelopmentCard instances have the same attributes
     */
    /* There no need for equals() override, in case it will be:
    - create an equals override in Card first
    - extends equals of Card
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCard that = (DevelopmentCard) o;
        return getLevel() == that.getLevel() && getType() == that.getType() && getProduction().equals(that.getProduction());
    }*/
}