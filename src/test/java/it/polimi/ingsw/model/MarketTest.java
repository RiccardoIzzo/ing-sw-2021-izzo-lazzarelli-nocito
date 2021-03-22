package it.polimi.ingsw.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class MarketTest tests Market class.
 *
 * @author Riccardo Izzo
 */
public class MarketTest {
    Market myMarket;
    int white, blue, gray, yellow, purple, red = 0;

    /**
     * Method initialization create an instance of Market and generates the market tray.
     */
    @Before
    public void initialization(){
        myMarket = new Market();
        myMarket.generateTray();
    }

    /**
     * Method generateTrayTest checks the number of marbles for each color: 4 white, 2 blue, 2 yellow, 2 gray, 2 purple, 1 red.
     */
    @Test
    public void generateTrayTest(){
        int i = 0;

        MarbleColor slide = myMarket.getSlideMarbel();
        if(slide == MarbleColor.WHITE) white++;
        else if(slide == MarbleColor.BLUE) blue++;
        else if(slide == MarbleColor.GRAY) gray++;
        else if(slide == MarbleColor.YELLOW) yellow++;
        else if(slide == MarbleColor.PURPLE) purple++;
        else if(slide == MarbleColor.RED) red++;

        while(i < 12){
            MarbleColor target = myMarket.getMarble(i);
            if(target == MarbleColor.WHITE) white++;
            else if(target == MarbleColor.BLUE) blue++;
            else if(target == MarbleColor.GRAY) gray++;
            else if(target == MarbleColor.YELLOW) yellow++;
            else if(target == MarbleColor.PURPLE) purple++;
            else if(target == MarbleColor.RED) red++;
            i++;
        }

        assertEquals(4, white);
        assertEquals(2, gray);
        assertEquals(2, blue);
        assertEquals(2, yellow);
        assertEquals(2, purple);
        assertEquals(1, red);
    }

    /**
     * Method insertMarbleRowTest test the behaviour of insertMarble method and checks the size of the chest in case of a row selection by the user.
     */
    @Test
    public void insertMarbleRowTest() {
        myMarket.insertMarble(2, "ROW");
        assertTrue(3 >= myMarket.chestSize());
    }

    /**
     * Method insertMarbleRowTest test the behaviour of insertMarble method and checks the size of the chest in case of a column selection by the user.
     */
    @Test
    public void insertMarbleColumnTest() {
        myMarket.insertMarble(3, "COL");
        assertTrue(4 >= myMarket.chestSize());
    }

}