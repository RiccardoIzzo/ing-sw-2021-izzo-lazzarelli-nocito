package it.polimi.ingsw.events.clientmessages;

import java.util.ArrayList;

/**
 * SelectLeaderCards message is used by the player at the beginning of the game to select two leader cards out of the four available.
 */
public class SelectLeaderCards implements ClientMessage{
    private final ArrayList<Integer> leadersToDiscard;

    /**
     * Constructor SelectLeaderCards creates a new SelectLeaderCards instance.
     * @param firstCardID index of the first leader card selected.
     * @param secondCardID index of the second leader card selected.
     */
    public SelectLeaderCards(int firstCardID, int secondCardID){
        leadersToDiscard = new ArrayList<>();
        leadersToDiscard.add(firstCardID);
        leadersToDiscard.add(secondCardID);
    }

    /**
     * Method getLeadersToDiscard returns the LeaderCard(s)' cardID the player wants to discard.
     * @return ArrayList of Integer, the cardID(s) of the LeaderCard(s) to discard.
     */
    public ArrayList<Integer> getLeadersToDiscard(){
        return leadersToDiscard;
    }
}
