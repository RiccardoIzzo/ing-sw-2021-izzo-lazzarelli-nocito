package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.Colors;
import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.network.NetworkHandler;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static it.polimi.ingsw.constants.GameConstants.DEVELOPMENTCARDIDS;
import static it.polimi.ingsw.constants.GameConstants.LEADERCARDIDS;

/**
 * CLI class manages the game with a Command Line Interface.
 *
 * @author Riccardo Izzo, Gabriele Lazzarelli
 */
public class CLI implements View{
    private String nickname;
    private ModelView modelView;
    private final NetworkHandler network;
    private final Scanner input;

    /**
     * Constructor CLI creates a new CLI instance and prepares the network connection.
     * @param ip server address.
     * @param port server port.
     */
    public CLI(String ip, int port) {
        network = new NetworkHandler(ip, port, this);
        network.setConnection();
        input = new Scanner(System.in);
    }

    /**
     * CLI main method.
     * @param args main args.
     */
    public static void main(String[] args) {
        System.out.println(
                """
                 __  __           _                 ___   __ ____                  _                             \s
                |  \\/  | __ _ ___| |_ ___ _ __ ___ / _ \\ / _|  _ \\ ___ _ __   __ _(_)___ ___  __ _ _ __   ___ ___\s
                | |\\/| |/ _` / __| __/ _ \\ '__/ __| | | | |_| |_) / _ \\ '_ \\ / _` | / __/ __|/ _` | '_ \\ / __/ _ \\
                | |  | | (_| \\__ \\ ||  __/ |  \\__ \\ |_| |  _|  _ <  __/ | | | (_| | \\__ \\__ \\ (_| | | | | (_|  __/
                |_|  |_|\\__,_|___/\\__\\___|_|  |___/\\___/|_| |_| \\_\\___|_| |_|\\__,_|_|___/___/\\__,_|_| |_|\\___\\___|
                """);
        System.out.println("Insert server ip address:");
        String ip = new Scanner(System.in).next();
        System.out.println("Insert server port:");
        int port = new Scanner(System.in).nextInt();
        CLI cli = new CLI(ip, port);
        cli.setNickname();
    }

    /**
     * Method getNickname returns the player's nickname.
     * @return player's nickname.
     */
    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Method setNickname asks for a nickname and sends a SetNickname message to the server with the selected nickname.
     */
    @Override
    public void setNickname() {
        System.out.println("Choose your nickname:");
        nickname = getString();
        send(new SetNickname(nickname));
    }

