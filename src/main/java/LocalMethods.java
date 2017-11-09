import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

class LocalMethods {

    private String path_slots_limits = Constants.resources + "/limits_slots/";
    
    private JSONObject getCurrencySettingsFromFile(String game, String currency) throws IOException {
        String file = new String(Files.readAllBytes(Paths.get(path_slots_limits + game)));
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
        String file = new String(Files.readAllBytes(Paths.get(path_slots_limits + game)));
        JSONObject curr_groups = new JsonNode(file).getObject();
        return curr_groups.keySet();
    }


    static Object[][] getAllGames(String path) {

        File folder = new File(Constants.resources + "/" + path);
        File[] listOfFiles = folder.listFiles();

        Object[][] objArray = new Object[listOfFiles.length][];

        for (int i = 0; i < listOfFiles.length; i++) {

            objArray[i] = new Object[1];
            objArray[i][0] = listOfFiles[i].getName();
        }

        return Constants.GAME == null ? objArray : new Object[][]{{Constants.GAME + ".json"}};
    }
}
