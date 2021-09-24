package cardgames.dominion;

import cardgames.Card;
import cardgames.dominion.instruction.Instruction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class CardLoader {
    public static List<DominionCard> load(String filePath){
        File file = new File(filePath);
        List<DominionCard> cards = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(file);
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject jsonObject = new JSONObject(tokener);
            Set<String> cardNames = jsonObject.keySet();
            for (String cardName : cardNames) {
                JSONObject jsonCard = jsonObject.getJSONObject(cardName);
                int cost = jsonCard.getInt("cost");
                JSONArray jsonTypes = jsonCard.getJSONArray("types");
                EnumSet<DominionTypes> types = EnumSet.noneOf(DominionTypes.class);
                for (int i = 0; i < jsonTypes.length(); i++){
                    types.add(jsonTypes.getEnum(DominionTypes.class, i));
                }
                JSONArray jsonInstruction = jsonCard.getJSONArray("on_play");
                Instruction effect = Instruction.of(jsonInstruction);
                DominionCard card = new DominionCard(cardName, types, cost, effect);
                cards.add(card);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return cards;
    }
}