    /**
     * Method setModelView sets the modelView for this view and for the network.
     * @param modelView the modelView.
     */
    @Override
    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
        network.getServerConnection().setModelView(modelView);
    }

    /**
     * Method handleNickname manages the two possible answers to a SetNickname message, ValidNickname and InvalidNickname.
     * @param message ServerMessage to handle.
     */
    @Override
    public void handleNickname(ServerMessage message){
        if(message instanceof ValidNickname){
            System.out.println("The selected nickname is valid and has been successfully registered.");
            send(new GetLobbies());
        }
        else if(message instanceof InvalidNickname){
            System.out.println("This nickname is not valid! Try again.");
            setNickname();
        }
    }

    /**
     * Method handleLobbies manages the lobby system.
     * First of all it prints the list of available lobbies with the corresponding maximum number of players.
     * Finally it manages three cases: create a lobby, register a player to a lobby and refresh the list of available lobbies.
     * @param lobbies map that associates the lobby id to the maximum number of players for that lobby.
     */
    @Override
    public void handleLobbies(Map<String, Integer> lobbies){
        System.out.println("*** LOBBIES ***");
        for(String lobbyID : lobbies.keySet()){
            System.out.println("--------------------");
            System.out.println("ID: " + lobbyID);
            System.out.println("Number of players: " + lobbies.get(lobbyID));
            System.out.println("--------------------");
        }
        System.out.println("\nDo you want to create your own lobby or join an existing one?");
        System.out.println("- CREATE\n- JOIN\n- REFRESH");
        String choice = getInput("create|join|refresh");
        switch (choice){
            case "create" ->{
                System.out.println("Insert the lobbyID:");
                String lobbyID = getString();
                if(lobbies.containsKey(lobbyID)){
                    System.out.println("Already exists a lobby with this id! Try again.");
                    send(new GetLobbies());
                }
                else {
                    System.out.println("Insert the number of players:");
                    int numPlayers = getInt();
                    while(numPlayers > 4){
                        System.out.println("The maximum number of players is four, choose again.");
                        numPlayers = getInt();
                    }
                    send(new CreateLobby(lobbyID, numPlayers));
                }
            }
            case "join" ->{
                System.out.println("Insert the lobbyID of the lobby you want to join:");
                String lobbyID = getString();
                if(lobbies.containsKey(lobbyID)){
                    send(new JoinLobby(lobbyID));
                }
                else{
                    System.out.println("Error, this lobby doesn't exist.");
                    send(new GetLobbies());
                }
            }
            case "refresh" -> send(new GetLobbies());
        }
    }

    /**
     * Method handleLeaders manages the selection of the leader cards at the beginning of the game.
     */
    @Override
    public void handleLeaders() {
        Set<Integer> ids = modelView.getMyDashboard().getLeaderCards().keySet();
        System.out.println("Select two out of four leader cards...\n");
        for(Integer id : ids){
            LeaderCard card = JsonCardsCreator.generateLeaderCard(id);
            System.out.println(card.toString());
        }

        System.out.println("Select the first card to discard by typing the id: ");
        int firstId = getInt();
        while(!ids.contains(firstId)){
            System.out.println("Id not valid, choose again.");
            firstId = getInt();
        }

        System.out.println("Select the second card to discard by typing the id: ");
        int secondId = getInt();
        while(!ids.contains(secondId)){
            System.out.println("Id not valid, choose again.");
            secondId = getInt();
        }
        send(new SelectLeaderCards(firstId, secondId));
    }

    /**
     * Method handleBonusResources manages the bonus resource system based on the players order at the beginning of the game.
     * @param amount amount of bonus resources that the player can choose.
     */
    @Override
    public void handleBonusResource(int amount) {
        ResourceMap bonusResources = new ResourceMap();
        for(int n = 0; n < amount; n++){
            System.out.println("Select a bonus resource: \n- STONE\n- COIN\n- SHIELD\n- SERVANT");
            String resource = getInput("stone|coin|shield|servant");
            switch (resource){
                case "stone" -> bonusResources.modifyResource(Resource.STONE, 1);
                case "coin" -> bonusResources.modifyResource(Resource.COIN, 1);
                case "shield" -> bonusResources.modifyResource(Resource.SHIELD, 1);
                case "servant" -> bonusResources.modifyResource(Resource.SERVANT, 1);
            }
        }
        send(new SendBonusResources(bonusResources));
        handleTurn();
    }

    /**
     * Method getValidAction returns a list of valid user action.
     * @return the list of actions.
     */
    public ArrayList<Action> getValidActions(){
        ArrayList<Action> actions = new ArrayList<>();
        for(Action action : Action.values()){
            if(action.enabled) actions.add(action);
        }
        return actions;
    }

    /**
     * Method basicActionPlayed disables the three mutually exclusive basic actions.
     */
    @Override
    public void basicActionPlayed(){
        Action.TAKE_RESOURCE.enabled = false;
        Action.BUY_CARD.enabled = false;
        Action.ACTIVATE_PRODUCTION.enabled = false;
    }

    /**
     * Method startTurn at the beginning of the player turn re-enable all actions.
     */
    public void startTurn(){
        for(Action action : Action.values()){
            action.enabled = true;
        }
    }

    /**
     * Method handleTurn manages a player's turn and the available user actions.
     * If there are available actions, it displays them and asks the player to select one.
     */
    @Override
    public void handleTurn() {
        if(getValidActions().size() == 0) {
            System.out.println("Playing: " + modelView.getCurrPlayer() + ". Wait for your turn...");
            return;
        }

        System.out.println("Select your next action: ");
        for(Action action : getValidActions()) {
            System.out.println(action);
        }
        int action = getInt();
        while (!getValidActions().stream().map(Enum::ordinal).collect(Collectors.toList()).contains(action)) {
            System.out.println("Action not valid, try again.");
            action = getInt();
        }

        switch (action) {
            case 0 -> handleTakeResource();
            case 1 -> handleBuyCard();
            case 2 -> handleActivateProduction();
            case 3 -> handleActivateLeader();
            case 4 -> handleDiscardLeader();
            case 5 -> showDashboard(modelView.getMyDashboard());
            case 6 -> handleEndTurn();
        }
    }

    /**
     * Method handleTakeResource manages the "TAKE_RESOURCE" action with the player that takes resources from the market.
     */
    @Override
    public void handleTakeResource() {
        showMarket(modelView.getMarketTray(), modelView.getSlideMarble());
        System.out.println("Insert the row/column index:");
        int index = getInt();
        while(index < 1 || index > 7){
            System.out.println("Row/Column index must be between 1 and 7! Try again.");
            if(changeAction()) {
                handleTurn();
                return;
            }
            index = getInt();
        }
        if(index > 4) send(new TakeResources(index - 4, 1));
        else send(new TakeResources(index, 2));
        basicActionPlayed();
    }

    /**
     * Method handleBuyCard manages the "BUY_CARD" action, if the requirements are met the player buys a card from the grid.
     */
    @Override
    public void handleBuyCard() {
        ArrayList<Integer> grid = modelView.getGrid();
        ArrayList<Integer> activeDevelopments = modelView.getMyDashboard().getActiveDevelopments();
        showCards(grid);
        System.out.println("Select the card that you want to buy by typing the id: ");

        int id = getInt();
        if(grid.contains(id)) {
            DevelopmentCard cardToBuy = JsonCardsCreator.generateDevelopmentCard(id);
            List<DevelopmentCard> cardsToCover = activeDevelopments.stream()
                    .filter(Objects::nonNull)
                    .map(JsonCardsCreator::generateDevelopmentCard)
                    .filter(card -> card.getLevel() == cardToBuy.getLevel() - 1).collect(Collectors.toList());
            if (cardToBuy.getLevel() > 1 && cardsToCover.size() == 0) {
                System.out.println("Cannot buy card. You don't own a level " + (cardToBuy.getLevel()-1)+ " card.");
                handleTurn();
            } else if (cardToBuy.getLevel() == 1 && activeDevelopments.stream().filter(Objects::nonNull).count() > 2){
                System.out.println("Cannot buy card. Not enough space for a level 1 card.");
                handleTurn();
            } else {
                send(new CheckRequirement(id));
            }
        }
        else {
            System.out.println("Id not valid, choose again.");
            handleTurn();
        }
    }

    /**
     * Method handleTemporaryShelf manages the placement of the resources in the warehouse.
     * If the resource configuration in the warehouse is not acceptable or if the player wants to modify the current
     * configuration, the warehouse is shown to the player and he can swap resources by typing the starting slot
     * and the ending slot for the selected resource.
     */
    @Override
    public void handleTemporaryShelf() {
        System.out.println("Place your resources on the shelves");
        int firstSlot, secondSlot;

        List<Integer> validSlots = IntStream.rangeClosed(0, 13)
                .boxed().collect(Collectors.toList());

        if (modelView.getMyDashboard().getExtraShelfResources().size() == 0) {
            validSlots.removeAll(Arrays.asList(6,7,8,9));
        } else if (modelView.getMyDashboard().getExtraShelfResources().size() == 1) {
            validSlots.removeAll(Arrays.asList(8,9));
        }

        while(true){
            if(modelView.getMyDashboard().checkWarehouse()) {
                showWarehouse(modelView.getMyDashboard().getWarehouse(),modelView.getMyDashboard().getExtraShelfResources());
                System.out.println("Are you ok with this resource configuration? y/n");
                if(getInput("y|n").equals("y")) break;
            }
            showWarehouse(modelView.getMyDashboard().getWarehouse(),modelView.getMyDashboard().getExtraShelfResources());
            System.out.println("Select the starting slot: ");
            firstSlot = getInt();
            System.out.println("Select the ending slot: ");
            secondSlot = getInt();
            if(validSlots.contains(firstSlot) && validSlots.contains(secondSlot)){
                modelView.getMyDashboard().swapResources(firstSlot, secondSlot);
            } else {
                System.out.println("Slot numbers are not valid. Choose two slots among:\n" + validSlots);
            }
        }
        send(new SetWarehouse(modelView.getMyDashboard().getWarehouse()));
        handleTurn();
    }

    /**
     * Method handleActivateProduction manages the "ACTIVATE_PRODUCTION" action, the player activate the production.
     */
    @Override
    public void handleActivateProduction() {
        ResourceMap totalResources = modelView.getMyDashboard().getTotalResources();
        ArrayList<Integer> productions = new ArrayList<>();
        ResourceMap requiredResources = new ResourceMap();
        System.out.println("My resources: " + totalResources);
        showCards(modelView.getMyDashboard().getAvailableProduction());
        System.out.println("Select the productions you want to activate: ");
        while(true){
            System.out.println("Add production by typing the id: ");
            int id = getInt();
            if(modelView.getMyDashboard().getAvailableProduction().contains(id)) productions.add(id);
            else System.out.println("Id not valid.");
            System.out.println("Add more? y/n");
            if(getInput("y|n").equals("n")) {
                for(Integer production : productions){
                    Card card = JsonCardsCreator.generateCard(production);
                    if(card instanceof ProductionLeaderCard) requiredResources.addResources(((ProductionLeaderCard) card).getProduction().getInputResource());
                    else if(card instanceof DevelopmentCard) requiredResources.addResources(((DevelopmentCard) card).getProduction().getInputResource());
                }
                if(totalResources.removeResources(requiredResources)) {
                    send(new ActivateProduction(productions));
                    basicActionPlayed();
                    System.out.println("Production activated!");
                }
                else System.out.println("Not enough resources.");
                break;
            }
        }
        handleTurn();
    }

    /**
     * Method handleActivateLeader manages the "ACTIVATE_LEADER" action, the player activates a leader card.
     */
    @Override
    public void handleActivateLeader() {
        showCards(modelView.getMyDashboard().getLeaderCards().keySet());
        System.out.println("Select the card you want to activate by typing the id: ");

        int id = getInt();
        if(modelView.getMyDashboard().getLeaderCards().containsKey(id)){
            if(modelView.getMyDashboard().getLeaderCards().get(id)){
                System.out.println("Card" + id + "already active");
                handleTurn();
            }
            else send(new CheckRequirement(id));
        }
        else {
            System.out.println("Id not valid, choose again.");
            handleTurn();
        }
    }

    /**
     * Method handleCheckRequirement manages CheckRequirement message in two different cases: "BUY_CARD" and "ACTIVATE_LEADER" actions.
     * @param result outcome of the requirement check.
     * @param id card id.
     */
    @Override
    public void handleCheckRequirement(boolean result, int id) {
        /*
        Requirement for development cards.
         */
        if(DEVELOPMENTCARDIDS.contains(id)){
            if(result) {
                DevelopmentCard cardToBuy = JsonCardsCreator.generateDevelopmentCard(id);
                int slotIndex;
                while(true){
                    System.out.println("Select the slot where you want to place the card:");
                    slotIndex = getInt();
                    Integer cardToCover = modelView.getMyDashboard().getActiveDevelopments().get(slotIndex);
                    if (cardToBuy.getLevel() > 1 && cardToCover != null) {
                        if (JsonCardsCreator.generateDevelopmentCard(cardToCover).getLevel() + 1 == cardToBuy.getLevel()) {
                            break;
                        }
                    } else if (cardToBuy.getLevel() == 1 && cardToCover == null){
                        break;
                    } else {
                        System.out.println("Cannot place your card at slot number " + slotIndex + ".");
                    }
                }
                int index = modelView.getGrid().indexOf(id);
                send(new BuyCard(index / 4, index % 4, slotIndex));
                basicActionPlayed();
            }
            else {
                System.out.println("Requirement not met.");
            }
            handleTurn();
        }
        /*
        Requirement for leader cards.
         */
        else if (LEADERCARDIDS.contains(id)) {
            if(result) {
                send(new ActivateLeaderCard(id));
            }
            else {
                System.out.println("Requirement not met.");
                handleTurn();
            }
        }
    }

    /**
     * Method handleDiscardLeader manages the "DISCARD_LEADER" action, the player select a leader card to discard.
     */
    @Override
    public void handleDiscardLeader() {
        Set<Integer> ids = modelView.getMyDashboard().getLeaderCards().keySet();
        if(ids.size() == 0) {
            System.out.println("No remaining leader cards.");
            handleTurn();
            return;
        }
        else{
            for(Integer id : ids){
                LeaderCard card = JsonCardsCreator.generateLeaderCard(id);
                System.out.println(card.toString());
            }
        }

        System.out.println("Select the leader card to discard by typing the id: ");
        int id = getInt();
        while(!ids.contains(id)){
            System.out.println("Id not valid, choose again.");
            if(changeAction()) {
                handleTurn();
                return;
            }
            id = getInt();
        }
        send(new DiscardLeaderCard(id));
        handleTurn();
    }

    /**
     * Method handleEndTurn manages the "END_TURN" action with the player that decides to end his turn.
     */
    @Override
    public void handleEndTurn() {
        System.out.println("Your turn is finished.");
        for(Action action : Action.values()){
            action.enabled = false;
        }
        send(new EndTurn());
    }

    /**
     * Method changeAction asks the user if he wants to change action.
     * @return true if the user wants to change action, false otherwise.
     */
    private boolean changeAction(){
        System.out.println("Do you want to change action? y/n");
        String option = getInput("y|n");
        if(option.equals(("y"))) return true;
        else{
            System.out.println("Continue with your current action.");
            return false;
        }
    }

    /**
     * Method getInput gets the user input and checks the validity by comparing the input with the available options.
     * @param check available options.
     * @return the valid user input.
     */
    @Override
    public String getInput(String check) {
        String option = getString().toLowerCase();
        while(!option.matches(check)){
            System.out.println("This option is not available, try again.");
            option = getString();
        }
        return option;
    }

    /**
     * Method getString reads a string after resetting the scanner.
     * Finally it returns the string.
     * @return the string.
     */
    private String getString(){
        input.reset();
        return input.nextLine();
    }

    /**
     * Method getInt resets the scanner, process any remaining characters and reads an integer.
     * Finally it returns the integer.
     * @return the integer.
     */
    private int getInt(){
        input.reset();
        int n = - 1;
        while (input.hasNext()){
            if (input.hasNextInt()){
                n = input.nextInt();
                break;
            } else {
                input.next();
            }
        }
        input.nextLine();
        return n;
    }

    /**
     * Method printText prints the given text on the terminal.
     * @param text text to print.
     */
    @Override
    public void printText(String text){
        System.out.println(text);
    }

    /**
     * Method send dispatch a message to the server.
     * @param message the ClientMessage to send.
     */
    @Override
    public void send(ClientMessage message) {
        network.sendToServer(message);
    }

    @Override
    public void handleSoloActionToken() {

    }

    public void showDashboard(ModelView.DashboardView dashboardView){
        System.out.println("\n*** FAITHTRACK ***");
        showFaithTrack(dashboardView.getFaithMarker(), dashboardView.getBlackMarker(), dashboardView.getPopesFavorTiles());
        System.out.println("\n*** WAREHOUSE ***");
        showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
        System.out.println("\n*** STRONGBOX ***");
        showStrongbox(dashboardView.getStrongbox());
        System.out.println("\n*** LEADER CARDS ***");
        showCards(dashboardView.getLeaderCards().keySet());
        System.out.println("\n*** ACTIVE DEVELOPMENTS ***");
        showCards(dashboardView.getActiveDevelopments().stream().filter(Objects::nonNull).collect(Collectors.toList()));
        handleTurn();
    }

    public static void showMarket(ArrayList<MarbleColor> marketTray, MarbleColor slideMarble) {
        System.out.printf("Slide marble = %s\n\n", slideMarble.toString());
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                System.out.print(marketTray.get(i*4+j).toString() + "\t");
            }
            System.out.printf("<-- %d\n", 5 + i);
        }
        System.out.println(
                """
                ^   ^   ^   ^
                |   |   |   |
                1   2   3   4
                """
        );
    }

    void showCards(Collection<Integer> cards) {
        for (Integer card: cards) {
            System.out.println(JsonCardsCreator.generateCard(card).toString());
        }
    }

    public static void showStrongbox(ResourceMap strongbox){
        System.out.printf("""
                            ╔══
                            ║   %s
                            ║   %s
                            ║   %s
                            ║   %s
                            ╚══
                        %n""", Resource.STONE + " x " + strongbox.getResource(Resource.STONE).toString(),
                        Resource.SERVANT + " x " + strongbox.getResource(Resource.SERVANT).toString(),
                        Resource.SHIELD + " x " + strongbox.getResource(Resource.SHIELD).toString(),
                        Resource.COIN + " x " + strongbox.getResource(Resource.COIN).toString());
    }

    public static void showWarehouse(ArrayList<Resource> warehouse, ArrayList<Resource> extraShelfResources){
        String firstExtraShelf = "", firstIndexPair = "", secondExtraShelf = "", secondIndexPair = "";
        if (extraShelfResources.size() > 0){
            firstIndexPair = "6   7";
            firstExtraShelf = String.format("⎣%s⎦ ⎣%s⎦  <-- 1st Leader card shelf {%s}", warehouseGet(warehouse,6),warehouseGet(warehouse,7), extraShelfResources.get(0));
        }
        if (extraShelfResources.size() > 1){
            secondIndexPair = "8   9";
            secondExtraShelf = String.format("⎣%s⎦ ⎣%s⎦  <-- 2nd Leader card shelf {%s}",warehouseGet(warehouse,8),warehouseGet(warehouse,9), extraShelfResources.get(1));
        }
        System.out.printf("""
                                                 +-0-+             %s
                                                 | %s |            %s
                                               +-1-+-2-+
                                               | %s | %s |           %s
                                             +-3-+-4-+-5-+        %s
                                             | %s | %s | %s |
                                             +---+---+---+
                                            10  11   12  13
                                            ⎣%s⎦ ⎣%s⎦ ⎣%s⎦ ⎣%s⎦
                        %n""", firstIndexPair, warehouseGet(warehouse,0), firstExtraShelf, warehouseGet(warehouse,1), warehouseGet(warehouse,2),
                secondIndexPair, secondExtraShelf, warehouseGet(warehouse,3), warehouseGet(warehouse,4), warehouseGet(warehouse,5),
                warehouseGet(warehouse,10), warehouseGet(warehouse,11), warehouseGet(warehouse,12), warehouseGet(warehouse,13));
    }
    private static String warehouseGet(ArrayList<Resource> warehouse, int index) {
        if (warehouse.get(index) == null) {
            return " ";
        } else {
            return warehouse.get(index).toString();
        }
    }

    public static void showFaithTrack(int f, int b, Boolean[] p) {
        System.out.printf(
                """
                          +----╔═══════════════════╗----+    ╔═════════╗    +----╔═════════════════════════════╗
                          | %s ║ %s | %s | %s | %s ║ %s |    ║   %s   ║    | %s ║ %s | %s | %s | %s | %s | %s ║
                          +----╚════╗----+----╔════╝----+    ║         ║    +----╚═════════╗----+----╔═════════╝
                          | %s |    ║   %s   ║    | %s |    ║         ║    | %s |         ║   %s   ║
                +----+----+----+    ║         ║    +----╔════╝----+----╚═════════╗         ║         ║
                | %s | %s | %s |    ║         ║    | %s ║ %s | %s | %s | %s | %s ║         ║         ║
                +----+----+----+    ╚═════════╝    +----╚════════════════════════╝         ╚═════════╝
                %n""", f(4,f,b),f(5,f,b),f(6,f,b),f(7,f,b),f(8,f,b),f(9,f,b),p(p[1],2),
                        f(18,f,b),f(19,f,b),f(20,f,b),f(21,f,b),f(22,f,b),f(23,f,b),f(24,f,b),
                        f(3,f,b),p(p[0], 3),f(10,f,b),f(17,f,b),p(p[2],4),f(0,f,b),f(1,f,b),
                        f(2,f,b),f(11,f,b),f(12,f,b),f(13,f,b),f(14,f,b),f(15,f,b),f(16,f,b));
    }

    private static String f(int tile, int faithMarker, int blackMarker){
        Colors backgroundColor = (tile == blackMarker) ? Colors.ANSI_BLACK_BACKGROUND: Colors.ANSI_RESET;
        Colors numberColor = (tile == faithMarker) ? Colors.ANSI_RED : Colors.ANSI_WHITE;
        String tileColor;
        if (tile < 10) {
            tileColor = String.format("%s%s0%d%s", backgroundColor, numberColor, tile, Colors.ANSI_RESET);
        } else {
            tileColor = String.format("%s%s%d%s", backgroundColor, numberColor, tile, Colors.ANSI_RESET);
        }
        return tileColor;
    }

    private static String p(Boolean popeTileActive, int points) {
        Colors numberColor;
        if(popeTileActive != null) {
            numberColor = popeTileActive ? Colors.ANSI_YELLOW_BACKGROUND : Colors.ANSI_RED_BACKGROUND;
            return String.format("%s%sVP%s", numberColor, points, Colors.ANSI_RESET);
        }
        else {
            return "   ";
        }
    }
}
