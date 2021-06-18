package it.polimi.ingsw.view;

import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.constants.GameConstants.*;
import static it.polimi.ingsw.constants.PlayerConstants.*;

/**
 * ModelView class contains all the visual and functional information the player needs during the game.
 * @author Gabriele Lazzarelli
 */
public class ModelView {
    ArrayList<DashboardView> dashboards;
    private final String myNickname;
    private String currPlayer;
    private ArrayList<Integer> grid;
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
        this.slideMarble = null;
        this.marketTray = new ArrayList<>();
    }

    /**
     * Method getMyNickname returns the player's name associated with this client.
     * @return the nickname of this client.
     */
    public String getMyNickname() {
        return myNickname;
    }

    /**
     * Method getCurrPlayer returns the player name who is playing.
     * @return the name of the player which is currently playing.
     */
    public String getCurrPlayer() {
        return currPlayer;
    }

    /**
     * Method getSlideMarble returns the market's slide marble.
     * @return the market's slide marble.
     */
    public MarbleColor getSlideMarble() {
        return slideMarble;
    }

    /**
     * Method getDashboards returns the list of DashboardView(s) present in the game.
     * @return the list of DashboardView(s) present in the game.
     */
    public ArrayList<DashboardView> getDashboards() {
        return dashboards;
    }

    /**
     * Method getGrid returns a list containing all the cards at the top of each Deck.
     * @return a list containing all the cards at the top of each Deck.
     */
    public ArrayList<Integer> getGrid() {
        return grid;
    }

    /**
     * Method getMarketTray returns the list of MarbleColor representing the market.
     * @return the list of MarbleColor representing the market.
     */
    public ArrayList<MarbleColor> getMarketTray() {
        return marketTray;
    }

    /**
     * Method getMyDashboard returns the DashboardView associated with this client.
     * @return the DashboardView associated with this client.
     */
    public DashboardView getMyDashboard() {
        return dashboards.stream().filter(dashboardView -> dashboardView.getNickname().equals(myNickname)).findAny().orElse(null);
    }

    /**
     * Method updateModelView updates a specific attribute in the ModelView.
     * @param playerSource is the name of the player that triggered a change in the Model.
     * @param propertyName is the name associated the specific attribute which changed.
     * @param objectToUpdate is the the new value of the attribute which changed.
     */
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
            }
        }
    }

    /**
     * DashboardView class is a helper class for the ModelView, it encapsulates all the info needed about a specific player.
     * @author Gabriele Lazzarelli
     */
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

        /**
         * Method DashboardView constructor creates an instance for the specified player name.
         * @param nickname the name of the owner of this dashboard representation.
         */
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

        /**
         * Method getNickname returns the nickname of the owner of this dashboard representation.
         * @return the name of the owner of this dashboard representation.
         */
        public String getNickname() {
            return nickname;
        }

        /**
         * Method getLeaderCards returns the LeaderCard(s) associated with this instance of DashboardView.
         * @return the ids of the LeaderCards in this DashboardView.
         */
        public Map<Integer, Boolean> getLeaderCards() {
            return leaderCards;
        }

        /**
         * Method getDevelopmentCards returns the DevelopmentCard(s) associated with this instance of DashboardView.
         * @return the ids of the DevelopmentCard(s) in this DashboardView.
         */
        public ArrayList<Integer> getDevelopmentCards() {
            return developmentCards;
        }

        /**
         * Method getAvailableProduction return the cards of this DashboardView that have an active PowerProduction.
         * @return the ids of the cards that have an active PowerProduction.
         */
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

        /**
         * Method getActiveDevelopments returns the active DevelopmentCard(s) associated with this instance of DashboardView.
         * @return the ids of the active DevelopmentCard(s) in this DashboardView.
         */
        public ArrayList<Integer> getActiveDevelopments() {
            return activeDevelopments;
        }

        /**
         * Method getStrongbox returns the strongbox associated with this instance of DashboardView.
         * @return the ResourceMap representing the strongbox in this DashboardView.
         */
        public ResourceMap getStrongbox() {
            return strongbox;
        }

        /**
         * Method getWarehouse returns the warehouse associated with this instance of DashboardView.
         * @return the list of resource representing the warehouse in this DashboardView.
         */
        public ArrayList<Resource> getWarehouse() {
            return warehouse;
        }

        /**
         * Method getExtraShelfResources returns the extra shelf resources associated with this instance of DashboardView.
         * @return the extra shelf resources in this DashboardView.
         */
        public ArrayList<Resource> getExtraShelfResources() {
            return extraShelfResources;
        }

        /**
         * Method getFaithMarker returns the position of the faith marker associated with this instance of DashboardView.
         * @return the position of the faith marker in this DashboardView.
         */
        public Integer getFaithMarker() {
            return faithMarker;
        }

        /**
         * Method getBlackMarker returns the position of the black marker associated with this instance of DashboardView.
         * @return the position of the black marker in this DashboardView.
         */
        public Integer getBlackMarker() {
            return blackMarker;
        }

        /**
         * Method getPopesFavorTiles returns the status of the pope tiles associated with this instance of DashboardView.
         * @return the position of the black marker in this DashboardView.
         */
        public Boolean[] getPopesFavorTiles() {
            return popesFavorTiles;
        }

        /**
         * Method setNickname sets the nickname of the owner of this DashboardView.
         */
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        /**
         * Method swapResource swaps the content of two slots.
         * @param firstResource the index of the first slot.
         * @param secondResource the index of the second slot.
         */
        public void swapResources(int firstResource, int secondResource) {
            Collections.swap(warehouse, firstResource, secondResource);
        }

        /**
         * Method shelfResources return the amount of resources of a shelf.
         * @param shelf the selected shelf.
         * @return the number of resources of the selected shelf.
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

        /**
         * Method checkWarehouse checks the warehouse's resource placement of the player before he's allowed to pass his turn.
         * @return true if the resources' configuration respects the game rules, false otherwise.
         */
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

            Resource resourceShelfOne = shelfOne.stream().findAny().orElse(null);
            Resource resourceShelfTwo = shelfTwo.stream().findAny().orElse(null);
            Resource resourceShelfThree = shelfThree.stream().findAny().orElse(null);

            if (resourceShelfOne == resourceShelfTwo && resourceShelfOne != null){
                return false;
            } else if (resourceShelfOne == resourceShelfThree && resourceShelfOne != null){
                return false;
            } else if (resourceShelfThree == resourceShelfTwo && resourceShelfThree != null){
                return false;
            }

            return true;
        }

        /**
         * Method UpdateDashboard updates the value of a specific attribute in this DashboardView.
         * @param propertyName the name associated with the specific attribute.
         * @param objectToUpdate the new value of the specific attribute.
         */
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
