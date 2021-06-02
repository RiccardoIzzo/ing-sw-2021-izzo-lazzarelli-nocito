package it.polimi.ingsw.network;

import it.polimi.ingsw.events.servermessages.UpdateView;
import it.polimi.ingsw.view.ModelView;

/**
 * ModelViewUpdater class manages UpdateView message and updates the ModelView.
 *
 * @author Gabriele Lazzarelli, Riccardo Izzo
 */
public class ModelViewUpdater extends Thread{
    private final ModelView modelView;
    private final UpdateView message;

    /**
     * Constructor ModelViewUpdater creates a new ModelViewUpdater instance.
     * @param modelView the model view to update.
     * @param message the UpdateView message.
     */
    public ModelViewUpdater(ModelView modelView, UpdateView message) {
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
    }
}
