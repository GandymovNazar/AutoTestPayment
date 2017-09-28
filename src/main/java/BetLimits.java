import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

class BetLimits {

    private static String loginUrlStage = "https://api.stg.m27613.com/v1/login";
    private static String loginUrlProd = "https://api.m27613.com/v1/login";
    private static String loginUrlCD2 = "http://api.cd2.d.skywind-tech.com/v1/login";
    private JSONArray allGamesFromServer = getAllGamesFromServer(Constants.ENVIRONMENT).getArray();

    BetLimits() throws UnirestException {
    }


    private static String getToken(String environment) {
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            switch (environment) {
                case "stage":
                    jsonResponse = Unirest.post(loginUrlStage)
                            .field("secretKey", "a6fcffac-4270-47a9-a821-299a883bd8be")
                            .field("username", "swftest2_USER")
                            .field("password", "123456qaB")
                            .asJson();
                    break;
                case "production":
                    jsonResponse = Unirest.post(loginUrlProd)
                            .field("secretKey", "MASTER_KEY-swftest_ENTITY-swftest_BRAND")
                            .field("username", "swftest_USER")
                            .field("password", "swftest_QaZ321")
                            .asJson();
                    break;
                case "cd2":
                    jsonResponse = Unirest.post(loginUrlCD2)
                            .field("secretKey", "MASTER_KEY-swftest_ENTITY-swftest_BRAND")
                            .field("username", "swftest_USER")
                            .field("password", "swftest_QaZ321!")
                            .asJson();
                    break;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return jsonResponse.getBody().getObject().get("accessToken").toString();
    }

    long getWincappingFromFile(String currency) throws IOException {
        String capping = new String(Files.readAllBytes(Paths.get(Constants.resources + "/winCapping.json")));
        JsonNode winCapping = new JsonNode(capping);
        return Long.parseLong(winCapping.getObject().get(currency).toString());
    }

    long getWincappingFromFile(String currency, boolean mark4) throws IOException {
        String capping;
        if (!mark4) {
            capping = new String(Files.readAllBytes(Paths.get(Constants.resources + "/winCapping.json")));
        } else {
            capping = new String(Files.readAllBytes(Paths.get(Constants.resources + "/winCappingMark4.json")));
        }
        JsonNode winCapping = new JsonNode(capping);
        return Long.parseLong(winCapping.getObject().get(currency).toString());
    }

    ArrayList<Double> getLimitsFromFile(String gameName, String currency) throws IOException {
        String limitsJson = new String(Files.readAllBytes(Paths.get(Constants.resources + "/limits/" + gameName + "_limits.json")));
        JsonNode allLimits = new JsonNode(limitsJson);
        ArrayList<Double> listdata = new ArrayList<>();
        JSONArray jArray = (JSONArray) allLimits.getObject().get(currency);
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                listdata.add(jArray.getDouble(i));
            }
        }
        return listdata;
    }

    JSONObject getLimitsFromServer(String gameName, String currency) throws UnirestException {
        for (int i = 0; i < allGamesFromServer.length(); i++) {
            if (Objects.equals(allGamesFromServer.getJSONObject(i).get("code"), gameName)) {
                JSONObject limits = (JSONObject) allGamesFromServer.getJSONObject(i).get("limits");
                return (JSONObject) limits.get(currency);
            }

        }
        System.out.println("Game not found");
        return null;
    }

    private JsonNode getAllGamesFromServer(String environment) throws UnirestException {
        String gamesUrl = "";
        switch (environment) {
            case "stage":
                gamesUrl = "https://api.stg.m27613.com/v1/games/?limit=1000";
                break;
            case "production":
                gamesUrl = "https://api.m27613.com/v1/games/?limit=1000";
                break;
            case "cd2":
                gamesUrl = "http://api.cd2.d.skywind-tech.com/v1/games/?limit=1000";
                break;
        }
        GetRequest jsonResponse = Unirest.get(gamesUrl).header("x-access-token", getToken(environment));
        HttpResponse<JsonNode> response = jsonResponse.asJson();
        return response.getBody();
    }

    double getDefaultBetFromServer(String gameName, String currency) throws UnirestException {
        for (int i = 0; i < allGamesFromServer.length(); i++) {
            if (Objects.equals(allGamesFromServer.getJSONObject(i).get("code"), gameName)) {
                JSONObject limits = (JSONObject) allGamesFromServer.getJSONObject(i).get("limits");
                JSONObject currencyLimits = (JSONObject) limits.get(currency);
                return Double.parseDouble(currencyLimits.get("stakeDef").toString());
            }

        }
        return -1;
    }

    int getDefaultBetElement(String game) throws IOException {
        String file = new String(Files.readAllBytes(Paths.get(Constants.resources + "/defaultBetElement.json")));
        JsonNode default_bet_elements = new JsonNode(file);
        return Integer.parseInt(default_bet_elements.getObject().get(game).toString());
    }

    double getMinBetFromServer(String gameName, String currency) throws UnirestException {
        for (int i = 0; i < allGamesFromServer.length(); i++) {
            if (Objects.equals(allGamesFromServer.getJSONObject(i).get("code"), gameName)) {
                JSONObject limits = (JSONObject) allGamesFromServer.getJSONObject(i).get("limits");
                JSONObject currencyLimits = (JSONObject) limits.get(currency);
                return Double.parseDouble(currencyLimits.get("stakeMin").toString());
            }
        }
        return -1;
    }

    double getMaxBetFromServer(String gameName, String currency) throws UnirestException {
        for (int i = 0; i < allGamesFromServer.length(); i++) {
            if (Objects.equals(allGamesFromServer.getJSONObject(i).get("code"), gameName)) {
                JSONObject limits = (JSONObject) allGamesFromServer.getJSONObject(i).get("limits");
                JSONObject currencyLimits = (JSONObject) limits.get(currency);
                return Double.parseDouble(currencyLimits.get("stakeMax").toString());
            }
        }
        return -1;
    }

    long getMaxTotalBetFromServer(String game, String currency) {
        for (int i = 0; i < allGamesFromServer.length(); i++) {
            if (Objects.equals(allGamesFromServer.getJSONObject(i).get("code"), game)) {
                JSONObject limits = (JSONObject) allGamesFromServer.getJSONObject(i).get("limits");
                JSONObject currencyLimits = (JSONObject) limits.get(currency);
                return Long.parseLong(currencyLimits.get("maxTotalStake").toString());
            }
        }
        return -1;
    }

    long getMaxTotalStakeFromFile(String game) throws IOException {
        String file = new String(Files.readAllBytes(Paths.get(Constants.resources + "/totalBetMultiplier.json")));
        JsonNode totalBetMultiplier = new JsonNode(file);
        return Long.parseLong(totalBetMultiplier.getObject().get(game).toString());
    }

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

    boolean isCurrencyPresentForGameInServer(String game, String currency){
        for (int i = 0; i < allGamesFromServer.length(); i++) {
            if (Objects.equals(allGamesFromServer.getJSONObject(i).get("code"), game)) {
                JSONObject limits = (JSONObject) allGamesFromServer.getJSONObject(i).get("limits");
                try {
                    limits.get(currency);
                } catch (JSONException e){
                    return false;
                }
            }
        }
        return true;
    }
}
