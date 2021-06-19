package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.GameConstants;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MarketTest tests Market class.
 *
 * @author Riccardo Izzo
 */
public class MarketTest {
    Market myMarket;
    int white, blue, gray, yellow, purple, red = 0;

    /**
     * Method initialization creates an instance of Market and generates the market tray.
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

        MarbleColor slide = myMarket.getSlideMarble();
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

        assertEquals(GameConstants.NUM_WHITE_MARBLES, white);
        assertEquals(GameConstants.NUM_MARBLES, gray);
        assertEquals(GameConstants.NUM_MARBLES, blue);
        assertEquals(GameConstants.NUM_MARBLES, yellow);
        assertEquals(GameConstants.NUM_MARBLES, purple);
        assertEquals(GameConstants.NUM_RED_MARBLES, red);
    }

    /**
     * Method whiteMarbleTest tests the behaviour of the resourceConverter method in the case of a white marble.
     */
    @Test
    public void whiteMarbleConversionTest(){
        assertEquals(Integer.valueOf(0), myMarket.resourceOutput().getResource(Resource.SHIELD));
        myMarket.setSpecialMarble(MarbleColor.BLUE);
        myMarket.resourceConverter(MarbleColor.WHITE);
        assertEquals(Integer.valueOf(1), myMarket.resourceOutput().getResource(Resource.SHIELD));
    }

    /**
     * Method resetTest tests reset method checking that outputMarket is empty and that both specialMarble and foundFaith are set to default values.
     */
    @Test
    public void resetTest(){
        myMarket.reset();
        for(Resource resource : Resource.values()){
            assertEquals(Integer.valueOf(0), myMarket.resourceOutput().getResource(resource));
        }
        assertNull(myMarket.getSpecialMarble());
        assertFalse(myMarket.findFaith());
    }

    /**
     * Method insertMarbleRowTest tests the insertion of a marble in a row.
     */
    @Test
    public void insertMarbleRowTest(){
        //INSERT HERE THE ROW NUMBER TO TEST, NUM_ROW = {1, 2 ,3}
        int NUM_ROW = 2;
        int index = (NUM_ROW - 1) * 4;
        MarbleColor currSlideMarble = myMarket.getSlideMarble();
        MarbleColor nextSlideMarble = myMarket.getMarble(index);
        MarbleColor marble1 = myMarket.getMarble(index + 1);
        MarbleColor marble2 = myMarket.getMarble(index + 2);
        MarbleColor marble3 = myMarket.getMarble(index + 3);
        //insert marble in the first row
        myMarket.insertMarble(NUM_ROW, 1);
        //checks that the marbles slide correctly in the grid
        assertEquals(myMarket.getSlideMarble(), nextSlideMarble);
        assertEquals(myMarket.getMarble(index), marble1);
        assertEquals(myMarket.getMarble(index + 1), marble2);
        assertEquals(myMarket.getMarble(index + 2), marble3);
        assertEquals(myMarket.getMarble(index + 3), currSlideMarble);

        int size = 0;
        for(Resource resource : Resource.values()){
            size += myMarket.resourceOutput().getResource(resource);
        }
        // when selecting a row the max number of resources obtainable is 4
        assertTrue(size <= 4);
    }

    /**
     * Method insertMarbleRowTest tests the insertion of a marble in a column.
     */
    @Test
    public void insertMarbleColumnTest(){
        //INSERT HERE THE COLUMN NUMBER TO TEST, NUM_COLUMN = {1, 2, 3, 4}
        int NUM_COLUMN = 3;
        int index = NUM_COLUMN - 1;
        MarbleColor currSlideMarble = myMarket.getSlideMarble();
        MarbleColor nextSlideMarble = myMarket.getMarble(index + 8);
        MarbleColor marble1 = myMarket.getMarble(index + 4);
        MarbleColor marble2 = myMarket.getMarble(index);
        // test: insert marble in the NUM_COLUMN column
        myMarket.insertMarble(NUM_COLUMN, 2);
        //checks that the marbles slide correctly in the grid
        assertEquals(myMarket.getSlideMarble(), nextSlideMarble);
        assertEquals(myMarket.getMarble(index + 8), marble1);
        assertEquals(myMarket.getMarble(index + 4), marble2);
        assertEquals(myMarket.getMarble(index), currSlideMarble);

        int size = 0;
        for(Resource resource : Resource.values()){
            size += myMarket.resourceOutput().getResource(resource);
        }
        // when selecting a column the max number of resources obtainable is 3
        assertTrue(size <= 3);
    }
}