package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.MultiplayerGame;
import it.polimi.ingsw.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
TYPE & LEVEL by cardID:
- 101 - 112 GREEN
  - 101 - 104 LEVEL I
  - 105 - 108 LEVEL II
  - 109 - 112 LEVEL III
- 113 - 124 PURPLE
  - 113 - 116 LEVEL I
  - 117 - 120 LEVEL II
  - 121 - 124 LEVEL III
- 125 - 136 BLUE
  - 125 - 128 LEVEL I
  - 129 - 132 LEVEL II
  - 133 - 136 LEVEL III
- 137 - 148 YELLOW
  - 137 - 140 LEVEL I
  - 141 - 144 LEVEL II
  - 145 - 148 LEVEL III
 */

/**
 * NumberRequirementTests if the method checkRequirement works as excepted
 */
public class NumberRequirementTest {
    Game game;
    Player player;

    //WhiteMarbleLeaderCard(s) and DiscountLeaderCard(s) have a NumberRequirement type, cardID [213-216] & [201-204]
    LeaderCard cardA;
    LeaderCard cardB;

    /**
     * Method setUp creates an instance of Player and adds to it 5 different DevelopmentCards
     */
    @Before
    public void setUp() {
        game = new MultiplayerGame();
        game.addPlayer("Jim");
        player = game.getPlayerByName("Jim");
        player.addDevelopmentCard(JsonCardsCreator.generateDevelopmentCard(101));
        player.addDevelopmentCard(JsonCardsCreator.generateDevelopmentCard(108));
        player.addDevelopmentCard(JsonCardsCreator.generateDevelopmentCard(110));
        player.addDevelopmentCard(JsonCardsCreator.generateDevelopmentCard(114));
        player.addDevelopmentCard(JsonCardsCreator.generateDevelopmentCard(126));
        cardA = JsonCardsCreator.generateLeaderCard(214); //requirements met
        cardB = JsonCardsCreator.generateLeaderCard(204); //requirements not met
    }

    /**
     * Method tearDown removes the references to the objects created
     */
    @After
    public void tearDown() {
        game = null;
        player = null;
    }

    /**
     * Method checkRequirement checks if the method works as expected
     */
    @Test
    public void checkRequirement() {
        assertTrue(cardA.getRequirement().checkRequirement(player));
        assertFalse(cardB.getRequirement().checkRequirement(player));
    }
}