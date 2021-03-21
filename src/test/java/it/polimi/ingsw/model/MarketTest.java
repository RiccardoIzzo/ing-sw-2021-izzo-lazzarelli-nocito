package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MarketTest {
    Market myMarket;
    int white = 0;
    int blue = 0;
    int gray = 0;
    int yellow = 0;
    int purple = 0;
    int red = 0;

    @Before
    public void initialization(){
        myMarket = new Market();
        myMarket.generateTray();
    }

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


}