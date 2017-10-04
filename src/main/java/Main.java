import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException, UnirestException {


        // this method for help to technical writers
        String game = "sw_9s1k";
        BetLimits limits = new BetLimits();
        Set<String> allCurrencies = limits.getAllCurrencies(game);
        try {
            PrintWriter writer = new PrintWriter(game + ".markdown", "UTF-8");
            writer.println("Currency | Coin bets");
            writer.println("--- | ---");
            for (String currency : allCurrencies) {
                String defBet = limits.getDefaultBetFromServer(game, currency, true);
                String betListFromFile = limits.getBetListFromFile(game, currency).toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace(",", "; ")
                        .replace(" " + defBet + ";", " **" + defBet + "**;");
                writer.println(currency + " | " + betListFromFile);
            }
            writer.close();
        } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }

    }
