package it.polimi.ingsw.model;

/**
 * Interface Requirement has to be implemented by every Card in the game.
 * It counts as a cost for the DevelopmentCard or as a requirement for the LeaderCard.
 * Marker interface pattern design is applied.
 */
public interface Requirement {
    boolean checkRequirement(Player player);
}