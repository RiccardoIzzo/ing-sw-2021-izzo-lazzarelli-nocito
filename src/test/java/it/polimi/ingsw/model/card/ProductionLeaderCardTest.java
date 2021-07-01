package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * ProductionLeaderCardTest tests ProductionLeaderCard class.
 *
 * @author Andrea Nocito
 * */
public class ProductionLeaderCardTest {
    ProductionLeaderCard productionLeaderCard;
    Integer cardID = 1;
    Integer victoryPoints = 2;
    Requirement requirement;
    ProductionPower productionPower;
    ResourceMap inputResource, outputResource;
    Integer outputFaith = 1;

    /**
     * Method initialization creates an instance of ProductionLeaderCard
     * */
    @Before
    public void initialization() {
        requirement = new ResourceRequirement(new ResourceMap());
        inputResource = new ResourceMap();
        inputResource.modifyResource(Resource.COIN, 1);
        outputResource = new ResourceMap();
        outputResource.modifyResource(Resource.SHIELD, 2);
        productionPower = new ProductionPower(inputResource, outputResource, outputFaith);
        productionLeaderCard = new ProductionLeaderCard(cardID, victoryPoints, requirement, productionPower);
    }

    /**
     * Method testGetExchange checks that the output of getProduction is the productionPower set with the constructor
     * */
    @Test
    public void testGetProduction() {
        assertEquals(productionLeaderCard.getProduction(), productionPower);
    }

    /**
     * Method testToString checks that the output values stated in the string are the correct ones
     * */
    @Test
    public void testToString() {
        String outputString = productionLeaderCard.toString();
        assertTrue(outputString.contains("ID: " + cardID));
        assertTrue(outputString.contains("VP: " + victoryPoints));
        assertTrue(outputString.contains("-Production: " + productionPower.toString()));
    }
}