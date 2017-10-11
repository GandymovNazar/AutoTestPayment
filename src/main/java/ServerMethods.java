import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.apache.http.annotation.Obsolete;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

class ServerMethods {

    private static String apiUrlStage = "https://api.stg.m27613.com/v1/";
    private static String apiUrlProd = "https://api.m27613.com/v1/";
    private static String apiUrlCD2 = "http://api.cd2.d.skywind-tech.com/v1/";

    private static String apiIpmMockStage = "http://104.199.163.39/v1/";
    private static String apiIpmMockProd = "http://104.199.203.12/v1/";
//    private static String apiIpStageGameToken = "http://104.199.171.21/v1/";

    private static String loginUrlStage = apiUrlStage + "login";
    private static String loginUrlProd = apiUrlProd + "login";
    private static String loginUrlCD2 = apiUrlCD2 + "login";

    private JSONArray allGamesFromServer = getAllGamesFromServer(Constants.ENVIRONMENT).getArray();

    ServerMethods() throws UnirestException {
    }


    String getToken(String environment) {
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


    void createPlayer(String custId, String currency, String accessToken) throws UnirestException {

        String body = "{\"cust_id\": \"" + custId + "\"," +
                "\"cust_login\": \"" + custId + "\"," +
                "\"currency_code\": \"" + currency + "\"," +
                "\"language\": \"en\"," +
                "\"country\": \"US\"," +
                "\"first_name\": \"\"," +
                "\"last_name\": \"\"," +
                "\"middle_name\": \"\"," +
                "\"email\": \"\"," +
                "\"phone\": \"\"," +
                "\"street_address\": \"\"," +
                "\"city\": \"\"," +
                "\"postcode\": \"\"," +
                "\"state\": \"\"," +
                "\"secret_question\": \"\"," +
                "\"secret_answer\": \"\"," +
                "\"salutation\": \"\"," +
                "\"sex\": \"\"," +
                "\"referral_info\": \"\"," +
                "\"rakeback\": false," +
                "\"date_of_birth\": \"12/12/1990\"" +
                "}";

        Unirest.post(apiIpmMockStage + "merchant/swftest2_1/customer")
                .header("x-access-token", accessToken)
                .header("Content-Type", "application/json")
                .body(body).asJson();
    }


    String getTicket(String custId) throws UnirestException {
        HttpResponse<String> response = Unirest.get(MessageFormat.format(
                "{0}merchant/swftest2_1/customer/{1}/ticket", apiIpmMockStage, custId))
                .header("Content-Type", "application/json")
                .header("X-ACCESS-TOKEN", "AAAAA")
                .asString();
        return response.getBody();
    }


    void addBalance(String custId, String currency, String balance, String accessToken) throws UnirestException {
        String body = "{\"cust_id\": \"" + custId + "\"," +
                "\"cust_login\": \"" + custId + "\"," +
                "\"currency_code\": \"" + currency + "\"," +
                "\"language\": \"en\"," +
                "\"country\": \"US\"," +
                "\"first_name\": \"\"," +
                "\"last_name\": \"\"," +
                "\"middle_name\": \"\"," +
                "\"email\": \"\"," +
                "\"phone\": \"\"," +
                "\"street_address\": \"\"," +
                "\"city\": \"\"," +
                "\"postcode\": \"\"," +
                "\"state\": \"\"," +
                "\"secret_question\": \"\"," +
                "\"secret_answer\": \"\"," +
                "\"salutation\": \"\"," +
                "\"sex\": \"\"," +
                "\"referral_info\": \"\"," +
                "\"rakeback\": false," +
                "\"date_of_birth\": \"12/12/1990\"" +
                "}";
        Unirest.post(MessageFormat.format(
                apiIpmMockStage + "merchant/swftest2_1/customer/{0}/balance/{1}", custId, balance))
                .header("x-access-token", accessToken)
                .header("Content-Type", "application/json")
                .body(body).asJson();
    }


    String getGameToken(String gameCode, String accessToken, String ticket) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(apiUrlStage + "merchants/game/url")
                .header("x-access-token", accessToken)
                .field("merchantType", "ipm")
                .field("merchantCode", "swftest2_1")
                .field("gameCode", gameCode)
                .field("ticket", ticket)
                .asJson();
        return response.getBody().getObject().get("url").toString();
    }

    @Obsolete
    long getWincappingFromFile(String currency) throws IOException {
        String capping = new String(Files.readAllBytes(Paths.get(Constants.resources + "/winCapping.json")));
        JsonNode winCapping = new JsonNode(capping);
        return Long.parseLong(winCapping.getObject().get(currency).toString());
    }

    @Obsolete
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

    @Obsolete
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


    @Obsolete
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

    @Obsolete
    long getMaxTotalStakeFromFile(String game) throws IOException {
        String file = new String(Files.readAllBytes(Paths.get(Constants.resources + "/totalBetMultiplier.json")));
        JsonNode totalBetMultiplier = new JsonNode(file);
        return Long.parseLong(totalBetMultiplier.getObject().get(game).toString());
    }



    boolean isCurrencyPresentForGameInServer(String game, String currency) {
        for (int i = 0; i < allGamesFromServer.length(); i++) {
            if (Objects.equals(allGamesFromServer.getJSONObject(i).get("code"), game)) {
                JSONObject limits = (JSONObject) allGamesFromServer.getJSONObject(i).get("limits");
                try {
                    limits.get(currency);
                } catch (JSONException e) {
                    return false;
                }
            }
        }
        return true;
    }
}
