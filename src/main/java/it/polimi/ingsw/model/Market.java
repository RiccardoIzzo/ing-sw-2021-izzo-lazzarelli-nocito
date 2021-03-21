package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Market {
    private ArrayList<MarbleColor> marketTray;
    private MarbleColor slideMarbel;

    public Market(){
        marketTray = new ArrayList<>();
    }

    public MarbleColor getSlideMarbel(){
        return slideMarbel;
    }

    public MarbleColor getMarble(int index){
        return marketTray.get(index);
    }

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

}
