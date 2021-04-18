package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DashboardTest extends TestCase {
    Dashboard dashboard;

    /**
     * Method initialization create an instance of Dashboard
     */
    @Before
    public void initialization() {
        dashboard = new Dashboard();
    }

}