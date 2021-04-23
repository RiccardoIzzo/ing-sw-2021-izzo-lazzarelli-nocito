package it.polimi.ingsw.model.player;

/**
 * SinglePlayerFaithTrack Class represents a player's faith track in single player mode
 *
 * @author Andrea Nocito
 */
public class SinglePlayerFaithTrack extends FaithTrack {
    private int blackFaithMarker;

    public SinglePlayerFaithTrack() {
        super();
        blackFaithMarker = 0;
    }

    public int getBlackFaithMarker() {
        return blackFaithMarker;
    }

    public void moveBlack() {
        blackFaithMarker++;
        checkBlack();
    }



    public boolean checkBlack() {
        return blackFaithMarker == END;
    }
}

