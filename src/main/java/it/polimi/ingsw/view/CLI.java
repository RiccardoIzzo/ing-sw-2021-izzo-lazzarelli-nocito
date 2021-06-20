package it.polimi.ingsw.view;

import it.polimi.ingsw.constants.Colors;
import it.polimi.ingsw.events.clientmessages.*;
import it.polimi.ingsw.events.servermessages.InvalidNickname;
import it.polimi.ingsw.events.servermessages.ServerMessage;
import it.polimi.ingsw.events.servermessages.ValidNickname;
import it.polimi.ingsw.model.JsonCardsCreator;
import it.polimi.ingsw.model.MarbleColor;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceMap;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.network.NetworkHandler;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        try {
            int port = new Scanner(System.in).nextInt();
            CLI cli = new CLI(ip, port);
            cli.setNickname();
        } catch (InputMismatchException e){
            System.err.println("Integer requested for the server port, restart the application.");
            System.exit(0);
        }
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
            getResource(bonusResources);
        }
        send(new SendBonusResources(bonusResources));
        handleTurn();
    }

    /**
     * Method getValidAction returns a list of valid user action.
     * @return the list of actions.
     */
    @Override
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
    @Override
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

        System.out.println("\nSelect your next action: ");
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
        ArrayList<Integer> activeWhiteMarbleLeaders = (ArrayList<Integer>) modelView.getMyDashboard().getLeaderCards()
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .filter(leader -> JsonCardsCreator.generateLeaderCard(leader) instanceof WhiteMarbleLeaderCard)
                .collect(Collectors.toList());
        int leaderID = 0;
        if (activeWhiteMarbleLeaders.size() == 2){
            System.out.println("Select one the two possible exchange for the white marble:");
            showCards(activeWhiteMarbleLeaders);
            leaderID = getInt();
            while (!activeWhiteMarbleLeaders.contains(leaderID)){
                System.out.println("Not a valid id.");
                leaderID = getInt();
            }
        } else if (activeWhiteMarbleLeaders.size() == 1){
            leaderID = activeWhiteMarbleLeaders.get(0);
        }
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
        if (leaderID != 0) {
            if(index > 4) send(new TakeResources(index - 4, 1, leaderID));
            else send(new TakeResources(index, 2, leaderID));
        } else {
            if(index > 4) send(new TakeResources(index - 4, 1));
            else send(new TakeResources(index, 2));
        }
        basicActionPlayed();
    }

    /**
     * Method handleBuyCard manages the "BUY_CARD" action, if the requirements are met the player buys a card from the grid.
     */
    @Override
    public void handleBuyCard() {
        ArrayList<Integer> grid = modelView.getGrid();
        ArrayList<Integer> activeDevelopments = modelView.getMyDashboard().getActiveDevelopments();
        ResourceMap discount = modelView.getMyDashboard().getLeaderCards()
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .filter(leader -> JsonCardsCreator.generateLeaderCard(leader) instanceof DiscountLeaderCard)
                .map(leader -> ((DiscountLeaderCard) JsonCardsCreator.generateLeaderCard(leader)).getDiscount())
                .reduce(new ResourceMap(), ResourceMap::addResources);

        showCards(grid);
        System.out.println("Select the card that you want to buy by typing the id: ");

        int id = getInt();
        if(grid.contains(id)) {
            DevelopmentCard cardToBuy = JsonCardsCreator.generateDevelopmentCard(id);

            List<DevelopmentCard> cardsToCover = activeDevelopments.stream()
                    .filter(Objects::nonNull)
                    .map(JsonCardsCreator::generateDevelopmentCard)
                    .filter(card -> card.getLevel() == cardToBuy.getLevel() - 1).collect(Collectors.toList());

            if (!modelView.getMyDashboard().getTotalResources().addResources(discount).removeResources(((ResourceRequirement) cardToBuy.getRequirement()).getResources())) {
                System.out.println("Cannot buy card. Not enough resources:" +
                        "\nMy resources: " + modelView.getMyDashboard().getTotalResources() +
                        "\nDiscount: " + discount +
                        "\nRequired resources: " + cardToBuy.getRequirement());
                handleTurn();
            } else if (cardToBuy.getLevel() > 1 && cardsToCover.size() == 0) {
                System.out.println("Cannot buy card. You don't own a level " + (cardToBuy.getLevel()-1)+ " card.");
                handleTurn();
            } else if (cardToBuy.getLevel() == 1 && activeDevelopments.stream().filter(Objects::nonNull).count() > 2){
                System.out.println("Cannot buy card. Not enough space for a level 1 card.");
                handleTurn();
            } else {
                int slotIndex;
                showActiveDevelopments(modelView.getMyDashboard().getActiveDevelopments());
                System.out.println("\nSelect the slot where you want to place the card:");
                while(true){
                    slotIndex = getInt();
                    if (slotIndex < 1 || slotIndex > 3) {
                        System.out.println("Invalid slot number: choose among {1,2,3}.");
                        continue;
                    }
                    Integer cardToCover = modelView.getMyDashboard().getActiveDevelopments().get(slotIndex-1);
                    if (cardToBuy.getLevel() > 1 && cardToCover != null) {
                        if (JsonCardsCreator.generateDevelopmentCard(cardToCover).getLevel() + 1 == cardToBuy.getLevel()) {
                            break;
                        }
                    } else if (cardToBuy.getLevel() == 1 && cardToCover == null){
                        break;
                    } else {
                        System.out.println("Cannot place your card at slot number " + slotIndex + "." + "Try again:");
                    }
                }
                int index = modelView.getGrid().indexOf(id);
                send(new BuyCard(index / 4, index % 4, slotIndex));
                basicActionPlayed();
                handleTurn();
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

        //Handles the basic production
        ResourceMap inputBasicProduction = new ResourceMap();
        ResourceMap outputBasicProduction = new ResourceMap();
        System.out.println("Do you want to activate the basic production? y/n");
        if(getInput("y|n").equals("y")){
            String resource;
            for(int n = 0; n < 2; n++){
                System.out.println("Select the " + ((n == 0) ? "first" : "second") + " input resource: \n- STONE\n- COIN\n- SHIELD\n- SERVANT");
                getResource(inputBasicProduction);
            }
            System.out.println("Select the output resource: \n- STONE\n- COIN\n- SHIELD\n- SERVANT");
            getResource(outputBasicProduction);
            requiredResources.addResources(inputBasicProduction);
        }

        //Handles the Card production
        if (modelView.getMyDashboard().getAvailableProduction().size() > 0){
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
                    break;
                }
            }
        }

        if(totalResources.removeResources(requiredResources)) {
            if (productions.size() > 0) {
                send(new ActivateProduction(productions));
                basicActionPlayed();
            }
            if (inputBasicProduction.getAmount() != 0) {
                send(new BasicProduction(inputBasicProduction, outputBasicProduction));
                basicActionPlayed();
            }
            if (!Action.ACTIVATE_PRODUCTION.enabled) {
                System.out.println("Production activated!");
            } else {
                System.out.println("No production activated");
            }
        }
        else System.out.println("Not enough resources.");
        handleTurn();
    }

    private void getResource(ResourceMap resourceMap) {
        String resource;
        resource = getInput("stone|coin|shield|servant");
        switch (resource){
            case "stone" -> resourceMap.modifyResource(Resource.STONE, 1);
            case "coin" -> resourceMap.modifyResource(Resource.COIN, 1);
            case "shield" -> resourceMap.modifyResource(Resource.SHIELD, 1);
            case "servant" -> resourceMap.modifyResource(Resource.SERVANT, 1);
        }
    }

    /**
     * Method handleActivateLeader manages the "ACTIVATE_LEADER" action, the player activates a leader card.
     */
    @Override
    public void handleActivateLeader() {
        ArrayList<Integer> inactiveLeaders = (ArrayList<Integer>) modelView.getMyDashboard().getLeaderCards()
                .entrySet()
                .stream()
                .filter(leader -> !leader.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (inactiveLeaders.size() == 0) {
            System.out.println("There are 0 inactive leaders left.");
            handleTurn();
            return;
        }

        showCards(inactiveLeaders);
        System.out.println("Select the card you want to activate by typing the id: ");

        int id = getInt();
        if(inactiveLeaders.contains(id)){
            send(new CheckRequirement(id));
        }
        else {
            System.out.println("Id not valid, choose again.");
            handleTurn();
        }
    }

    /**
     * Method handleCheckRequirement manages CheckRequirement message.
     * @param result outcome of the requirement check.
     * @param id card id.
     */
    @Override
    public void handleCheckRequirement(boolean result, int id) {
        /*
        Requirement for leader cards.
         */
        if(result) {
            send(new ActivateLeaderCard(id));
            System.out.println("Leader activated!");
        }
        else {
            System.out.println("Requirement not met.");
        }
        handleTurn();
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

    /**
     * Method showDashboard prints the visual representation of a player's dashboard: faith track, warehouse, strongbox,
     * leader cards and the visible development cards.
     * @param dashboardView the class containing the data to visualize.
     */
    public void showDashboard(ModelView.DashboardView dashboardView){
        System.out.println("\n*** FAITHTRACK ***");
        showFaithTrack(dashboardView.getFaithMarker(), dashboardView.getBlackMarker(), dashboardView.getPopesFavorTiles());
        System.out.println("\n*** WAREHOUSE ***");
        showWarehouse(dashboardView.getWarehouse(), dashboardView.getExtraShelfResources());
        System.out.println("\n*** STRONGBOX ***");
        showStrongbox(dashboardView.getStrongbox());
        System.out.println("\n*** LEADER CARDS ***");
        showLeaders(dashboardView.getLeaderCards());
        System.out.println("\n*** ACTIVE DEVELOPMENTS ***");
        showActiveDevelopments(dashboardView.getActiveDevelopments());
        handleTurn();
    }

    /**
     * Method showMarket prints the visual representation of the market.
     * @param marketTray the list of marble colors representing the market.
     * @param slideMarble the slide marble color.
     */
    public static void showMarket(ArrayList<MarbleColor> marketTray, MarbleColor slideMarble) {
        System.out.printf("Slide marble = %s\n\n", slideMarble.toString());
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                System.out.print(marketTray.get(i*4+j).toString() + "   ");
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

    /**
     * Method showCards prints the visual representation of a collection of card ids.
     * @param cards the ids whose cards are to show.
     */
    public static void showCards(Collection<Integer> cards) {
        for (Integer card: cards) {
            if (card != null) {
                System.out.println(Objects.requireNonNull(JsonCardsCreator.generateCard(card)));
            }
        }
        System.out.println();
    }

    /**
     * Method showStrongbox prints the visual representation of the strongbox.
     * @param strongbox the ResourceMap representing the strongbox.
     */
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

    /**
     * Method showWarehouse prints the visual representation of the warehouse.
     * @param warehouse the list of resources representing the warehouse.
     * @param extraShelfResources the resource(s) allowed by the ExtraShelfLeaderCard(s).
     */
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

    /**
     * Method warehouseGet gets the element of the index position in the warehouse.
     * @param warehouse the list of resources representing the warehouse.
     * @param index the position of the element to get.
     * @return a empty String if the index position is empty, else the toString method of the element in the index position.
     */
    private static String warehouseGet(ArrayList<Resource> warehouse, int index) {
        if (warehouse.get(index) == null) {
            return " ";
        } else {
            return warehouse.get(index).toString();
        }
    }

    /**
     * Method showFaithTrack prints the visual representation of the faith track.
     * @param f the faith marker position.
     * @param b the black marker position.
     * @param p the pope tiles values.
     */
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
                %n""", f(4,f,b),f(5,f,b),f(6,f,b),f(7,f,b),f(8,f,b),f(9,f,b),p(p[1],3),
                        f(18,f,b),f(19,f,b),f(20,f,b),f(21,f,b),f(22,f,b),f(23,f,b),f(24,f,b),
                        f(3,f,b),p(p[0], 2),f(10,f,b),f(17,f,b),p(p[2],4),f(0,f,b),f(1,f,b),
                        f(2,f,b),f(11,f,b),f(12,f,b),f(13,f,b),f(14,f,b),f(15,f,b),f(16,f,b));
    }

    /**
     * Method f formats the color of a tile in the faithTrack, the faith marker tile is red formatted, the black marker tile
     * has a black background.
     * @param tile the tile number.
     * @param faithMarker the faith marker position.
     * @param blackMarker the black marker position.
     * @return the colored tile.
     */
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

    /**
     * Method p formats a pope tile in the faith track.
     * @param popeTileActive the value representing the state of the pope tile to format.
     * @param points the victory points associated with the pope tile to format.
     * @return the formatted pope tile.
     */
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

    /**
     * Method showLeaders prints the visual representation of the leader cards.
     * @param leaders a map representing the leader cards.
     */
    public static void showLeaders(Map<Integer,Boolean> leaders){
        for (Integer id: leaders.keySet()){
            if (leaders.get(id)) {
                System.out.println(Colors.ANSI_GREEN_BOLD + "\nACTIVE" + Colors.ANSI_RESET);
            } else {
                System.out.println(Colors.ANSI_RED + "\nNOT ACTIVE" + Colors.ANSI_RESET);
            }
            showCards(Set.of(id));
        }
    }

    /**
     * Method showActiveDevelopments prints the visual representation of the three slots of development card in the dashboard.
     * @param activeDevelopments the ids whose cards are at the top of a slot.
     */
    public static void showActiveDevelopments(ArrayList<Integer> activeDevelopments) {
        int slot = 0;
        for (Integer id: activeDevelopments) {
            System.out.printf("\nSLOT #%d\n", slot + 1);
            if (id != null) {
                showCards(Collections.singleton(id));
            } else {
                System.out.print(" -- Empty slot -- ");
                System.out.println();
            }
            slot++;
        }
    }

    /**
     * Method showStats prints the statics at the end of the game showing the winner player.
     * @param gameStats a map containing the statistics.
     */
    public void showStats(Map<String, Integer> gameStats){
        boolean firstPlayer = true;

        for (String player : gameStats.keySet()) {
            if (firstPlayer){
                System.out.printf(Colors.ANSI_GREEN_BOLD + "WINNER\n" + "%-20s" + "%10d" + Colors.ANSI_RESET + "\n", player, gameStats.get(player));
                firstPlayer = false;
            } else {
                System.out.printf("%-20s" + "%10d" + "\n", player,gameStats.get(player));
            }
        }
    }
}
