package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.constants.GameConstants;
import it.polimi.ingsw.model.card.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static it.polimi.ingsw.constants.GameConstants.DEVELOPMENTCARDIDS;
import static it.polimi.ingsw.constants.GameConstants.LEADERCARDIDS;

/**
 * JsonCardsCreator class manages the cards creation from a JSON file.
 *
 * @author Riccardo Izzo
 */
public class JsonCardsCreator {
    /**
    Adapter for Requirement subclasses.
     */
    private static final RuntimeTypeAdapterFactory<Requirement> requirementAdapterFactory = RuntimeTypeAdapterFactory.of(Requirement.class)
            .registerSubtype(LevelRequirement.class,"Level")
            .registerSubtype(NumberRequirement.class, "Number")
            .registerSubtype(ResourceRequirement.class, "Resource");
    private static final Gson gson = new GsonBuilder().registerTypeAdapterFactory(requirementAdapterFactory).create();

    private static final ArrayList<DevelopmentCard> developmentCards = generateDevelopmentCards();
    private static final ArrayList<LeaderCard> leaderCards = generateLeaderCards();

    /**
     * Method generateDevelopmentCards generates a list with the 48 development cards from a JSON file.
     * @return a list of development cards.
     */
    public static ArrayList<DevelopmentCard> generateDevelopmentCards(){
        ArrayList<DevelopmentCard> cards = new ArrayList<>();
        try (Reader reader = new InputStreamReader(JsonCardsCreator.class.getResourceAsStream(GameConstants.developmentCardsJson))) {
            cards = gson.fromJson(reader , new TypeToken<ArrayList<DevelopmentCard>>(){}.getType());
        } catch (IOException e) {
            Logger.getLogger("JsonCardCreator error");
        }
        return cards;
    }

    /**
     * Method generateDevelopmentCard generates a DevelopmentCard given its cardID.
     * @param cardID the card id.
     * @return the development card generated.
     */
    public static DevelopmentCard generateDevelopmentCard(int cardID) {
        return developmentCards.stream().filter(x -> x.getCardID() == cardID).findAny().orElse(null);
    }

    /**
     * Method generateLeaderCards generates a list with the 16 leader cards from a JSON file.
     * @return a list of leader cards.
     */
    public static ArrayList<LeaderCard> generateLeaderCards(){
        ArrayList<LeaderCard> leaders = new ArrayList<>();
        List <Type> types = new ArrayList<>();
        types.add(new TypeToken<ArrayList<DiscountLeaderCard>>(){}.getType());
        types.add(new TypeToken<ArrayList<ExtraShelfLeaderCard>>(){}.getType());
        types.add(new TypeToken<ArrayList<ProductionLeaderCard>>(){}.getType());
        types.add(new TypeToken<ArrayList<WhiteMarbleLeaderCard>>(){}.getType());
        int i = 0;

        for(String filePath : GameConstants.leaderCardsJson){
            try (Reader reader = new InputStreamReader(JsonCardsCreator.class.getResourceAsStream(filePath))){
                leaders.addAll(gson.fromJson(reader, types.get(i++)));
            } catch (IOException e) {
                Logger.getLogger("ClientConnection error");
            }
        }
        return leaders;
    }

    /**
     * Method generateLeaderCard generates a LeaderCard given its cardID.
     * @param cardID the card id.
     * @return the leader card generated.
     */
    public static LeaderCard generateLeaderCard(int cardID) {
        return leaderCards.stream().filter(x -> x.getCardID() == cardID).findAny().orElse(null);
    }

    /**
     * Method generateCard generates a Card given its cardID.
     * @param cardID the card id.
     * @return the card generated.
     */
    public static Card generateCard(int cardID) {
        if (DEVELOPMENTCARDIDS.contains(cardID)) {
            return generateDevelopmentCard(cardID);
        } else if (LEADERCARDIDS.contains(cardID)){
            return generateLeaderCard(cardID);
        } else {
            return null;
        }
    }
}
