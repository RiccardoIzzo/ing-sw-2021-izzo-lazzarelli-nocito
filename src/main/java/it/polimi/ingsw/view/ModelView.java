package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.constants.GameConstants.*;
import static it.polimi.ingsw.constants.PlayerConstants.*;

public class ModelView {
    ArrayList<DashboardView> dashboards;
    private String myNickname;
    private String currPlayer;
    private ArrayList<Integer> grid;
    private MarbleColor slideMarble;
    private ArrayList<MarbleColor> marketTray;
    private Integer tokenDrawn;

    public ModelView(ArrayList<String> players, String myNickname) {
        this.dashboards = new ArrayList<>();
        for (String player: players) {
            dashboards.add(new DashboardView(player));
        }
        this.myNickname = myNickname;
        this.currPlayer = "";
        this.grid = new ArrayList<>(12);
        this.slideMarble = null;
        this.marketTray = new ArrayList<>(12);
        this.tokenDrawn = null;
    }

    public String getMyNickname() {
        return myNickname;
    }

    public String getCurrPlayer() {
        return currPlayer;
    }

    public MarbleColor getSlideMarble() {
        return slideMarble;
    }

    public Integer getTokenDrawn() {
        return tokenDrawn;
    }

    public ArrayList<DashboardView> getDashboards() {
        return dashboards;
    }

    public ArrayList<Integer> getGrid() {
        return grid;
    }

    public ArrayList<MarbleColor> getMarketTray() {
        return marketTray;
    }

    public DashboardView getMyDashboard() {
        return dashboards.stream().filter(dashboardView -> dashboardView.getNickname().equals(myNickname)).findAny().orElse(null);
    }

    public void updateModelView(String playerSource, String propertyName, Object objectToUpdate) {
        if (playerSource != null) {
            DashboardView dashboardView = dashboards.stream().filter(dashboard -> dashboard.getNickname().equals(playerSource)).findAny().orElse(null);
            dashboardView.updateDashboard(propertyName, objectToUpdate);
        } else {
            if (END_TURN.equals(propertyName)) {
                currPlayer = (String) objectToUpdate;
            } else if (TOKEN_DRAWN.equals(propertyName)) {
                tokenDrawn = (Integer) objectToUpdate;
            } else if (MARKET_CHANGE.equals(propertyName)) {
                marketTray = (ArrayList<MarbleColor>) objectToUpdate;
            } else if (SLIDE_MARBLE.equals(propertyName)) {
                slideMarble = (MarbleColor) objectToUpdate;
            }
        }
    }

    public class DashboardView{
        private String nickname;
        private Map<Integer, Boolean> leaderCards;
        private ArrayList<Integer> developmentCards;
        private ArrayList<Integer> availableProduction;
        private ResourceMap strongbox;
        private ArrayList<Resource> warehouse;
        private ArrayList<Resource> extraShelfResources;
        private Integer faithMarker;
        private Integer blackMarker;
        private Boolean[] popesFavorTiles;

        public DashboardView(String nickname) {
            this.nickname = nickname;
            this.leaderCards = new HashMap<>();
            this.developmentCards = new ArrayList<>();
            this.strongbox = new ResourceMap();
            this.warehouse = new ArrayList<>();
            this.extraShelfResources = new ArrayList<>();
            this.faithMarker = 0;
            this.blackMarker = 0;
            this.popesFavorTiles = new Boolean[] {false, false, false};
        }

        public String getNickname() {
            return nickname;
        }

        public Map<Integer, Boolean> getLeaderCards() {
            return leaderCards;
        }

        public ArrayList<Integer> getDevelopmentCards() {
            return developmentCards;
        }

        public ArrayList<Integer> getAvailableProduction() {
            return availableProduction;
        }

        public ResourceMap getStrongbox() {
            return strongbox;
        }

        public ArrayList<Resource> getWarehouse() {
            return warehouse;
        }

        public ArrayList<Resource> getExtraShelfResources() {
            return extraShelfResources;
        }

        public Integer getFaithMarker() {
            return faithMarker;
        }

        public Integer getBlackMarker() {
            return blackMarker;
        }

        public Boolean[] getPopesFavorTiles() {
            return popesFavorTiles;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void swapResources(int firstResource, int secondResource) {
            Collections.swap(warehouse, firstResource, secondResource);
        }

        public void updateDashboard(String propertyName, Object objectToUpdate) {
            if (LEADER_ACTIVATION.equals(propertyName)) {
                leaderCards.put((Integer) objectToUpdate, true);
            } else if (SET_LEADERS.equals(propertyName) || DISCARD_LEADER.equals(propertyName)) {
                leaderCards = (Map<Integer, Boolean>) objectToUpdate;
            } else if (STRONGBOX_CHANGE.equals(propertyName)) {
                strongbox = (ResourceMap) objectToUpdate;
            } else if (TEMPORARY_SHELF_CHANGE.equals(propertyName) || SHELF_CHANGE.equals(propertyName)) {
                warehouse = (ArrayList<Resource>) objectToUpdate;
            } else if (FAITH_MARKER_POSITION.equals(propertyName)) {
                faithMarker = (Integer) objectToUpdate;
            } else if (BLACK_MARKER_POSITION.equals(propertyName)) {
                blackMarker = (Integer) objectToUpdate;
            } else if (POPES_TILES_CHANGE.equals(propertyName)) {
                popesFavorTiles = (Boolean[]) objectToUpdate;
            } else if (DEVELOPMENTS_CHANGE.equals(propertyName)) {
                developmentCards = (ArrayList<Integer>) objectToUpdate;
            } else if (PRODUCTIONS_CHANGE.equals(propertyName)) {
                availableProduction = (ArrayList<Integer>) objectToUpdate;
            }
        }
    }
}
