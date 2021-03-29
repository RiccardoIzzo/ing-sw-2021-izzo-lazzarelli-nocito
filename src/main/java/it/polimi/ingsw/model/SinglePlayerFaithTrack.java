package it.polimi.ingsw.model;
/**
 * SinglePlayerFaithTrack Class represents a player's faith track in single player mode
 *
 * @author Andrea Nocito
 */
public class SinglePlayerFaithTrack extends  FaithTrack {
    private int blackFaithMarker;

    public SinglePlayerFaithTrack() {
        super();
        blackFaithMarker = 0;
    }
    public void moveBlack() {
        blackFaithMarker++;
        checkBlack();
    }
    public void checkBlack() {
        if(blackFaithMarker == END) {
            // Game ended
        }
    }
}

