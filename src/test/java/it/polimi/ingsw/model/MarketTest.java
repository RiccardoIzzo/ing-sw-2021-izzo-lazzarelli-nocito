package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MarketTest tests Market class.
 *
 * @author Riccardo Izzo
 */
public class MarketTest {
    Market myMarket;
    int white, blue, gray, yellow, purple, red = 0;

    /**
     * Method initialization create an instance of Market and generates the market tray.
     */
    @BeforeEach
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

        assertEquals(4, white);
        assertEquals(2, gray);
        assertEquals(2, blue);
        assertEquals(2, yellow);
        assertEquals(2, purple);
        assertEquals(1, red);
    }

    /**
     * Method whiteMarbleTest tests the behaviour of the resourceConverter method in the case of a white marble.
     */
    @Test
    public void whiteMarbleTest(){
        assertEquals(Integer.valueOf(0), myMarket.resourceOutput().getResource(Resource.SHIELD));
        myMarket.setSpecialMarble(MarbleColor.BLUE);
        myMarket.resourceConverter(MarbleColor.WHITE);
        assertEquals(Integer.valueOf(1), myMarket.resourceOutput().getResource(Resource.SHIELD));
    }
}