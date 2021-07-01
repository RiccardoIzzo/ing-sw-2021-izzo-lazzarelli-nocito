package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.ResourceMap;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * WhiteMarbleLeaderCardTest tests WhiteMarbleLeaderCard class.
 *
 * @author Andrea Nocito
 * */
public class WhiteMarbleLeaderCardTest {
    WhiteMarbleLeaderCard whiteMarbleLeaderCard;
    Requirement requirement;
    Set<MarbleColor> exchange;
    Integer cardID = 1;
    Integer victoryPoints = 2;
    MarbleColor color = MarbleColor.BLUE;

    /**
     * Method initialization creates an instance of WhiteMarbleLeaderCard
     */
    @Before
    public void initialization() {
        requirement = new ResourceRequirement(new ResourceMap());
        exchange = new HashSet<>();
        exchange.add(color);
        whiteMarbleLeaderCard = new WhiteMarbleLeaderCard(cardID, victoryPoints, requirement,exchange);
    }

    /**
     * Method testGetExchange checks that the output of getExchange is the exchange set with the constructor.
     */
    @Test
    public void testGetExchange() {
        assertEquals(whiteMarbleLeaderCard.getExchange(), exchange);
    }

    /**
     * Method testToString checks that the output values stated in the string are the correct ones
     */
    @Test
    public void testToString() {
        String outputString = whiteMarbleLeaderCard.toString();
        assertTrue(outputString.contains("ID: " + cardID));
        assertTrue(outputString.contains("VP: " + victoryPoints));
        assertTrue(outputString.contains("Exchange: [" + color + "]"));
    }
}