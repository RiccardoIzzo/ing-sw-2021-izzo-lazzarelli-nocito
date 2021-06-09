package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.constants.GameConstants.*;
import static it.polimi.ingsw.constants.PlayerConstants.*;

public class ModelView {
    ArrayList<DashboardView> dashboards;
    private final String myNickname;
    private String currPlayer;
    private ArrayList<Integer> grid;
    private Boolean[] tilesUncovered;
    private MarbleColor slideMarble;
    private ArrayList<MarbleColor> marketTray;

    public ModelView(ArrayList<String> players, String myNickname) {
        this.dashboards = new ArrayList<>();
        for (String player: players) {
            dashboards.add(new DashboardView(player));
        }
        this.myNickname = myNickname;
        this.currPlayer = "";
        this.grid = new ArrayList<>();
        this.tilesUncovered = new Boolean[]{false, false, false};
        this.slideMarble = null;
        this.marketTray = new ArrayList<>();
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
            } else if (MARKET_CHANGE.equals(propertyName)) {
                marketTray = (ArrayList<MarbleColor>) objectToUpdate;
            } else if (SLIDE_MARBLE.equals(propertyName)) {
                slideMarble = (MarbleColor) objectToUpdate;
            } else if (GRID_CHANGE.equals(propertyName)) {
                grid = (ArrayList<Integer>) objectToUpdate;
            } else if (TILES_UNCOVERED_CHANGE.equals(propertyName)) {
                tilesUncovered = (Boolean[]) objectToUpdate;
            }
        }
    }

    public class DashboardView{
        private String nickname;
        private Map<Integer, Boolean> leaderCards;
        private ArrayList<Integer> developmentCards;
        private ArrayList<Integer> activeDevelopments;
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
            this.activeDevelopments = new ArrayList<>(Collections.nCopies(3,null));
            this.strongbox = new ResourceMap();
            this.warehouse = new ArrayList<>(Collections.nCopies(14,null));
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

        public ArrayList<Integer> getAvailableProduction(){
            ArrayList<Integer> powerProductionCards = new ArrayList<>();
            for(Integer id : leaderCards.keySet()){
                if(leaderCards.get(id))  powerProductionCards.add(id);
            }
            powerProductionCards.addAll(activeDevelopments.stream().filter(Objects::nonNull).collect(Collectors.toList()));
            return powerProductionCards;
        }

        public ResourceMap getTotalResources(){
            ResourceMap myResources = new ResourceMap();
            myResources.addResources(strongbox);
            for(Resource resource : Resource.values()) {
                myResources.modifyResource(resource, (int) warehouse.stream().filter(Objects::nonNull).filter(res -> res == resource).count());
            }
            return myResources;
        }

        public ArrayList<Integer> getActiveDevelopments() {
            return activeDevelopments;
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

        /**
         * Method shelfResources return the amount of resources of a shelf
         * @param shelf the selected shelf
         * @return an integer, the number of resources of the selected shelf
         */
        public int shelfResources(List<Resource> shelf){
            int amount = 0;
            for (Resource resource: shelf) {
                if (resource != null) {
                    amount++;
                }
            }
            return amount;
        }

        public boolean checkWarehouse() {
            List<Resource> shelfOne, shelfTwo, shelfThree, extraShelf1, extraShelf2, temporaryShelf;
            shelfOne = warehouse.subList(0,1);
            shelfTwo = warehouse.subList(1,3);
            shelfThree = warehouse.subList(3,6);
            extraShelf1 = warehouse.subList(6,8);
            extraShelf2 = warehouse.subList(8,10);
            temporaryShelf = warehouse.subList(10,14);

            List<List<Resource>> shelves = new ArrayList<>();
            shelves.add(shelfOne);
            shelves.add(shelfTwo);
            shelves.add(shelfThree);
            if (extraShelfResources.size() > 0) {
                shelves.add(extraShelf1);
                if (extraShelfResources.size() > 1) {
                    shelves.add(extraShelf2);
                } else if (shelfResources(extraShelf2) != 0){
                    return false;
                }
            } else if (shelfResources(extraShelf1) != 0){
                return false;
            }

            if (shelfResources(temporaryShelf) > 0) {
                for (List<Resource> shelf : shelves) {
                    if (shelfResources(shelf) < shelf.size()){
                        if (shelf == extraShelf1 && temporaryShelf.contains(extraShelfResources.get(0))) {
                            return false;
                        } else if (shelf == extraShelf2 && temporaryShelf.contains(extraShelfResources.get(1))) {
                            return false;
                        } else if (shelf.stream().filter(Objects::nonNull).distinct().count() == 1) {
                            Resource resource = shelf.stream().filter(Objects::nonNull).findAny().orElse(null);
                            if (temporaryShelf.contains(resource)) return false;
                        } else if(shelfResources(shelf) == 0) {
                            return false;
                        }
                    }
                }
            }
            for (List<Resource> shelf : shelves) {
                if (shelf.stream().filter(Objects::nonNull).distinct().count() > 1) {
                    return false;
                }
                if (shelf == extraShelf1 && !(shelf.stream().filter(Objects::nonNull).distinct().findAny().orElse(extraShelfResources.get(0)) == extraShelfResources.get(0))) {
                    return false;
                }
                else if (shelf == extraShelf2 && !(shelf.stream().filter(Objects::nonNull).distinct().findAny().orElse(extraShelfResources.get(1)) == extraShelfResources.get(1))) {
                    return false;
                }
            }
            return true;
        }

        public void updateDashboard(String propertyName, Object objectToUpdate) {
            if (SET_LEADERS.equals(propertyName) || DISCARD_LEADER.equals(propertyName) || LEADER_ACTIVATION.equals(propertyName)) {
                leaderCards = (Map<Integer, Boolean>) objectToUpdate;
            } else if (STRONGBOX_CHANGE.equals(propertyName)) {
                strongbox = (ResourceMap) objectToUpdate;
            } else if (TEMPORARY_SHELF_CHANGE.equals(propertyName) || SHELF_CHANGE.equals(propertyName)) {
                warehouse = (ArrayList<Resource>) objectToUpdate;
            } else if (EXTRA_SHELF_CHANGE.equals(propertyName)) {
                extraShelfResources = (ArrayList<Resource>) objectToUpdate;
            } else if (FAITH_MARKER_POSITION.equals(propertyName)) {
                faithMarker = (Integer) objectToUpdate;
            } else if (BLACK_MARKER_POSITION.equals(propertyName)) {
                blackMarker = (Integer) objectToUpdate;
            } else if (POPES_TILES_CHANGE.equals(propertyName)) {
                popesFavorTiles = (Boolean[]) objectToUpdate;
            } else if (DEVELOPMENTS_CHANGE.equals(propertyName)) {
                developmentCards = (ArrayList<Integer>) objectToUpdate;
            } else if (ACTIVE_DEVELOPMENTS_CHANGE.equals(propertyName)) {
                activeDevelopments = (ArrayList<Integer>) objectToUpdate;
            }
        }
    }
}
