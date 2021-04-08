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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
     * @param i row index.
     * @param j column index.
     * @return the deck.
     */
    public Deck getDeck(int i, int j){
        return grid[i][j];
    }

    /**
     * Method generateGrid generates the grid with the 48 development cards from a JSON file.
     */
    public void generateGrid(){
        ArrayList<Card> jsonCards;
        Type listType = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();
        int index = 0;

        try (Reader reader = new FileReader("src/main/resources/json/development_card.json")) {
            // Convert JSON file into list of Java object
            jsonCards = new Gson().fromJson(reader, listType);

            for(int j = 0; j < 4; j++){
                for(int i = 0; i < 3; i++){
                    grid[i][j] = new Deck(jsonCards.subList(index, index + 4));
                    index += 4;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method generateLeaders generates the 16 leader cards from a JSON file, shuffles them and distributed them among the players.
     */
    public void generateLeaders(){
        List<LeaderCard> leaders = new ArrayList<>();
        Iterator<Player> itr = players.iterator();
        int index = 0;

        try (Reader reader = new FileReader("src/main/resources/json/discount_leadercard.json")) {
            ArrayList<DiscountLeaderCard> leads1 = new Gson().fromJson(reader, new TypeToken<ArrayList<DiscountLeaderCard>>(){}.getType());
            for(int i = 0; i < 4; i++){
                leaders.add(leads1.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Reader reader = new FileReader("src/main/resources/json/extrashelf_leadercard.json")) {
            ArrayList<ExtraShelfLeaderCard> leads2 = new Gson().fromJson(reader, new TypeToken<ArrayList<ExtraShelfLeaderCard>>(){}.getType());
            for(int i = 0; i < 4; i++){
                leaders.add(leads2.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Reader reader = new FileReader("src/main/resources/json/production_leadercard.json")) {
            ArrayList<ProductionLeaderCard> leads3 = new Gson().fromJson(reader, new TypeToken<ArrayList<ProductionLeaderCard>>(){}.getType());
            for(int i = 0; i < 4; i++){
                leaders.add(leads3.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Reader reader = new FileReader("src/main/resources/json/whitemarble_leadercard.json")) {
            ArrayList<WhiteMarbleLeaderCard> leads4 = new Gson().fromJson(reader, new TypeToken<ArrayList<WhiteMarbleLeaderCard>>(){}.getType());
            for(int i = 0; i < 4; i++){
                leaders.add(leads4.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(leaders);

        while(itr.hasNext()){
            itr.next().setLeaders(leaders.subList(index, index + 4));
            index += 4;
        }
    }
}
