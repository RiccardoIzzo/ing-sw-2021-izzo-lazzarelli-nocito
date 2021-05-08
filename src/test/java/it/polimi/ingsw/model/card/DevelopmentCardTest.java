package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Gabriele Lazzarelli
 */
public class DevelopmentCardTest {
    Card card1;
    Card card2;
    Card card3;
    ResourceMap resourceMapA;
    ResourceMap resourceMapB;
    ResourceRequirement resourceRequirement;
    ResourceRequirement resourceRequirementDiff;
    ProductionPower productionPower;


    @Before
    public void setUp() {
        resourceMapA = new ResourceMap();
        resourceMapB = new ResourceMap();
        resourceMapA.modifyResource(Resource.COIN, 3);
        resourceMapA.modifyResource(Resource.SHIELD, 1);
        resourceMapB.modifyResource(Resource.SERVANT, 2);
        resourceMapB.modifyResource(Resource.STONE,1 );

        resourceRequirement = new ResourceRequirement(resourceMapA);
        resourceRequirementDiff = new ResourceRequirement(resourceMapB);
        productionPower = new ProductionPower(resourceMapB, resourceMapA, 2);

        card1 = new DevelopmentCard(101, 2, resourceRequirement ,CardColor.GREEN, 2, productionPower);
        card2 = new DevelopmentCard(102, 2, resourceRequirement ,CardColor.GREEN, 2, productionPower);
        card3 = new DevelopmentCard(103, 2, resourceRequirementDiff ,CardColor.GREEN, 2, productionPower);
    }

    @After
    public void tearDown() {
        card1 = null;
        card2 = null;
        card3 = null;
        resourceMapB = null;
        resourceMapA = null;
        resourceRequirement = null;
        resourceRequirementDiff = null;
        productionPower = null;
    }
}