package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

public class ResourceRequirementTest {
    Game game;
    Player player;

    //ExtraShelfLeaderCard(s) and DevelopmentCard(s) have a ResourceRequirement type, cardID [205-208] & [101-148]
    LeaderCard leaderCardA;
    LeaderCard leaderCardB;
    DevelopmentCard developmentCardA;
    DevelopmentCard developmentCardB;

    @Before
    public void setUp() {
        game = new MultiplayerGame();
        game.addPlayer("Jim");
        player = game.getPlayerByName("Jim");
        player.getDashboard().getStrongbox().modifyResource(Resource.STONE, 5);
        player.getDashboard().getStrongbox().modifyResource(Resource.SHIELD, 2);
        player.getDashboard().getStrongbox().modifyResource(Resource.COIN, 1);
        player.getDashboard().getStrongbox().modifyResource(Resource.SERVANT, 0);

        leaderCardA = JsonCardsCreator.generateLeaderCard(206); //requirements met
        leaderCardB = JsonCardsCreator.generateLeaderCard(207); //requirements not met

        developmentCardA = JsonCardsCreator.generateDevelopmentCard(138); //requirements met
        developmentCardB = JsonCardsCreator.generateDevelopmentCard(128); //requirements non met
    }

    @After
    public void tearDown() {
        game = null;
        player = null;
    }


    @Test
    public void checkRequirement() {
        assertTrue(leaderCardA.getRequirement().checkRequirement(player));
        assertTrue(developmentCardA.getRequirement().checkRequirement(player));
        assertFalse(leaderCardB.getRequirement().checkRequirement(player));
        assertFalse(developmentCardB.getRequirement().checkRequirement(player));
    }
}