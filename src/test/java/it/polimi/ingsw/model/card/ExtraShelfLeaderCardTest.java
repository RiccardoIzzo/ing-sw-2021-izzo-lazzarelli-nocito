package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Gabriele Lazzarelli , Andrea Nocito
 */
public class ExtraShelfLeaderCardTest {
    ExtraShelfLeaderCard extraShelfLeaderCard;
    Integer cardID = 1;
    Integer victoryPoints = 2;
    Requirement requirement;
    Resource allowedResource = Resource.COIN;
    /**
     * Method initialization creates an instance of  ExtraShelfLeaderCard
     * */
    @Before
    public void initialization() {
        requirement = new ResourceRequirement(new ResourceMap());
        extraShelfLeaderCard = new ExtraShelfLeaderCard(cardID, victoryPoints, requirement, allowedResource);
    }

    /**
     * Method testGetResource checks that the output of getResource is the resource set with the constructor
     * */
    @Test
    public void testGetResource() {
        assertEquals(extraShelfLeaderCard.getResource(), allowedResource);
    }

    /**
     * Method testToString checks that the output values stated in the string are the correct ones
     * */
    @Test
    public void testToString() {
        String outputString = extraShelfLeaderCard.toString();
        assertTrue(outputString.contains("ID: " + cardID));
        assertTrue(outputString.contains("VP: " + victoryPoints));
        assertTrue(outputString.contains("2[" + allowedResource.toString() + "]"));
    }
}