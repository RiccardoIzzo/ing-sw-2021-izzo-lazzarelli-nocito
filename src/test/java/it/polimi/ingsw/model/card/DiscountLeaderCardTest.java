package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DiscountLeaderCardTest tests DiscountLeaderCard class.
 * @author Gabriele Lazzarelli , Andrea Nocito
 */
public class DiscountLeaderCardTest {
    DiscountLeaderCard discountLeaderCard;
    Integer cardID = 1;
    Integer victoryPoints = 2;
    Requirement requirement;
    ResourceMap discount;

    /**
     * Method initialization creates an instance of  DiscountLeaderCard
     * */
    @Before
    public void initialization() {
        requirement = new ResourceRequirement(new ResourceMap());
        discount = new ResourceMap();
        discount.modifyResource(Resource.COIN, 1);
        discountLeaderCard = new DiscountLeaderCard(cardID, victoryPoints, requirement, discount);
    }

    /**
     * Method testGetDiscount checks that the output of getDiscount is the discount set with the constructor
     * */
    @Test
    public void testGetDiscount() {
        assertEquals(discountLeaderCard.getDiscount(), discount);
    }

    /**
     * Method testToString checks that the output values stated in the string are the correct ones
     * */
    @Test
    public void testToString() {
        String outputString = discountLeaderCard.toString();
        assertTrue(outputString.contains("ID: " + cardID));
        assertTrue(outputString.contains("VP: " + victoryPoints));
        assertTrue(outputString.contains("Discount: " + discount.asList().toString()));
    }
}