package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 * Market class represents the market board where the player can buy resources.
 *
 * @author Riccardo Izzo
 */
public class Market {
    private ArrayList<MarbleColor> marketTray;
    private MarbleColor slideMarbel;
    private ResourceMap outputMarket;
    private MarbleColor specialMarble;
    private boolean faith;

    /**
     * Constructor Market create a new Market instance.
     */
    public Market(){
        marketTray = new ArrayList<>();
        outputMarket = new ResourceMap();
    }

    /**
     * Method getSlideMarbel returns the marble that can slide, the one outside the market tray.
     * @return the marble.
     */
    public MarbleColor getSlideMarbel(){
        return slideMarbel;
    }

    /**
     * Method getMarble returns the marble of the the market tray at the specific index.
     * @param index marbel position in the market tray.
     * @return the marble.
     */
    public MarbleColor getMarble(int index){
        return marketTray.get(index);
    }

    /**
     * Method getFaith return a boolean that indicates if in the last turn a red marble was collected.
     * @return true if faith was collected in the last turn.
     */
    public boolean getFaith(){
        return faith;
    }

    /**
     * Method setSpecialMarble updates the variable "specialMarble".
     * @param marble the new specialMarble.
     */
    public void setSpecialMarble(MarbleColor marble){
        this.specialMarble = marble;
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
     * Method insertMarble takes all the resources of the selected row/column.
     * @param pos row/column index.
     * @param type represent the user choice: 1 = row, 2 = column.
     */
    public void insertMarble(int pos, int type){
        faith = false;
        if(type == 1) selectRow(pos - 1);
        else if(type == 2) selectCol(pos - 1);
    }

    /**
     * Method selectRow push the row in order and insert the slideMarbel in the market tray.
     * @param n row index.
     */
    public void selectRow(int n){
        MarbleColor temp = slideMarbel;
        int offset = n * 4;

        for(int i = 0; i < 4; i++){
            resourceConverter(getMarble(offset + i));
        }

        slideMarbel = getMarble(offset);
        marketTray.set(offset, getMarble(offset + 1));
        marketTray.set(offset + 1, getMarble(offset + 2));
        marketTray.set(offset + 2, getMarble(offset + 3));
        marketTray.set(offset + 3, temp);
    }

    /**
     * Method selectRow push the column in order and insert the slideMarbel in the market tray.
     * @param n column index.
     */
    public void selectCol(int n){
        MarbleColor temp = slideMarbel;

        for(int i = 0; i < 3; i++){
            resourceConverter(getMarble(n + (i * 4)));
        }

        slideMarbel = getMarble(n + 8);
        marketTray.set(n + 8, getMarble(n + 4));
        marketTray.set(n + 4, getMarble(n));
        marketTray.set(n, temp);
    }

    /**
     * Method resourceOutput returns a ResourceMap with the amount of taken resources.
     * @return a ResourceMap.
     */
    public ResourceMap resourceOutput(){
        return outputMarket;
    }

    /**
     * Method resourceConverter converts the color of the marble to the corresponding resource type and adds it to the ResourceMap.
     * @param marble marble color.
     */
    private void resourceConverter(MarbleColor marble){
        if(marble == MarbleColor.BLUE) outputMarket.addResource(Resource.SHIELD, 1);
        else if(marble == MarbleColor.GRAY) outputMarket.addResource(Resource.STONE, 1);
        else if(marble == MarbleColor.YELLOW) outputMarket.addResource(Resource.COIN, 1);
        else if(marble == MarbleColor.PURPLE) outputMarket.addResource(Resource.SERVANT, 1);
        else if(marble == MarbleColor.RED) faith = true;
        else if(marble == MarbleColor.WHITE) {
            if(specialMarble != null){
                resourceConverter(specialMarble);
            }
        }
    }

}
