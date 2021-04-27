package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.GameConstants;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * GameTest tests Game class.
 *
 * @author Riccardo Izzo
 */
public class GameTest {
    Game game;

    /**
     * Method initialization create an instance of Game and adds three players.
     */
    @Before
    public void initialization() {
        game = new Game();
        game.addPlayer("Riccardo");
        game.addPlayer("Andrea");
        game.addPlayer("Gabriele");
        game.generateGrid();
        game.generateLeaders();
    }

    /**
     * Method AddRemovePlayers tests addPlayer and removePlayer methods, checks the number of players.
     */
    @Test
    public void AddRemovePlayers() {
        assertEquals("Riccardo", game.getPlayers().get(0).getNickname());
        assertEquals("Andrea", game.getPlayers().get(1).getNickname());
        assertEquals("Gabriele", game.getPlayers().get(2).getNickname());
        assertEquals(3, game.getNumPlayers());
        game.addPlayer("Francesco");
        assertEquals(4, game.getNumPlayers());
        game.removePlayer("Francesco");
        assertEquals(3, game.getNumPlayers());
        game.removePlayer("Riccardo");
        game.removePlayer("Gabriele");
        game.removePlayer("Andrea");
        assertEquals(0, game.getNumPlayers());
    }

    /**
     * Method generateGridTest checks the correct creation of the 48 development cards in the deck grid.
     */
    @Test
    public void generateGridTest() {
        int size = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 3; i++) {
                size += game.getDeck(0, 0).getCards().size();
            }
        }
        assertEquals(GameConstants.NUM_DEVELOPMENT_CARDS, size);
    }

    /**
     * Method generateLeadersTest checks the correct assignment of four leader cards to each player at the start of the game.
     */
    @Test
    public void generateLeadersTest(){
        for(Player player : game.getPlayers()){
            assertEquals(4, player.getLeaders().size());
        }

    }

    /**
     * Method gridTest checks that in each deck there are only cards of the same color and level.
     */
    @Test
    public void gridTest(){
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 3; i++) {
                DevelopmentCard card = (DevelopmentCard) game.getDeck(i, j).draw();
                DevelopmentCard temp;
                CardColor color = card.getType();
                int level = card.getLevel();

                while (!(game.getDeck(i, j).getCards().empty())) {
                    temp = (DevelopmentCard) game.getDeck(i, j).draw();
                    assertEquals(color, temp.getType());
                    assertEquals(level, temp.getLevel());
                }
            }
        }
    }

    /**
     * Method getPlayerByNameTest test getPlayerByName method in both cases:
     * 1. Standard case when there is a player with the name specified as a parameter.
     * 2. Null case when there is no player with that name.
     */
    @Test
    public void getPlayerByNameTest(){
        assertEquals(game.getPlayers().get(0), game.getPlayerByName("Riccardo"));
        assertNotEquals(game.getPlayers().get(1), game.getPlayerByName("Riccardo"));
        assertEquals(game.getPlayers().get(1), game.getPlayerByName("Andrea"));
        assertEquals(game.getPlayers().get(2), game.getPlayerByName("Gabriele"));
        assertNull(game.getPlayerByName("Francesco"));
    }
}