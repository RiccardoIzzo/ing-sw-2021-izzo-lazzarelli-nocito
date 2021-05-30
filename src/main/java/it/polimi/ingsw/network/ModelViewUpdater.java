package it.polimi.ingsw.network;

import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;

import static it.polimi.ingsw.constants.GameConstants.END_TURN;
import static it.polimi.ingsw.constants.PlayerConstants.SET_LEADERS;
import static it.polimi.ingsw.constants.PlayerConstants.TEMPORARY_SHELF_CHANGE;

/**
 * ModelViewUpdater class manages UpdateView message and updates the ModelView.
 *
 * @author Gabriele Lazzarelli, Riccardo Izzo
 */
public class ModelViewUpdater extends Thread{
    private final View view;
    private final ModelView modelView;
    private final UpdateView message;

    /**
     * Constructor ModelViewUpdater creates a new ModelViewUpdater instance.
     * @param view view interface.
     * @param modelView the model view to update.
     * @param message the UpdateView message.
     */
    public ModelViewUpdater(View view, ModelView modelView, UpdateView message) {
        this.view = view;
        this.modelView = modelView;
        this.message = message;
    }

    /**
     * Runnable class method that handle a single UpdateView message and updates the model view.
     * Based on the property name some specific methods can be called on the view.
     */
    @Override
    public void run() {
        String sourcePlayer = message.getSourcePlayer();
        String propertyName = message.getPropertyName();
        Object newValue = message.getNewValue();
        modelView.updateModelView(sourcePlayer, propertyName, newValue);
        if (SET_LEADERS.equals(propertyName)) {
            view.handleLeaders();
        }
        else if(END_TURN.equals(propertyName)){
            view.printText(newValue.toString() + ", ready to play.");
        }
        else if(TEMPORARY_SHELF_CHANGE.equals(propertyName)){
            view.handleTemporaryShelf();
        }
    }
}
