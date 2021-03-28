package it.polimi.ingsw.model;

/**
 * Game class contains the main logic of "Master of Renaissance".
 *
 * @author Riccardo Izzo
 */
public class Game {
    private Market market;
    private Deck[][] grid;

    /**
     * Game constructor creates a new Game instance.
     */
    public Game(){
        market = new Market();
        grid = new Deck[3][4];
    }

    public void startGame(){
    }
}
