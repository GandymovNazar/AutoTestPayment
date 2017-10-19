import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

class LocalMethods {

    private JSONObject getCurrencySettingsFromFile(String game, String currency) throws IOException {
        String file = new String(Files.readAllBytes(Paths.get(Constants.resources + "/curr_groups/" + game + ".json")));
        JsonNode curr_groups = new JsonNode(file);
        return (JSONObject) curr_groups.getObject().get(currency);
    }

    double getDefaultBetFromFile(String game, String currency) throws IOException {
        JSONObject currencySettings = getCurrencySettingsFromFile(game, currency);
        return Double.parseDouble(currencySettings.get("stakeDef").toString());
    }

    double getMaxBetFromFile(String game, String currency) throws IOException {
        JSONObject currencySettings = getCurrencySettingsFromFile(game, currency);
        return Double.parseDouble(currencySettings.get("stakeMax").toString());
    }

    long getMaxTotalBetFromFile(String game, String currency) throws IOException {
        JSONObject currencySettings = getCurrencySettingsFromFile(game, currency);
        return Long.parseLong(currencySettings.get("maxTotalStake").toString());
    }

    double getMinBetFromFile(String game, String currency) throws IOException {
        JSONObject currencySettings = getCurrencySettingsFromFile(game, currency);
        return Double.parseDouble(currencySettings.get("stakeMin").toString());
    }

    long getMaxWinFromFile(String game, String currency) throws IOException {
        JSONObject currencySettings = getCurrencySettingsFromFile(game, currency);
        return Long.parseLong(currencySettings.get("winMax").toString());
    }

    JSONArray getBetListFromFile(String game, String currency) throws IOException {
        JSONObject currencySettings = getCurrencySettingsFromFile(game, currency);
        return (JSONArray) currencySettings.get("stakeAll");
    }

    Set<String> getAllCurrencies(String game) throws IOException {
        String file = new String(Files.readAllBytes(Paths.get(Constants.resources + "/curr_groups/" + game + ".json")));
        JSONObject curr_groups = new JsonNode(file).getObject();
        return curr_groups.keySet();
    }

    double getCoinRate(String game, String currency) throws IOException {
        JSONObject currencySettings = getCurrencySettingsFromFile(game, currency);
        return Double.parseDouble(currencySettings.get("coinsRate").toString());

    }
}
