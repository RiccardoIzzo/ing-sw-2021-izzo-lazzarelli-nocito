package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 * Market class represent the market board where the player can buy resources.
 *
 * @author Riccardo Izzo
 */
public class Market {
    private ArrayList<MarbleColor> marketTray;
    private MarbleColor slideMarbel;
    private ArrayList<Resource> chest;


    /**
     * Constructor Market create a new Market instance.
     */
    public Market(){
        marketTray = new ArrayList<>();
        chest = new ArrayList<>();
    }


    /**
     * Method getSlideMarbel returns the marble that can slide, the one outside the market tray.
     * @return the marble of type MarbleColor.
     */
    public MarbleColor getSlideMarbel(){
        return slideMarbel;
    }


    /**
     * Method getMarble returns the marble of the the market tray at the specific index.
     * @param index of type int - marbel position.
     * @return the marble of type MarbleColor.
     */
    public MarbleColor getMarble(int index){
        return marketTray.get(index);
    }


    /**
     * Method chestSize returns the number of resources that can be stored (size of chest).
     * @return the number of resources.
     */
    public int chestSize(){
        return chest.size();
    }


    /**
     * Method generateTray creates the market tray with 12 marbles randomly generated and select the marble that can slide.
     */
    public void generateTray(){
        Random random = new Random();

        for(int n_white = 0; n_white < 4; n_white++){
            marketTray.add(MarbleColor.WHITE);
        }
        for(int n_color = 0; n_color < 2; n_color++){
            marketTray.add(MarbleColor.BLUE);
            marketTray.add(MarbleColor.GRAY);
            marketTray.add(MarbleColor.YELLOW);
            marketTray.add(MarbleColor.PURPLE);
        }
        marketTray.add(MarbleColor.RED);

        Collections.shuffle(marketTray);
        int rand = random.nextInt(12);
        slideMarbel = marketTray.get(rand);
        marketTray.remove(rand);
    }


    /**
     * Method insertMarble take all the resources displayed in the chosen row or column
     * @param pos of type int - row/column index.
     * @param type of type String - represent the user choice: row or column.
     */
    public void insertMarble(int pos, String type){
        chest.clear();
        if(type.equals("ROW")) selectRow(pos - 1);
        else if(type.equals(("COL"))) selectCol(pos);

    }


    /**
     * Method selectRow push the row in order and insert the slideMarbel.
     * @param n of type int - row index.
     */
    public void selectRow(int n){
        MarbleColor temp1 = slideMarbel;
        int offset = n * 4;

        for(int i = 0; i < 4; i++){
            resourceConverter(getMarble(offset + i));
        }

        slideMarbel = getMarble(n);
        marketTray.set(offset, getMarble(offset + 1));
        marketTray.set(offset + 1, getMarble(offset + 2));
        marketTray.set(offset + 2, getMarble(offset + 3));
        marketTray.set(offset + 3, temp1);
    }


    /**
     * Method selectRow push the column in order and insert the slideMarbel.
     * @param n of type int - column index.
     */
    public void selectCol(int n){
        MarbleColor temp2 = slideMarbel;

        for(int i = 0; i < 3; i++){
            resourceConverter(getMarble(n + (i * 4)));
        }

        slideMarbel = getMarble(n);
        marketTray.set(n + 8, getMarble(n + 4));
        marketTray.set(n + 4, getMarble(n));
        marketTray.set(n, temp2);
    }


    /**
     * Method resourceConverter converts the color of the marble into the right resource and adds it to the chest.
     * @param marble of type MarbleColor - marble color.
     */
    public void resourceConverter(MarbleColor marble){
        if(marble == MarbleColor.BLUE) chest.add(Resource.SHIELD);
        else if(marble == MarbleColor.GRAY) chest.add(Resource.STONE);
        else if(marble == MarbleColor.YELLOW) chest.add(Resource.COIN);
        else if(marble == MarbleColor.PURPLE) chest.add(Resource.SERVANT);
        //else if(marble == MarbleColor.RED);
        //else if(marble == MarbleColor.WHITE);
    }

}
