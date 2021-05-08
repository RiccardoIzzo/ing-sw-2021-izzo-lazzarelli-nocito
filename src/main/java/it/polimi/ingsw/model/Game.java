package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.constants.GameConstants;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Game class contains the main logic of "Master of Renaissance".
 *
 * @author Riccardo Izzo
 */
public abstract class Game {
    private final ArrayList<Player> players;
    private final Market market;
    private final Deck[][] grid;

    RuntimeTypeAdapterFactory<Requirement> requirementAdapterFactory = RuntimeTypeAdapterFactory.of(Requirement.class)
            .registerSubtype(LevelRequirement.class,"Level")
            .registerSubtype(NumberRequirement.class, "Number")
            .registerSubtype(ResourceRequirement.class, "Resource");
    Gson gson = new GsonBuilder().registerTypeAdapterFactory(requirementAdapterFactory).create();

    /**
     * Game constructor creates a new Game instance.
     */
    public Game(){
        players = new ArrayList<>();
        market = new Market();
        grid = new Deck[3][4];
    }

    /**
     * Method getPlayers returns the list of players.
     * @return a list of players.
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Method getGrid returns the grid of the development cards.
     * @return the grid.
     */
    public Deck[][] getGrid() {
        return grid;
    }

    /**
     * Method getNumPlayers returns the number of players.
     * @return the number of players.
     */
    public int getNumPlayers(){
        return players.size();
    }

    /**
     * Method addPlayer creates a player and adds it to the list of players.
     * @param name name of the player to be added.
     */
    public void addPlayer(String name){
        if(this instanceof SinglePlayerGame) players.add(new Player(name, true, this));
        else players.add(new Player(name, false, this));

    }

    /**
     * Method removePlayer removes a player from the list.
     * @param name name of the player to be removed.
     */
    public void removePlayer(String name){
        players.remove(getPlayerByName(name));
    }

    /**
     * Method getPlayerByName returns a player instance relying on his name.
     * @param name player name.
     * @return an instance of player.
     */
    public Player getPlayerByName(String name){
        for(Player player : players){
            if(player.getNickname().equals(name)){
                return player;
            }
        }
        return null;
    }

    public void startGame(){

    }

    /**
     * Method getDeck returns the deck of development cards at the specified indexes.
     * @param row row index.
     * @param col column index.
     * @return the deck.
     */
    public Deck getDeck(int row, int col){
        return grid[row][col];
    }

    /**
     * Method generateGrid generates the grid with the 48 development cards from a JSON file.
     */
    public void generateGrid(){
        try (Reader reader = new FileReader(GameConstants.developmentCardsJson)) {
            // Convert JSON file into list of Java object
            ArrayList<DevelopmentCard> jsonCards = gson.fromJson(reader, new TypeToken<ArrayList<DevelopmentCard>>(){}.getType());

            for(int j = 0; j < 4; j++){
                for(int i = 0; i < 3; i++){
                    grid[i][j] = new Deck(jsonCards.subList(0, 4));
                    jsonCards.removeAll(jsonCards.subList(0, 4));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method generateLeaders generates the 16 leader cards from a JSON file, shuffles them and distributes them among the players.
     */
    public void generateLeaders(){
        List<LeaderCard> leaders = new ArrayList<>();
        List <Type> types = new ArrayList<>();
        types.add(new TypeToken<ArrayList<DiscountLeaderCard>>(){}.getType());
        types.add(new TypeToken<ArrayList<ExtraShelfLeaderCard>>(){}.getType());
        types.add(new TypeToken<ArrayList<ProductionLeaderCard>>(){}.getType());
        types.add(new TypeToken<ArrayList<WhiteMarbleLeaderCard>>(){}.getType());
        int i = 0;

        for(String filePath : GameConstants.leaderCardsJson){
            try (Reader reader = new FileReader(filePath)){
                leaders.addAll(gson.fromJson(reader, types.get(i++)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.shuffle(leaders);
        for(Player player : getPlayers()){
            player.setLeaders(leaders.subList(0, 4));
            leaders.removeAll(leaders.subList(0, 4));
        }
    }
}
