package it.polimi.ingsw.model;

/**
 * MoveBlackMarkerToken class implements SoloActionToken interface and represents the token that move forward the black cross marker.
 *
 * @author Riccardo Izzo
 */
public class MoveBlackMarkerToken implements SoloActionToken{
    private final int steps;
    private final boolean resetStack;

    /**
     * Constructor MoveBlackMarkerToken creates a new MoveBlackMarkerToken instance.
     * @param steps number of steps that the black cross marker must take.
     * @param reset true if the token also shuffles the stack of token.
     */
    public MoveBlackMarkerToken(int steps, boolean reset){
        this.steps = steps;
        this.resetStack = reset;
    }

    /**
     * Method getSteps returns the number of steps that the black cross marker must take.
     * @return the number of steps.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Method isResetStack returns a boolean that indicates if the token has this effect.
     * @return true it the token has the indicated effect.
     */
    public boolean isResetStack() {
        return resetStack;
    }

    /**
     * Method playToken represents the effect of the token.
     */
    @Override
    public void playToken() {

    }

}
