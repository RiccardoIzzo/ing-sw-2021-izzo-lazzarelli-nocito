package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.Deck;
import it.polimi.ingsw.model.card.DiscountLeaderCard;
import it.polimi.ingsw.model.card.ExtraShelfLeaderCard;
import it.polimi.ingsw.model.card.WhiteMarbleLeaderCard;
import it.polimi.ingsw.model.card.ProductionLeaderCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Game class contains the main logic of "Master of Renaissance".
 *
 * @author Riccardo Izzo
 */
public class Game {
    private final ArrayList<Player> players;
    private final Market market;
    private final Deck[][] grid;

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
     * Method addPlayer create a nem player and adds it to the list of player.
     * @param name name of the player to be added.
     */
    public void addPlayer(String name){
        players.add(new Player(name));
    }

    /**
     * Method removePlayer remove a player from the list.
     * @param name name of the player to be removed.
     */
    public void removePlayer(String name){
        players.remove(getPlayerByName(name));
    }

    /**
     * Method getPlayerByName return a player instance relying on his name.
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
        ArrayList<Card> jsonCards;
        Type type = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();

        try (Reader reader = new FileReader("src/main/resources/json/development_card.json")) {
            // Convert JSON file into list of Java object
            jsonCards = new Gson().fromJson(reader, type);

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
        Map<String, Type> files = new HashMap<>();
        files.put("src/main/resources/json/discount_leadercard.json", new TypeToken<ArrayList<DiscountLeaderCard>>(){}.getType());
        files.put("src/main/resources/json/extrashelf_leadercard.json", new TypeToken<ArrayList<ExtraShelfLeaderCard>>(){}.getType());
        files.put("src/main/resources/json/production_leadercard.json", new TypeToken<ArrayList<ProductionLeaderCard>>(){}.getType());
        files.put("src/main/resources/json/whitemarble_leadercard.json", new TypeToken<ArrayList<WhiteMarbleLeaderCard>>(){}.getType());

        for(String key : files.keySet()){
            try (Reader reader = new FileReader(key)){
                leaders.addAll(new Gson().fromJson(reader, files.get(key)));
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
